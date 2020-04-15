package com.bskim.inmemory;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * H2Runner
 */
@Slf4j
@Component
public class H2Runner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        try(Connection connection = dataSource.getConnection();){
            String url = connection.getMetaData().getURL();
            String userName = connection.getMetaData().getUserName();
            
            log.info(url);
            log.info(userName);
    
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE USER (ID INTEGER NOT NULL, name VARCHAR(255), PRIMARY KEY (id))";
            statement.executeUpdate(sql);
            connection.close();

        } catch(Exception e){
            log.info("error.........");
        }

        // spring에서 지원하는 jdbcTemplate...
        jdbcTemplate.execute("INSERT INTO USER VALUES (1, 'BOSUNG')");

    }
}