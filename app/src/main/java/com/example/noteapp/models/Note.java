package com.example.noteapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private String id;
    private String title;
    private String content;
    private Date createdAt;
    private boolean isImportant;

    public Note(String title, String content) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.title = title;
        this.content = content;
        this.createdAt = new Date();
        this.isImportant = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void toggleImportant() {
        this.isImportant = !this.isImportant;
    }

    public String getPreview() {
        if (content.length() <= 30)
            return content;
        return content.substring(0, 30) + "...";
    }

    public String isValid() {
        return title != null && title.isEmpty() && content != null;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm ");
        return title + " (" + sdf.format(createdAt) + ")" + (isImportant ? "UP" : "");
    }

}
