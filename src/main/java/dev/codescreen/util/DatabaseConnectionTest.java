package dev.codescreen.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void testConnection() {
        try {
            int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM transaction", Integer.class);
            System.out.println("Database connection successful. Number of rows in 'transaction' table: " + count);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}