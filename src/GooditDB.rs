use std::error::Error;
use mysql::*;
use mysql::prelude::*;
use serde::Serialize;
use std::sync::Mutex;


#[derive(Debug, PartialEq, Eq, Serialize)]
pub struct User {
    name: String,
    date: String,
    num_posts: u32,
    num_comments: u32,
    num_groups: u32
}

#[derive(Debug, PartialEq, Eq, Serialize)]
pub struct Group {
    name: String,
    date: String,
    num_users: u32,
    num_posts: u32,
}

#[derive(Debug, PartialEq, Eq, Serialize)]
pub struct Post {
    id: u32,
    title: String,
    content: String,
    date: String,
    likes: u32,
    dislikes: u32,
}

#[derive(Debug, PartialEq, Eq, Serialize)]
pub struct Comment {
    id: u32,
    content: String,
    date: String,
    likes: u32,
    dislikes: u32,
}

impl Group {
    pub fn new(group_name: String, conn :&mut PooledConn) -> Result<()> {
        // check if group_name already exists in database

        //create group
        let grp = Group {
            name: group_name,
            date: "".to_string(),
            num_users: 0,
            num_posts: 0
        };
        Group::add_group(grp, conn);
        Ok(())
    }
    fn add_group(grp: Group, conn :&mut PooledConn) -> Result<()> {
        let q = format!("insert into `Group` (Name) values ('{}');", grp.name);
        conn.query_drop(q);
        Ok(())
    }

}

pub fn create_pool() -> Pool {
    let username = "charrod";
    let password = "g3.Y7cXrLUnrE3XF7VUoEyz7"; // rando password, not useful outside of project
    let mut mysql_host = "localhost:3306";
    let database_name = "goodit";
    let url = format!("mysql://{}:{}@{}/{}", username, password, mysql_host, database_name);
    println!("Generating Pool");
    Pool::new(Opts::from_url(&url).expect("Err generating options")).expect("Err Generating Pool, if remote ensure tunnel is running")
}

#[cfg(test)]
mod tests {
    use mysql::*;
    use mysql::prelude::*;
    use nix::libc::creat;
    use crate::GooditDB::{create_group, create_pool, Group};

    #[test]
    fn test_add_group() {
        let test_pool = create_pool();
        let mut test_conn = test_pool.get_conn().expect("err on connection");
        let grp = Group {
            name: "test_group".to_string(),
            date: "".to_string(),
            num_users: 0,
            num_posts: 0
        };
        assert!(Group::add_group(grp, &mut test_conn).is_ok());
        test_conn.query_drop("DELETE FROM `Group` WHERE Name='test_group'");
    }
}