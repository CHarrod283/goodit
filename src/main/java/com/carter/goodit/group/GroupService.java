package com.carter.goodit.group;

import com.carter.goodit.post.Post;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

public class GroupService implements GroupActions {

    JdbcTemplate jdbcTemplate;

    public GroupService(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void createGroup(String name, String username) {
        String SQL = "insert into `Group` (Name) values (?)";
        jdbcTemplate.query(
                SQL,
                new Object[] {name},
                new int[] {Types.VARCHAR},
                new GroupRowMapper()
        );
    }

    @Override
    public List<Group> getAllGroups() {
        String SQL = "select * from `Group`";
        return jdbcTemplate.query(
                SQL,
                new GroupRowMapper()
        );
    }

    @Override
    public List<Group> searchGroups(String name) {
        String SQL = "select * from `Group` where Name like \"%" + name + "%\"";
        return jdbcTemplate.query(
                SQL,
                new GroupRowMapper()
        );
    }

    @Override
    public List<Group> getGroupsByUser(String name) {
        String SQL = "select * from `Group`, `In` where Name = `In`.GroupName and UserName = ?";
        return jdbcTemplate.query(
                SQL,
                new Object[] {name}, // inserts name into sql text
                new int[] {Types.VARCHAR},
                (rs, rowNum) -> new Group(
                        rs.getString("Name"),
                        rs.getTimestamp("Date"),
                        rs.getInt("NumUsers"),
                        rs.getInt("NumPosts")
                )
        );
    }

    @Override
    public Group getGroupByName(String name) {
        String SQL = "select * from `Group` where Name = ?";
        List<Group> groups = jdbcTemplate.query(
                SQL,
                new Object[] {name}, // inserts name into sql text
                new int[] {Types.VARCHAR},
                new GroupRowMapper()
        );
        assert groups.size() <= 1; // name may not exist in groups
        if (groups.size() == 0){
            return null;
        }
        return groups.get(0);
    }

    @Override
    public List<Post> getPostsByGroupNewest(Group group) {
        return null;
    }

    @Override
    public List<Post> getPostsByGroupHighestRated(Group group) {
        return null;
    }

    @Override
    public void joinGroup(String name, String username) {
        String SQL = "select * from `In` where GroupName = ? and UserName = ?";
        List<Group> grp = jdbcTemplate.query(
                SQL,
                new Object[]{name, username}, // inserts name into sql text
                new int[]{Types.VARCHAR, Types.VARCHAR},
                (rs, rowNum) -> new Group(
                        rs.getString("GroupName")
                )
        );

        if(grp.size() != 0) {
            return;
        }
        
        SQL = "insert into `In` (GroupName, UserName) values (?, ?)";
        jdbcTemplate.query(
                SQL,
                new Object[] {name, username}, // inserts name into sql text
                new int[] {Types.VARCHAR, Types.VARCHAR},
                new GroupRowMapper()
        );
        SQL = "update `User` set NumGroups = NumGroups + 1 where Name = ?";
        jdbcTemplate.query(
                SQL,
                new Object[] {username},
                new int[] {Types.VARCHAR},
                new GroupRowMapper() // dont think this matters
        );
        SQL = "update `Group` set NumUsers = NumUsers + 1 where Name = ?";
        jdbcTemplate.query(
                SQL,
                new Object[] {name},
                new int[] {Types.VARCHAR},
                new GroupRowMapper() // dont think this matters
        );
    }
}
