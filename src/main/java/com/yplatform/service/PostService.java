package com.yplatform.service;

import java.util.List;
import java.util.stream.Collectors;

import java.sql.Timestamp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yplatform.dao.PostDAOImpl;
import com.yplatform.model.Post;
import com.yplatform.dao.UserDAOImpl;

public class PostService {
    private static PostDAOImpl postDAO = new PostDAOImpl();
    private static UserDAOImpl userDAO = new UserDAOImpl();
    public static boolean addPost(Post post) {
        if (isValidPost(post)) {
            postDAO.createPost(post);
            return true;
        }
        return false;
    }

    public static boolean updatePost(Post post) {
        if (isValidPost(post) && post.getId() != null) {
            postDAO.updatePost(post);
            return true;
        }
        return false;
    }

    public static boolean deletePost(Long postId) {
        if (postId != null) {
            try {
                postDAO.deletePost(postId);
                return postDAO.getPostById(postId) == null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean isValidPost(Post post) {
        return post != null &&
               //post.getTitle() != null && !post.getTitle().isEmpty() &&
               post.getContent() != null && !post.getContent().isEmpty() &&
               post.getUserId() != null && post.getPostDate() != null;
    }
    // Helper method to convert a list of posts to JSON
    public static String convertPostsListToJson(List<Post> posts) {
        // Implement JSON conversion logic (using a library like Jackson, Gson, etc.)
        // This is a simplified example, you may need to adjust based on your JSON library
        // Here, assuming you have a PostDto class to represent posts in a simplified way
        List<PostDto> postDtos = posts.stream()
                .map(post -> new PostDto(/*post.getTitle(),*/ post.getContent(), post.getPostDate(), userDAO.getUsernameById(post.getUserId())))
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(postDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle JSON conversion error
            return "{}";
        }
    }

    // PostDto class for simplifying post representation in JSON
    public static class PostDto {
        //private String title;
        private String content;
        private Timestamp date;
        private String author;

        public PostDto(/*String title,*/ String content, Timestamp date, String author) {
            //this.title = title;
            this.content = content;
            this.date = date;
            this.author = author;
        }
        //public String getTitle(){
        //    return this.title;
        //}
        public String getContent(){
            return this.content;
        }
        public Timestamp getDate(){
            return this.date;
        }
        public String getAuthor(){
            return this.author;
        }
    }
}