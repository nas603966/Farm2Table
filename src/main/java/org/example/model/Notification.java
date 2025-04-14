package org.example.model;

public class Notification {
    private String id;
    private String userId;
    private String message;

    public Notification() {}

    public Notification(String id, String userId, String message) {
        this.id = id;
        this.userId = userId;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
