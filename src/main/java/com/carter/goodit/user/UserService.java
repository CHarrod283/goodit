package com.carter.goodit.user;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;


public class UserService implements UserActions {
    private User user;

    public String getUsername() {
        return user.getName();
    }

    JdbcTemplate jdbcTemplate;

    public UserService(DataSource dataSource) {
        user = null;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean signup(String name) {
        // check if name already in database
        String SQL = "select * from `User` where Name = ?";
        List<User> users = jdbcTemplate.query(
                SQL,
                new Object[] {name}, // inserts name into sql text
                new int[] {Types.VARCHAR},
                new UserRowMapper()
        );
        if (users.size() != 0) {
            return false;
        }
        SQL = "insert into `User` (Name) values ( ? )";
        jdbcTemplate.query(
                SQL,
                new Object[] {name}, // inserts name into sql text
                new int[] {Types.VARCHAR},
                new UserRowMapper()
        );
        return true;
    }

    @Override
    public boolean login(String name) {
        // check that name is in database
        String SQL = "select * from `User` where Name = ?";
        List<User> users = jdbcTemplate.query(
                SQL,
                new Object[] {name}, // inserts name into sql text
                new int[] {Types.VARCHAR},
                new UserRowMapper()
        );
        if (users.size() == 0) {
            return false;
        }
        this.user = users.get(0);
        return true;
    }
}
