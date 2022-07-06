package com.mycompany.myapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserkDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertIntoUserk(String email,String firstname,String lastname,Long tenant_id,String username, Long userid){
        var query ="insert into userk(email,firstname,lastname,tenant_id,username,userid) values (?,?,?,?,?,?)";
        int update = this.jdbcTemplate.update(query,email,firstname,lastname,tenant_id,username,userid);
        System.out.println(update + " rows affected");
    }
}
