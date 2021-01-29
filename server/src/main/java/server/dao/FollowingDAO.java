package server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.Page;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.domain.User;
import model.service.request.FollowerRequest;
import model.service.request.FollowingRequest;
import model.service.response.FollowerResponse;
import model.service.response.FollowingResponse;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;
import model.service.request.FollowStateRequest;
import model.service.response.FollowStateResponse;
import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;
import model.service.request.UnfollowUserRequest;
import model.service.response.UnfollowUserResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowingDAO {
    // This is the hard coded followee data returned by the 'getFollowees()' method
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
    private DynamoDB dynamoDB = new DynamoDB(client);
    private Table table = dynamoDB.getTable("follows");

    public FollowUserResponse followUser(FollowUserRequest request) {
        //return new FollowUserResponse();

        try {
            table.putItem(new Item()
                    .withPrimaryKey("follower_handle", request.getRootUser().getAlias(),
                            "followee_handle", request.getUserToFollow().getAlias()));
            return new FollowUserResponse();
        } catch (Exception e) {
            return new FollowUserResponse(e.toString());
        }
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        //return new UnfollowUserResponse();

        try {
            table.deleteItem(new DeleteItemSpec()
                    .withPrimaryKey("follower_handle", request.getRootUser().getAlias(),
                            "followee_handle", request.getUserToUnfollow().getAlias()));
            return new UnfollowUserResponse();
        } catch (Exception e) {
            return new UnfollowUserResponse(e.toString());
        }
    }

    public FollowStateResponse followState(FollowStateRequest request) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower_handle", request.getRootUser().getAlias(),
                "followee_handle", request.getUserGiven().getAlias());
        try {
            Item outcome = table.getItem(spec);
            if (outcome.isNull("follower_handle")) return new FollowStateResponse(false);
            else return new FollowStateResponse(true);
        } catch (Exception e) {
            return new FollowStateResponse(false);
        }
    }

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     *  the User whose count of how many following is desired.
     * @return said count.
     */
//    public NumFollowersFollowingResponse getNumFollowersFollowing(NumFollowersFollowingRequest request) {
//        return new NumFollowersFollowingResponse(getDummyFollowers().size(), getDummyFollowees().size());
//    }

    public void addFollowersBatch(List<String> followers, String target) {

        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems("follows");

        // Add each user into the TableWriteItems object
        for (String follower : followers) {
            Item item = new Item()
                    .withPrimaryKey("follower_handle", follower, "followee_handle", target);
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems("follows");
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

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        assert request.getLimit() > 0;
        assert request.getFollower() != null;

        List<User> responseFollowees = new ArrayList<>(request.getLimit());
        UserDAO userDAO = new UserDAO();

        QuerySpec spec = new QuerySpec();
        if (request.getLastFollowee() != null) {
            spec
                .withKeyConditionExpression("follower_handle = :alias")
                .withValueMap(new ValueMap()
                        .withString(":alias", request.getFollower().getAlias()))
                .withConsistentRead(true)
                .withExclusiveStartKey("follower_handle", request.getFollower().getAlias(),
                        "followee_handle", request.getLastFollowee().getAlias())
                .withMaxPageSize(request.getLimit());
        } else {
            spec
                .withKeyConditionExpression("follower_handle = :alias")
                .withValueMap(new ValueMap()
                        .withString(":alias", request.getFollower().getAlias()))
                .withConsistentRead(true)
                .withMaxPageSize(request.getLimit());
        }

        ItemCollection<QueryOutcome> items = table.query(spec);

        Page<Item, QueryOutcome> page = items.firstPage();
        Iterator<Item> item = page.iterator();
        while (item.hasNext()) {
            responseFollowees.add(userDAO.getUserByAlias(item.next().getString("followee_handle")));
        }

        return new FollowingResponse(responseFollowees, page.hasNextPage());

//        List<User> allFollowees = getDummyFollowees();
//        List<User> responseFollowees = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowees != null) {
//                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);
//
//                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//                    responseFollowees.add(allFollowees.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowees.size();
//            }
//        }
//
//        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowee the last followee that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.equals(allFollowees.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

    public List<String> getFollowers(String alias) {

        Index table = dynamoDB.getTable("follows").getIndex("follows_index");

        List<String> responseFollowers = new ArrayList<>();

        //Page<Item, QueryOutcome> page = null;
//        do {
            QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression("followee_handle = :alias")
                    .withValueMap(new ValueMap()
                    .withString(":alias", alias));

//            if (page != null) spec.withExclusiveStartKey("followee_handle", alias,
//                    "follower_handle", responseFollowers.get(responseFollowers.size()-1));

            ItemCollection<QueryOutcome> items = table.query(spec);

            Iterator<Item> itemIterator = items.iterator();
            Item item = null;
                while (itemIterator.hasNext()) {
                    item = itemIterator.next();
                    responseFollowers.add(item.getString("follower_handle"));
                }
//        } while (page.hasNextPage());

        return responseFollowers;
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        assert request.getLimit() > 0;
        assert request.getFollowee() != null;

        Index table = dynamoDB.getTable("follows").getIndex("follows_index");

        List<User> responseFollowers = new ArrayList<>(request.getLimit());
        UserDAO userDAO = new UserDAO();

        QuerySpec spec = new QuerySpec();
        if (request.getLastFollower() != null) {
            spec
                    .withKeyConditionExpression("followee_handle = :alias")
                    .withValueMap(new ValueMap()
                            .withString(":alias", request.getFollowee().getAlias()))
                    .withExclusiveStartKey("followee_handle", request.getFollowee().getAlias(),
                            "follower_handle", request.getLastFollower().getAlias())
                    .withMaxPageSize(request.getLimit());
        } else {
            spec
                    .withKeyConditionExpression("followee_handle = :alias")
                    .withValueMap(new ValueMap()
                            .withString(":alias", request.getFollowee().getAlias()))
                    .withMaxPageSize(request.getLimit());
        }

        ItemCollection<QueryOutcome> items = table.query(spec);

        Page<Item, QueryOutcome> page = items.firstPage();
        Iterator<Item> item = page.iterator();
        while (item.hasNext()) {
            responseFollowers.add(userDAO.getUserByAlias(item.next().getString("follower_handle")));
        }

        return new FollowerResponse(responseFollowers, page.hasNextPage());

//        List<User> allFollowers = getDummyFollowers();
//        List<User> responseFollowers = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if (request.getLimit() > 0) {
//            if (allFollowers != null) {
//                int followersIndex = getFollowersStartingIndex(request.getLastFollower(), allFollowers);
//
//                for (int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
//                    responseFollowers.add(allFollowers.get(followersIndex));
//                }
//
//                hasMorePages = followersIndex < allFollowers.size();
//            }
//        }
//
//        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    private int getFollowersStartingIndex(User lastFollower, List<User> allFollowers) {

        int followersIndex = 0;

        if (lastFollower != null) {
            for (int i = 0; i < allFollowers.size(); i++) {
                if (lastFollower.equals(allFollowers.get(i))) {
                    followersIndex = i + 1;
                }
            }
        }

        return followersIndex;
    }

    List<User> getDummyFollowers() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user20, user19, user18, user17, user16, user15, user14,
                user13, user12, user11);
    }
}
