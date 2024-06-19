package com.example.stationdatacontroller.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean(name = "DB1")
    public JdbcTemplate jdbcTemplate1(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:30011/stationdb");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        DataSource dataSource = new HikariDataSource(hikariConfig);
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "DB2")
    public JdbcTemplate jdbcTemplate2(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:30012/stationdb");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        DataSource dataSource = new HikariDataSource(hikariConfig);
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "DB3")
    public JdbcTemplate jdbcTemplate3(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:30013/stationdb");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        DataSource dataSource = new HikariDataSource(hikariConfig);
        return new JdbcTemplate(dataSource);
    }
}
