package com.yplatform.dao;
import com.yplatform.model.Post;
import com.yplatform.repository.SQLiteDBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAOImpl implements PostDAO{
    private static final String DATABASE_URL = SQLiteDBManager.getJdbcUrl();
    @Override
    public void createPost(Post post) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO posts (title, content, user_id) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setLong(3, post.getUserId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM posts")) {

            while (resultSet.next()) {
                Post post = createPostFromResultSet(resultSet);
                posts.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    @Override
    public Post getPostById(Long postId) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM posts WHERE post_id = ?")) {

            preparedStatement.setLong(1, postId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createPostFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        List<Post> posts = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM posts WHERE user_id = ?")) {

            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = createPostFromResultSet(resultSet);
                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    @Override
    public void updatePost(Post post) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE posts SET title = ?, content = ?, user_id = ? WHERE post_id = ?")) {

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setLong(3, post.getUserId());
            preparedStatement.setLong(4, post.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePost(Long postId) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM posts WHERE post_id = ?")) {

            preparedStatement.setLong(1, postId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Post createPostFromResultSet(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getLong("post_id"),
                resultSet.getString("title"),
                resultSet.getString("content"),
                resultSet.getLong("user_id")
        );
    }
}
