package com.yplatform.dao;

import com.yplatform.model.User;
import java.util.List;

public interface UserRelationshipDAO {
    void addFollower(Long userId, Long followedUserId);
    void removeFollower(Long userId, Long followedUserId);
    List<User> getFollowers(Long user_id);
}