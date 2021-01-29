package server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

import model.domain.User;
import model.service.request.NumFollowersFollowingRequest;
import model.service.request.SignupRequest;
import model.service.response.NumFollowersFollowingResponse;
import server.helper.Hasher;

public class UserDAO {

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
    private DynamoDB dynamoDB = new DynamoDB(client);
    private Table table = dynamoDB.getTable("user");

    public User getUser(String username) {
//        return new User("Test", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", username);
        try {
            Item outcome = table.getItem(spec);
            return new User(outcome.get("firstName").toString(), outcome.get("lastName").toString(),
                    outcome.get("alias").toString(), outcome.get("password").toString(),
                    outcome.get("imageURL").toString());
        } catch (Exception e) {
            return new User();
        }

    }

    public User getUserByAlias(String alias) {
//        return new User("Fake", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        try {
            Item outcome = table.getItem(spec);
            return new User(outcome.get("firstName").toString(), outcome.get("lastName").toString(),
                    outcome.get("alias").toString(), outcome.get("imageURL").toString());
        } catch (Exception e) {
            return new User();
        }
    }

    public User createUser(SignupRequest request, String imageURL) {
//        return new User(request.getFirstName(), request.getLastName(),
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Hasher hasher = new Hasher();
        String password = null;
        try {
            password = hasher.generateStorngPasswordHash(request.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        try {
            PutItemOutcome outcome = putItemDynamo(table,
                                                    request.getUsername(),
                                                    password,
                                                    request.getFirstName(),
                                                    request.getLastName(),
                                                    imageURL);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new User(request.getFirstName(), request.getLastName(), request.getUsername(),
                request.getPassword(), imageURL);

    }

    public void updateNumFollow(String rootAlias, String otherAlias, boolean isFollow) {

        try {
            GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", rootAlias);
            Item outcome = table.getItem(spec);
            int numFollowing = outcome.getInt("numFollowing");
            if (isFollow) numFollowing++;
            else numFollowing--;

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", rootAlias)
                    .withUpdateExpression("set numFollowing = :p")
                    .withValueMap(new ValueMap().withInt(":p", numFollowing));

            table.updateItem(updateItemSpec);

            GetItemSpec specOther = new GetItemSpec().withPrimaryKey("alias", otherAlias);
            Item outcomeOther = table.getItem(specOther);
            int numFollowers = outcomeOther.getInt("numFollowers");
            if (isFollow) numFollowers++;
            else numFollowers--;

            UpdateItemSpec updateItemSpecOther = new UpdateItemSpec().withPrimaryKey("alias", otherAlias)
                    .withUpdateExpression("set numFollowers = :p")
                    .withValueMap(new ValueMap().withInt(":p", numFollowers));

            table.updateItem(updateItemSpecOther);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NumFollowersFollowingResponse getNumFollow(NumFollowersFollowingRequest request) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", request.getUser().getAlias());
        Item outcome = table.getItem(spec);
        try {
            int numFollowing = outcome.getInt("numFollowing");
            int numFollowers = outcome.getInt("numFollowers");
            return new NumFollowersFollowingResponse(numFollowers, numFollowing);
        } catch (Exception e) {
            return new NumFollowersFollowingResponse(0, 0);
        }
    }

    private static PutItemOutcome putItemDynamo(Table table, String alias, String password, String firstName, String lastName, String imageURL) {
        return table.putItem(new Item()
                .withPrimaryKey("alias", alias)
                .withString("password", password)
                .withString("firstName", firstName)
                .withString("lastName", lastName)
                .withString("imageURL", imageURL)
                .withInt("numFollowers", 0)
                .withInt("numFollowing", 0));
    }

    public void addUserBatch(List<User> users) {

        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems("user");

        // Add each user into the TableWriteItems object
        for (User user : users) {
            Item item = new Item()
                    .withPrimaryKey("alias", user.getAlias())
                    .withString("password", user.getPassword())
                    .withString("firstName", user.getFirstName())
                    .withString("lastName", user.getLastName())
                    .withString("imageURL", user.getImageUrl());
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems("user");
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        //logger.log("Wrote User Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            //logger.log("Wrote more Users");
        }
    }
}
