package edu.byu.cs.tweeter.model.net;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.BuildConfig;
import model.domain.AuthToken;
import model.domain.Feed;
import model.domain.Status;
import model.domain.Story;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.FeedRequest;
import model.service.request.FollowStateRequest;
import model.service.request.FollowUserRequest;
import model.service.request.FollowerRequest;
import model.service.request.FollowingRequest;
import model.service.request.LoginRequest;
import model.service.request.LogoutRequest;
import model.service.request.NewTweetRequest;
import model.service.request.NumFollowersFollowingRequest;
import model.service.request.SignupRequest;
import model.service.request.StoryRequest;
import model.service.request.UnfollowUserRequest;
import model.service.request.UserByAliasRequest;
import model.service.response.FeedResponse;
import model.service.response.FollowStateResponse;
import model.service.response.FollowUserResponse;
import model.service.response.FollowerResponse;
import model.service.response.FollowingResponse;
import model.service.response.LoginResponse;
import model.service.response.LogoutResponse;
import model.service.response.NewTweetResponse;
import model.service.response.NumFollowersFollowingResponse;
import model.service.response.SignupResponse;
import model.service.response.StoryResponse;
import model.service.response.UnfollowUserResponse;
import model.service.response.UserByAliasResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    private static Map<User, List<User>> followeesByFollower;
    private static Map<User, List<User>> followersByFollowee;
    private static Map<User, Feed> feedByUser;
    private static Map<User, Story> storyByUser;

    private static final String SERVER_URL = "https://dtdaxakzgb.execute-api.us-west-2.amazonaws.com/dev";
    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

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

    private final Status status1 = new Status("Hello @GiovannaGiles I would love it if you checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user2), Arrays.asList("https://www.google.com"), user1);
    private final Status status2 = new Status("Hello @DeeDempsey I would love it if you checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user3), Arrays.asList("https://www.google.com"), user2);
    private final Status status3 = new Status("Hello @FranFranklin I would love it if you and @JustinJones checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user4), Arrays.asList("https://www.google.com"), user3);
    private final Status status4 = new Status("Hello @GiovannaGiles I would love it if you checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user2), Arrays.asList("https://www.google.com"), user4);
    private final Status status5 = new Status("Hello @DeeDempsey I would love it if you checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user3), Arrays.asList("https://www.google.com"), user5);
    private final Status status6 = new Status("Hello @FranFranklin I would love it if you and @JustinJones checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user4), Arrays.asList("https://www.google.com"), user6);
    private final Status status7 = new Status("Hello @GiovannaGiles I would love it if you checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user2), Arrays.asList("https://www.google.com"), user7);
    private final Status status8 = new Status("Hello @DeeDempsey I would love it if you checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user3), Arrays.asList("https://www.google.com"), user8);
    private final Status status9 = new Status("Hello @FranFranklin I would love it if you and @JustinJones checked this link out: https://www.google.com", LocalDateTime.now(), Arrays.asList(user4), Arrays.asList("https://www.google.com"), user9);

    private boolean isFollowing = true;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of Login-Related services...
     */

    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
//        User user = new User("Test", "User",
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
//        return new LoginResponse(user, new AuthToken());

        LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of Logout-Related services...
     */

    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException, TweeterRemoteException {
        //return new LogoutResponse();

        LogoutResponse response = clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of Signup-Related services...
     */

    public SignupResponse signup(SignupRequest request, String urlPath) throws IOException, TweeterRemoteException {
//        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(),
//                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
//        return new SignupResponse(user, new AuthToken());

        if (request.getProfilePic() != null) request.profilePicToString();
        SignupResponse response = clientCommunicator.doPost(urlPath, request, null, SignupResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of NewTweet-Related services...
     */

    public NewTweetResponse postTweet(NewTweetRequest request, String urlPath) throws IOException, TweeterRemoteException {
        //return new NewTweetResponse();

        NewTweetResponse response = clientCommunicator.doPost(urlPath, request, null, NewTweetResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of FollowState-Related services...
     */

    public FollowStateResponse followState(FollowStateRequest request, String urlPath) throws IOException, TweeterRemoteException {
        //return new FollowStateResponse(false);

        FollowStateResponse response = clientCommunicator.doPost(urlPath, request, null, FollowStateResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of UnfollowUser-Related services...
     */

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        //return new UnfollowUserResponse();

        UnfollowUserResponse response = clientCommunicator.doPost(urlPath, request, null, UnfollowUserResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of FollowUser-Related services...
     */

    public FollowUserResponse followUser(FollowUserRequest request, String urlPath) throws IOException, TweeterRemoteException{
        //return new FollowUserResponse();

        FollowUserResponse response = clientCommunicator.doPost(urlPath, request, null, FollowUserResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of GetNumFollowers-Related services...
     */

    public NumFollowersFollowingResponse
    getNumFollowersFollowing(NumFollowersFollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        //return new NumFollowersFollowingResponse(getDummyFollowers().size(), getDummyFollowees().size());

        NumFollowersFollowingResponse response = clientCommunicator.doPost(urlPath, request, null, NumFollowersFollowingResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of GetUserByAlias-Related services...
     */

    public UserByAliasResponse getUserByAlias(UserByAliasRequest request, String urlPath) throws IOException, TweeterRemoteException{
//        User user = new User("Fake", "User",
//                             "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
//        return new UserByAliasResponse(user);

        UserByAliasResponse response = clientCommunicator.doPost(urlPath, request, null, UserByAliasResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of Story-Related services...
     */

    public StoryResponse getStory(StoryRequest request, String urlPath) throws IOException, TweeterRemoteException {
//        if (BuildConfig.DEBUG) {
//            if (request.getLimit() < 0) {
//                throw new AssertionError();
//            }
//
//            if (request.getUser() == null) {
//                throw new AssertionError();
//            }
//        }
//
//        Story completeStory = getDummyStory();
//        List<Status> responseStatuses = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if (request.getLimit() > 0) {
//            if (completeStory != null) {
//                int statusIndex = getStoryStartingIndex(request.getLastStatus(), completeStory);
//
//                for (int limitCounter = 0; statusIndex < completeStory.getSize() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
//                    responseStatuses.add(completeStory.getStatusAt(statusIndex));
//                }
//
//                hasMorePages = statusIndex < completeStory.getSize();
//            }
//        }
//
//        return new StoryResponse(new Story(responseStatuses), hasMorePages);

        StoryResponse response = clientCommunicator.doPost(urlPath, request, null, StoryResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    private int getStoryStartingIndex(Status lastStatus, Story completeStory) {
        int statusIndex = 0;

        if (lastStatus != null) {
            for (int i = 0; i < completeStory.getSize(); i++) {
                if (lastStatus.equals(completeStory.getStatusAt(i))) {
                    statusIndex = i + 1;
                }
            }
        }

        return statusIndex;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of Feed-Related services...
     */

    public FeedResponse getFeed(FeedRequest request, String urlPath) throws IOException, TweeterRemoteException {

//        if (BuildConfig.DEBUG) {
//            if (request.getLimit() < 0) {
//                throw new AssertionError();
//            }
//
//            if (request.getUser() == null) {
//                throw new AssertionError();
//            }
//        }
//
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

        FeedResponse response = clientCommunicator.doPost(urlPath, request, null, FeedResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of Follower-Related services...
     */

    public FollowerResponse getFollowers(FollowerRequest request, String urlPath) throws IOException, TweeterRemoteException {

        // Used in place of assert statements because Android does not support them
//        if(BuildConfig.DEBUG) {
//            if(request.getLimit() < 0) {
//                throw new AssertionError();
//            }
//
//            if(request.getFollowee() == null) {
//                throw new AssertionError();
//            }
//        }
//
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

        FollowerResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerResponse.class);
        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
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

    private List<User> getDummyFollowers() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user20, user19, user18, user17, user16, user15, user14,
                user13, user12, user11);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Beginning of Following-Related services...
     */

    public FollowingResponse getFollowees(FollowingRequest request, String urlPath) throws IOException, TweeterRemoteException {

        // Used in place of assert statements because Android does not support them
//        if(BuildConfig.DEBUG) {
//            if(request.getLimit() < 0) {
//                throw new AssertionError();
//            }
//
//            if(request.getFollower() == null) {
//                throw new AssertionError();
//            }
//        }
//
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

        //TODO: new lambda functionality
        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

        if (response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

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
                }
            }
        }

        return followeesIndex;
    }

    private List<User> getDummyFollowees() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }

    private Feed getDummyFeed() {
        return new Feed(Arrays.asList(status1, status2, status3, status4, status5, status6, status7, status8, status9));
    }

    private Story getDummyStory() {
        return new Story(Arrays.asList(status9, status8, status7, status6, status5, status4, status3, status2, status1));
    }

    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFollowGenerator() {
        return FollowGenerator.getInstance();
    }
}
