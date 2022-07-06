package com.mycompany.myapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TenantDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertIntoTenant(Long id, String name){
        var query = "insert into tenant(id,name) values(?,?)";
        int update = this.jdbcTemplate.update(query,id,name);
        System.out.println(update + " rows affected");
    }
}
