package com.yplatform.dao;
import com.yplatform.model.Post;
import com.yplatform.repository.SQLiteDBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

public class PostDAOImpl implements PostDAO{
    private static final String DATABASE_URL = SQLiteDBManager.getJdbcUrl();
    @Override
    public void createPost(Post post) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO posts (title, content, user_id, post_date) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setLong(3, post.getUserId());
            preparedStatement.setDate(4, post.getPostDate());

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
                     "UPDATE posts SET title = ?, content = ?, user_id = ?, post_date = ? WHERE post_id = ?")) {

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setLong(3, post.getUserId());
            preparedStatement.setDate(4, post.getPostDate());
            preparedStatement.setLong(5, post.getId());

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
                resultSet.getLong("user_id"),
                resultSet.getDate("post_date")
        );
    }

    @Override
    public List<Post> getPostsOfInterest(Long user_id) {
        List<Post> postsOfInterest = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT p.* FROM posts p " +
                             "JOIN user_relationships r ON p.user_id = r.followed_user_id " +
                             "WHERE r.user_id = ? AND p.post_date >= ? " +
                             "ORDER BY p.date_created DESC")){
            preparedStatement.setLong(1, user_id);
            // Calculate the date 3 days ago
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -3);
            Date threeDaysAgo = calendar.getTime();

            preparedStatement.setDate(2, new java.sql.Date(threeDaysAgo.getTime()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = new Post();
                    post.setId(resultSet.getLong("post_id"));
                    post.setTitle(resultSet.getString("title"));
                    post.setPostDate(resultSet.getDate("post_date"));
                    post.setContent(resultSet.getString("content"));
                    // Set other properties as needed
                    postsOfInterest.add(post);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return postsOfInterest;
    }
}
