package com.carter.goodit.post;

import com.carter.goodit.group.GroupRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Date;
import java.util.List;

public class PostService implements PostActions{

    JdbcTemplate jdbcTemplate;
    public PostService(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createPost(String groupName, String userName, String title, String body) {
        int ID = groupName.hashCode() + userName.hashCode() + new Date().hashCode();
        String SQL = "insert into `Post` (ID, Title, Content) values (?, ?, ?)";
        jdbcTemplate.query(
                SQL,
                new Object[] {ID, title, body},
                new int[] {Types.INTEGER, Types.VARCHAR, Types.VARCHAR},
                new PostRowMapper()
        );
        SQL = "insert into `Created` (PostID, UserName) VALUES (?, ?)";
        jdbcTemplate.query(
                SQL,
                new Object[] {ID, userName},
                new int[] {Types.INTEGER, Types.VARCHAR},
                new PostRowMapper() // dont think this matters
        );
        SQL = "insert into `For` (PostID, GroupName) VALUES (?, ?)";
        jdbcTemplate.query(
                SQL,
                new Object[] {ID, groupName},
                new int[] {Types.INTEGER, Types.VARCHAR},
                new PostRowMapper() // dont think this matters
        );
        SQL = "update `Group` set NumPosts = NumPosts + 1 where Name = ?";
        jdbcTemplate.query(
                SQL,
                new Object[] {groupName},
                new int[] {Types.VARCHAR},
                new GroupRowMapper() // dont think this matters
        );
    }

    @Override
    public List<Post> getPosts(String username) {
        String SQL = "select * from `Post`, `For`,`Created` " +
                "where ID=`For`.PostID and ID=Created.PostID and GroupName in (" +
                "select Name from `Group`, `In` where Name=`In`.GroupName and `In`.UserName = ?" +
                ")";
        return jdbcTemplate.query(
                SQL,
                new Object[] {username},
                new int[] {Types.VARCHAR},
                (rs, rowNum) -> {
                    Post p = new Post(
                            rs.getInt("ID"),
                            rs.getString("Title"),
                            rs.getString("Content"),
                            rs.getTimestamp("Date"),
                            rs.getInt("Likes"),
                            rs.getInt("Dislikes")
                    );
                    p.setGroupName(rs.getString("GroupName"));
                    p.setUsername(rs.getString("Username"));
                    return p;
                }
        );
    }

    @Override
    public List<Post> getPostsByGroup(String groupname) {
        String SQL = "select * from `Post`, `For`,`Created` " +
                "where ID=`For`.PostID and ID=Created.PostID and GroupName = ?";
        return jdbcTemplate.query(
                SQL,
                new Object[] {groupname},
                new int[] {Types.VARCHAR},
                (rs, rowNum) -> {
                    Post p = new Post(
                            rs.getInt("ID"),
                            rs.getString("Title"),
                            rs.getString("Content"),
                            rs.getTimestamp("Date"),
                            rs.getInt("Likes"),
                            rs.getInt("Dislikes")
                    );
                    p.setGroupName(rs.getString("GroupName"));
                    p.setUsername(rs.getString("Username"));
                    return p;
                }
        );
    }

    @Override
    public void likePost(int id, String userName) {
        String SQL = "select * from `LikesPost` where PostID = ? and UserName = ?";
        List<Integer> ids = jdbcTemplate.query(
                SQL,
                new Object[] {id, userName},
                new int[] {Types.INTEGER, Types.VARCHAR},
                (rs, rowNum) -> {
                    int exists = rs.getInt("Likes");
                    return exists;
                }
        );

        if (ids.size() > 0) {
            // if dislike change to like
            if(ids.get(0) == 0) {
                SQL = "update `LikesPost` set Likes = 1 where PostID = ? and UserName = ?";
                jdbcTemplate.query(
                        SQL,
                        new Object[] {id, userName},
                        new int[] {Types.INTEGER, Types.VARCHAR},
                        new PostRowMapper() // dont think this matters
                );
                SQL = "update `Post` set Likes = Likes + 1, Dislikes = Dislikes - 1 where ID = ?";
                jdbcTemplate.query(
                        SQL,
                        new Object[] {id},
                        new int[] {Types.INTEGER},
                        new PostRowMapper() // dont think this matters
                );
            }
            // if already liked return
            return;
        }



        SQL = "insert into `LikesPost` (PostID, UserName, Likes) values (?, ?, 1)";
        jdbcTemplate.query(
                SQL,
                new Object[] {id, userName},
                new int[] {Types.INTEGER, Types.VARCHAR},
                new PostRowMapper()
        );
        SQL = "update `Post` set Likes = Likes + 1 where ID = ?";
        jdbcTemplate.query(
                SQL,
                new Object[] {id},
                new int[] {Types.INTEGER},
                new PostRowMapper() // dont think this matters
        );
    }

    @Override
    public void dislikePost(int id, String userName) {
        String SQL = "select * from `LikesPost` where PostID = ? and UserName = ?";
        List<Integer> ids = jdbcTemplate.query(
                SQL,
                new Object[] {id, userName},
                new int[] {Types.INTEGER, Types.VARCHAR},
                (rs, rowNum) -> {
                    int exists = rs.getInt("Likes");
                    return exists;
                }
        );

        if (ids.size() > 0) {
            // if dislike change to like
            if(ids.get(0) == 1) {
                SQL = "update `LikesPost` set Likes = 0 where PostID = ? and UserName = ?";
                jdbcTemplate.query(
                        SQL,
                        new Object[] {id, userName},
                        new int[] {Types.INTEGER, Types.VARCHAR},
                        new PostRowMapper() // dont think this matters
                );
                SQL = "update `Post` set Likes = Likes - 1, Dislikes = Dislikes + 1 where ID = ?";
                jdbcTemplate.query(
                        SQL,
                        new Object[] {id},
                        new int[] {Types.INTEGER},
                        new PostRowMapper() // dont think this matters
                );
            }
            // if already liked return
            return;
        }

        SQL = "insert into `LikesPost` (PostID, UserName, Likes) values (?, ?, 0)";
        jdbcTemplate.query(
                SQL,
                new Object[] {id, userName},
                new int[] {Types.INTEGER, Types.VARCHAR},
                new PostRowMapper()
        );
        SQL = "update `Post` set Dislikes = Dislikes + 1 where ID = ?";
        jdbcTemplate.query(
                SQL,
                new Object[] {id},
                new int[] {Types.INTEGER},
                new PostRowMapper() // dont think this matters
        );
    }
}
