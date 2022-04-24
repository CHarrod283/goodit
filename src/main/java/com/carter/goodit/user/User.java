package com.carter.goodit.user;

import java.sql.Timestamp;

public class User {
    private String Name;
    private Timestamp Date;
    private int NumPosts;
    private int NumComments;
    private int NumGroups;

    public User(String name, Timestamp date, int numPosts, int numComments, int numGroups) {
        Name = name;
        Date = date;
        NumPosts = numPosts;
        NumComments = numComments;
        NumGroups = numGroups;
    }

    public User(String name) {
        Name = name;
        Date = null;
        NumComments = 0;
        NumPosts = 0;
        NumGroups = 0;
    }

    public String getName() {
        return Name;
    }

    public Timestamp getDate() {
        return Date;
    }

    public int getNumPosts() {
        return NumPosts;
    }

    public int getNumComments() {
        return NumComments;
    }

    public int getNumGroups() {
        return NumGroups;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public void setNumPosts(int numPosts) {
        NumPosts = numPosts;
    }

    public void setNumComments(int numComments) {
        NumComments = numComments;
    }

    public void setNumGroups(int numGroups) {
        NumGroups = numGroups;
    }

    @Override
    public String toString() {
        return "User{" +
                "Name='" + Name + '\'' +
                ", Date=" + Date +
                ", NumPosts=" + NumPosts +
                ", NumComments=" + NumComments +
                ", NumGroups=" + NumGroups +
                '}';
    }
}
