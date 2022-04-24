package com.carter.goodit.group;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;


public class Group {

    private String Name;
    private Timestamp Date;
    private int NumUsers;
    private int NumPosts;

    public Group(String name, Timestamp date, int numUsers, int numPosts) {
        Name = name;
        Date = date;
        NumUsers = numUsers;
        NumPosts = numPosts;
    }

    public Group(String name) {
        Name = name;
        Date = null;
        NumUsers = 0;
        NumPosts = 0;
    }

    public String getName() {
        return Name;
    }

    public Timestamp getDate() {
        return Date;
    }

    public int getNumUsers() {
        return NumUsers;
    }

    public int getNumPosts() {
        return NumPosts;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public void setNumUsers(int numUsers) {
        NumUsers = numUsers;
    }

    public void setNumPosts(int numPosts) {
        NumPosts = numPosts;
    }

    @Override
    public String toString() {
        return "Group{" +
                "Name='" + Name + '\'' +
                ", Date='" + Date + '\'' +
                ", NumUsers=" + NumUsers +
                ", NumPosts=" + NumPosts +
                '}';
    }
}
