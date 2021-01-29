package server.service;

import java.util.ArrayList;
import java.util.List;

import model.domain.User;
import server.dao.FollowingDAO;
import server.dao.UserDAO;

public class FillerService {

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@creedev";

    public static void main(String[] args) {
        fillDatabase();
    }

    public static void fillDatabase() {

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDAO userDAO = new UserDAO();
        FollowingDAO followDAO = new FollowingDAO();

        List<String> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 0; i <= NUM_USERS; i++) {

            String firstName = "Guy";
            String lastName = Integer.toString(i);
            String alias = "@Guy" + i;

            // Note that in this example, a UserDTO only has a firstName and an alias.
            // The url for the profile image can be derived from the alias in this example
//            User user = new User();
//            user.setAlias(alias);
//            user.setFirstName(firstName);
//            user.setLastName(lastName);
//            user.setImageUrl("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
//            user.setPassword("easy123");
//            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
//        if (users.size() > 0) {
//            userDAO.addUserBatch(users);
//        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }

}
