package com.yplatform;

import com.yplatform.model.Post;
import com.yplatform.model.User;
import com.yplatform.repository.SQLiteDBManager;
import com.yplatform.dao.PostDAO;
import com.yplatform.dao.PostDAOImpl;
import com.yplatform.dao.UserDAOImpl;
import com.yplatform.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Set;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DatabaseInitializer {
    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());
    public static void main(String[] args) {
        SQLiteDBManager dbManager = new SQLiteDBManager();
        dbManager.initializeDatabase();

        // Example: Creating and persisting a user
        User user = new User();
        user.setUsername("user1");
        user.setPassword("Password123@");
        user.setEmail("example1@mail.aub.edu");
        String dateString = "17-12-1999";
         try {
            // Parsing the string to create a java.util.Date
            java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);

            // Directly creating java.sql.Date from java.util.Date
            Date sqlDate = new Date(utilDate.getTime());
             user.setDateOfBirth(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        };
        UserService userService = new UserService();
        UserDAOImpl userDAOImpl = new UserDAOImpl();
        User testUser = new User();
        userService.addUser(user);
        try {
            Optional<User> optionalUser = userDAOImpl.getUserByEmail("maa297@mail.aub.edu");

            if (optionalUser.isPresent()) {
                testUser = optionalUser.get();
                // Now you can use 'testUser' as a valid User object
            } else {
                // Handle the case when the user is not present
                logger.info("User not found for the given email.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(testUser.getDateOfBirth()  + " " + testUser.getPassword() + "\n");
        // Validate the user before persisting (you can use your validator here)

        // Persist user
        //dbManager.createUser(user);

        // Example: Fetching users
        //List<User> users = dbManager.getAllUsers();
       // logger.info("Users: " + users);

        // Example: Creating and persisting a post associated with a user
        /*Post post = new Post();
        post.setTitle("My First Post");
        post.setContent("This is the content of my first post.");
        post.setUserId(user.getId());

        Post post2 = new Post();
        post2.setTitle("My Second Post");
        post2.setContent("This is the content of my second post.");
        post2.setUserId(user.getId());
        // Persist post
        PostDAO postDAO = new PostDAOImpl();

        postDAO.createPost(post);
        postDAO.createPost(post2);

        // Example: Fetching posts
        List<Post> posts = postDAO.getPostsByUserId(user.getId());
        for (Post postx : posts) {
            logger.info("Post Title: " + postx.getTitle());
        }*/
    }
}