package com.carter.goodit.post;

import java.util.List;

public interface PostActions {
    void createPost(String groupName, String userName, String title, String body);

    List<Post> getPosts(String username);

    List<Post> getPostsByGroup(String groupname);

    void likePost(int id, String userName);

    void dislikePost(int id, String userName);
}
