package server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import org.joda.time.DateTime;

import model.domain.AuthToken;
import model.service.request.LoginRequest;
import model.service.request.LogoutRequest;
import model.service.request.SignupRequest;
import model.service.response.LogoutResponse;

public class AuthTokenDAO {

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
    private DynamoDB dynamoDB = new DynamoDB(client);
    private Table table = dynamoDB.getTable("authToken");

    public AuthToken getAuthToken(LoginRequest loginRequest) {
        AuthToken authToken = new AuthToken();
        try {
            PutItemOutcome outcome = table.putItem(new Item()
                    .withPrimaryKey("alias", loginRequest.getUsername())
                    .withString("token", authToken.getToken())
                    .withLong("timestamp", (System.currentTimeMillis() / 1000L) + 600));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return authToken;
    }

    public AuthToken getAuthToken(SignupRequest request) {

        AuthToken authToken = new AuthToken();
        try {
            PutItemOutcome outcome = table.putItem(new Item()
                    .withPrimaryKey("alias", request.getUsername())
                    .withString("token", authToken.getToken()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return authToken;
    }

    public boolean validAuth(String alias) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", alias);
        try {
            Item outcome = table.getItem(spec);
            if (outcome.isNull("token")) return false;
            else {
                //updateAuth(alias);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void updateAuth(String alias) {
//        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", alias)
//                .withUpdateExpression("set timestamp = :p")
//                .withValueMap(new ValueMap().withLong(":p", System.currentTimeMillis() / 1000L + 600))
//                .withReturnValues(ReturnValue.UPDATED_NEW);
//
//        try {
//            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
//        } catch (Exception e) {
//            //System.err.println(e.getMessage());
//        }

        AuthToken authToken = new AuthToken();
        try {
            PutItemOutcome outcome = table.putItem(new Item()
                    .withPrimaryKey("alias", alias)
                    .withString("token", authToken.getToken()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey("alias", logoutRequest.getUser().getAlias());

        try {
            table.deleteItem(deleteItemSpec);
            return new LogoutResponse();
        } catch (Exception e) {
            return new LogoutResponse(e.toString());
        }
        //return new LogoutResponse();
    }
}
