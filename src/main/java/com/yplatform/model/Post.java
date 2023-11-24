package com.yplatform.model;
import java.sql.Timestamp;
public class Post {

    private Long id;
    private String title;
    private String content;
    private Long user_id;
    private Timestamp post_date;

    // Constructors

    public Post() {
    }

    public Post(String title, String content, Long user_id, Timestamp post_date) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.post_date = post_date;
    }
    public Post(Long id, String title, String content, Long user_id, Timestamp post_date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.post_date = post_date;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }
    public Timestamp getPostDate(){
        return post_date;
    }
    public void setPostDate(Timestamp post_date) {
        this.post_date = post_date;
    }
}