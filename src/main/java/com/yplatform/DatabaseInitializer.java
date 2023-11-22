package com.yplatform;

//import com.yplatform.model.Post;
//import com.yplatform.model.User;
import com.yplatform.repository.SQLiteDBManager;
//import com.yplatform.dao.PostDAO;
//import com.yplatform.dao.PostDAOImpl;

//import java.util.List;
//import java.util.logging.Logger;
//import java.util.logging.Level;
//import java.util.Set;

public class DatabaseInitializer {
    //private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());
    public static void main(String[] args) {
        SQLiteDBManager dbManager = new SQLiteDBManager();
        dbManager.initializeDatabase();
        /*
        // Example: Creating and persisting a user
        User user = new User();
        long Id = 1;
        user.setId(Id);
        user.setUsername("user1");
        user.setPassword("Password123@");

        // Validate the user before persisting (you can use your validator here)

        // Persist user
        //dbManager.createUser(user);

        // Example: Fetching users
        //List<User> users = dbManager.getAllUsers();
       // logger.info("Users: " + users);

        // Example: Creating and persisting a post associated with a user
        Post post = new Post();
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