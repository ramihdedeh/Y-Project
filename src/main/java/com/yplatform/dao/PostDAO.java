package com.yplatform.dao;
import com.yplatform.model.Post;

import java.util.List;

public interface PostDAO {
    void createPost(Post post);

    List<Post> getAllPosts();

    Post getPostById(Long postId);

    List<Post> getPostsByUserId(Long userId);

    void updatePost(Post post);

    void deletePost(Long postId);

    List<Post> getPostsOfInterest(Long user_id);
}
