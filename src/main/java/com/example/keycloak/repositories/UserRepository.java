package com.example.keycloak.repositories;

import com.example.keycloak.entity.User;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private static final String DB_URL = "jdbc:h2:mem:keycloak;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static final JdbcConnectionPool connectionPool =
            JdbcConnectionPool.create(DB_URL, DB_USER, DB_PASSWORD);

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE users (
                id VARCHAR(36) PRIMARY KEY,
                first_name VARCHAR(50),
                last_name VARCHAR(50),
                country VARCHAR(50)
            )""";

    private static final String INSERT_USER_SQL = """
            INSERT INTO users (id, first_name, last_name, country) 
            VALUES (?, ?, ?, ?)""";

    private static final String FIND_BY_LAST_NAME_SQL = """
            SELECT id, first_name, last_name, country 
            FROM users 
            WHERE last_name = ?""";

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create table
            stmt.execute(CREATE_TABLE_SQL);

            // Insert test users using prepared statements
            insertTestUser("1", "John", "joe", "USA");
            insertTestUser("2", "Jane", "Smith", "Canada");

        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Failed to initialize H2 database", e);
        }
    }

    private static void insertTestUser(String id, String firstName, String lastName, String country) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER_SQL)) {

            stmt.setString(1, id);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, country);
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Failed to insert test user", e);
            throw new RuntimeException("Failed to insert test user", e);
        }
    }

    public Optional<User> findByLastName(String lastName) {
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_LAST_NAME_SQL)) {

            stmt.setString(1, lastName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    logger.debug("Found user: {}", user);
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Failed to find user by last name: {}", lastName, e);
            throw new RuntimeException("DB query failed for last name: " + lastName, e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String country = rs.getString("country");

        logger.debug("Raw DB values - id: {}, firstName: {}, lastName: {}, country: {}",
                id, firstName, lastName, country);

        return new User(id, firstName, lastName, country);
    }
}
