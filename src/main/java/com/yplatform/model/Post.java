package com.yplatform.model;

public class Post {

    private Long id;
    private String title;
    private String content;
    private Long user_id;

    // Constructors

    public Post() {
    }

    public Post(String title, String content, Long user_id) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
    }
    public Post(Long id, String title, String content, Long user_id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
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
}