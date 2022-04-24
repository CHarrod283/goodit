package com.carter.goodit.post;

import java.sql.Timestamp;

public class Post {
    int ID;
    String Title;
    String Content;
    Timestamp Date;
    int Likes;
    int Dislikes;

    String Username; // optional
    String GroupName; // optional

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        this.Likes = likes;
    }

    public int getDislikes() {
        return Dislikes;
    }

    public void setDislikes(int dislikes) {
        this.Dislikes = dislikes;
    }

    public Post(int ID, String title, String content, Timestamp date, int likes, int dislikes) {
        this.ID = ID;
        this.Title = title;
        this.Content = content;
        Date = date;
        this.Likes = likes;
        this.Dislikes = dislikes;
    }
}
