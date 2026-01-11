package data;

import config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbClient {

    private static final Logger logger = LogManager.getLogger(DbClient.class);
    private Connection connection;

    public DbClient() {
        connect();
    }

    public void connect() {
        try {
            String url = Config.getProperty("db.url");
            String username = Config.getProperty("db.username");
            String password = Config.getProperty("db.password");

            connection = DriverManager.getConnection(url, username, password);
            logger.info("Database connection established");
        } catch (SQLException e) {
            logger.error("Failed to connect to database: {}", e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection: {}", e.getMessage());
        }
    }

    public List<Map<String, Object>> executeQuery(String sql) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
            logger.debug("Query executed successfully, {} rows returned", results.size());
        } catch (SQLException e) {
            logger.error("Query execution failed: {}", e.getMessage());
            throw new RuntimeException("Query execution failed", e);
        }
        return results;
    }

    public int executeUpdate(String sql) {
        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            logger.debug("Update executed, {} rows affected", rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            logger.error("Update execution failed: {}", e.getMessage());
            throw new RuntimeException("Update execution failed", e);
        }
    }

    public List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
            logger.debug("Parameterized query executed, {} rows returned", results.size());
        } catch (SQLException e) {
            logger.error("Parameterized query failed: {}", e.getMessage());
            throw new RuntimeException("Query execution failed", e);
        }
        return results;
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
