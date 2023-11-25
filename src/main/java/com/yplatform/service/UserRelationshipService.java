package com.yplatform.service;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yplatform.dao.UserRelationshipDAOImpl;
import com.yplatform.model.User;

public class UserRelationshipService {

    private static UserRelationshipDAOImpl userRelationshipDAO = new UserRelationshipDAOImpl();

    public static boolean addFollower(Long userId, Long followedUserId) {
        try {
            userRelationshipDAO.addFollower(userId, followedUserId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeFollower(Long userId, Long followedUserId) {
        try {
            userRelationshipDAO.removeFollower(userId, followedUserId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to convert a list of users to JSON
    public static String convertUsersListToJson(List<User> users) {
        // Implement JSON conversion logic (using a library like Jackson, Gson, etc.)
        // This is a simplified example, you may need to adjust based on your JSON library
        // Here, assuming you have a UserDto class to represent users in a simplified way
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getUsername()))
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(userDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle JSON conversion error
            return "{}";
        }
    }

    // UserDto class for simplifying user representation in JSON
    public static class UserDto {
        private String username;

        public UserDto(String username) {
            this.username = username;
        }

        public String getUsername() {
            return this.username;
        }
    }
}