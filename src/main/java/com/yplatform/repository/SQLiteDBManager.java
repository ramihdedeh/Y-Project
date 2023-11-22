package com.yplatform.repository;
import java.sql.*;
public class SQLiteDBManager {
    private static final String JDBC_URL = "jdbc:sqlite:./data/posts.db";
    public static String getJdbcUrl() {
        return JDBC_URL;
    }
    public void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             Statement statement = connection.createStatement()) {

            // Create users table
            statement.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "email TEXT UNIQUE NOT NULL,"
                    + "first_name TEXT,"
                    + "last_name TEXT,"
                    + "date_of_birth DATE,"
                    + "username TEXT NOT NULL,"
                    + "password TEXT NOT NULL)");

            // Create posts table
            statement.execute("CREATE TABLE IF NOT EXISTS posts ("
                    + "post_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "content TEXT NOT NULL,"
                    + "post_date DATE,"
                    + "user_id INTEGER NOT NULL,"
                    + "FOREIGN KEY (user_id) REFERENCES users(user_id))");
            
            //Create a user relationship table
            statement.execute("CREATE TABLE IF NOT EXISTS user_relationships ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"   
                + "followed_user_id INTEGER NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES users(id),"
                + "FOREIGN KEY (followed_user_id) REFERENCES users(id),"
                + "UNIQUE (user_id, followed_user_id))");
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
