package server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.Page;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.domain.Feed;
import model.domain.Status;
import model.domain.User;
import model.service.response.FeedResponse;
import model.service.request.FeedRequest;

public class FeedDAO {

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


    private final Status status1 = new Status("Hello @GiovannaGiles I would love it if you checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user2), Arrays.asList("https://www.google.com"), user1);
    private final Status status2 = new Status("Hello @DeeDempsey I would love it if you checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user3), Arrays.asList("https://www.google.com"), user2);
    private final Status status3 = new Status("Hello @FranFranklin I would love it if you and @JustinJones checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user4), Arrays.asList("https://www.google.com"), user3);
    private final Status status4 = new Status("Hello @GiovannaGiles I would love it if you checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user2), Arrays.asList("https://www.google.com"), user4);
    private final Status status5 = new Status("Hello @DeeDempsey I would love it if you checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user3), Arrays.asList("https://www.google.com"), user5);
    private final Status status6 = new Status("Hello @FranFranklin I would love it if you and @JustinJones checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user4), Arrays.asList("https://www.google.com"), user6);
    private final Status status7 = new Status("Hello @GiovannaGiles I would love it if you checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user2), Arrays.asList("https://www.google.com"), user7);
    private final Status status8 = new Status("Hello @DeeDempsey I would love it if you checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user3), Arrays.asList("https://www.google.com"), user8);
    private final Status status9 = new Status("Hello @FranFranklin I would love it if you and @JustinJones checked this link out: https://www.google.com", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), Arrays.asList(user4), Arrays.asList("https://www.google.com"), user9);

    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
    private DynamoDB dynamoDB = new DynamoDB(client);
    private Table table = dynamoDB.getTable("feed");

    public FeedResponse getFeed(FeedRequest request) {

        assert request.getLimit() > 0;
        assert request.getUser() != null;

        List<Status> responseStatuses = new ArrayList<>(request.getLimit());
        UserDAO userDAO = new UserDAO();

        QuerySpec spec = new QuerySpec();
        if (request.getLastStatus() != null) {
            spec
                    .withKeyConditionExpression("alias = :alias")
                    .withScanIndexForward(false)
                    .withValueMap(new ValueMap()
                            .withString(":alias", request.getUser().getAlias()))
                    .withConsistentRead(true)
                    .withExclusiveStartKey("alias", request.getUser().getAlias(),
                            "timestamp", request.getLastStatus().getTimestampString())
                    .withMaxPageSize(request.getLimit());
        } else {
            spec
                    .withKeyConditionExpression("alias = :alias")
                    .withScanIndexForward(false)
                    .withValueMap(new ValueMap()
                            .withString(":alias", request.getUser().getAlias()))
                    .withConsistentRead(true)
                    .withMaxPageSize(request.getLimit());
        }

        ItemCollection<QueryOutcome> items = table.query(spec);

        Page<Item, QueryOutcome> page = items.firstPage();
        Iterator<Item> item = page.iterator();
        while (item.hasNext()) {
            Item currItem = item.next();
            responseStatuses.add(new Status(currItem.getString("message"),
                    currItem.getString("timestamp"),
                    null, null,
                    userDAO.getUserByAlias(currItem.getString("statusAlias"))));
        }

        return new FeedResponse(new Feed(responseStatuses), page.hasNextPage());

//        Feed completeFeed = getDummyFeed();
//        List<Status> responseStatuses = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if (request.getLimit() > 0) {
//            if (completeFeed != null) {
//                int statusIndex = getFeedStartingIndex(request.getLastStatus(), completeFeed);
//
//                for (int limitCounter = 0; statusIndex < completeFeed.getSize() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
//                    responseStatuses.add(completeFeed.getStatusAt(statusIndex));
//                }
//
//                hasMorePages = statusIndex < completeFeed.getSize();
//            }
//        }
//
//        return new FeedResponse(new Feed(responseStatuses), hasMorePages);
    }

    public void batchPutFeed(List<String> aliasBatch, String message, String timestamp, String statusAlias) {

        TableWriteItems items = new TableWriteItems("feed");

        for (String alias : aliasBatch) {
            Item item = new Item()
                    .withPrimaryKey("alias", alias, "timestamp", timestamp)
                    .withString("message", message)
                    .withString("statusAlias", statusAlias);
            items.addItemToPut(item);
        }

        loopBatchWrite(items);
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

    private int getFeedStartingIndex(Status lastStatus, Feed completeFeed) {
        int statusIndex = 0;

        if (lastStatus != null) {
            for (int i = 0; i < completeFeed.getSize(); i++) {
                if (lastStatus.equals(completeFeed.getStatusAt(i))) {
                    statusIndex = i + 1;
                }
            }
        }

        return statusIndex;
    }

    private Feed getDummyFeed() {
        return new Feed(Arrays.asList(status1, status2, status3, status4, status5, status6, status7, status8, status9));
    }
}
