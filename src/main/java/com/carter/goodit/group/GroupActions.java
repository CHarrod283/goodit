package com.carter.goodit.group;

import com.carter.goodit.post.Post;

import java.util.List;

public interface GroupActions {

    void createGroup(String name, String username);

    List<Group> getAllGroups();

    List<Group> searchGroups(String name);

    List<Group> getGroupsByUser(String name);

    Group getGroupByName(String name);
    List<Post> getPostsByGroupNewest(Group group);
    List<Post> getPostsByGroupHighestRated(Group group);

    void joinGroup(String name, String username);
}
