package com.yplatform.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.yplatform.model.Post;
import com.yplatform.model.User;

import java.util.List;
import java.util.Set;

public class DatabaseInitializer {

    public static void main(String[] args) {
        // Initialize the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        // Initialize the Validator
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        // Example: Creating and persisting a user
        em.getTransaction().begin();
        User user = new User();
        user.setUsername("user1");
        user.setPassword("Password123@");

        // Validate the user before persisting
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<User> violation : violations) {
                System.out.println("Validation Error: " + violation.getMessage());
            }
            em.getTransaction().rollback();
            return;
        }

        em.persist(user);
        em.getTransaction().commit();

        // Example: Fetching users
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        System.out.println("Users: " + users);

        // Example: Creating and persisting a post associated with a user
        em.getTransaction().begin();
        Post post = new Post();
        post.setTitle("My First Post");
        post.setContent("This is the content of my first post.");
        post.setUser(user);
        em.persist(post);
        em.getTransaction().commit();

        // Example: Fetching posts
        List<Post> posts = em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
        System.out.println("Posts: " + posts);

        // Close the EntityManager and EntityManagerFactory
        em.close();
        emf.close();
    }
}