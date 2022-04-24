package com.carter.goodit.group;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Group(
                rs.getString("Name"),
                rs.getTimestamp("Date"),
                rs.getInt("NumUsers"),
                rs.getInt("NumPosts")
        );
    }
}
