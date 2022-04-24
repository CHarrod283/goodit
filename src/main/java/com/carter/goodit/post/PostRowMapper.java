package com.carter.goodit.post;

import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
                rs.getInt("ID"),
                rs.getString("Title"),
                rs.getString("Content"),
                rs.getTimestamp("Date"),
                rs.getInt("Likes"),
                rs.getInt("Dislikes")
        );
    }
}
