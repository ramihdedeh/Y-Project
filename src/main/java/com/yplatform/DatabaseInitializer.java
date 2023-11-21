package com.yplatform;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.yplatform.model.Post;
import com.yplatform.model.User;
import com.yplatform.repository.SQLiteDBManager;

import java.util.List;
import java.util.Set;

public class DatabaseInitializer {

    public static void main(String[] args) {
        SQLiteDBManager dbManager = new SQLiteDBManager();
        dbManager.initializeDatabase();

        // Example: Creating and persisting a user
        User user = new User();
        user.setUsername("user1");
        user.setPassword("Password123@");

        // Validate the user before persisting (you can use your validator here)

        // Persist user
        dbManager.createUser(user);

        // Example: Fetching users
        List<User> users = dbManager.getAllUsers();
        System.out.println("Users: " + users);

        // Example: Creating and persisting a post associated with a user
        Post post = new Post();
        post.setTitle("My First Post");
        post.setContent("This is the content of my first post.");
        post.setUser(user);

        // Persist post
        dbManager.createPost(post);

        // Example: Fetching posts
        List<Post> posts = dbManager.getAllPosts();
        System.out.println("Posts: " + posts);
    }
}