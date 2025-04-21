package lk.ijse.supermarketfx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private final String URL = "jdbc:mysql://localhost:3306/supermarketfx";
    private final String USER = "root";
    private final String PASSWORD = "mysql";

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(URL,USER,PASSWORD);
    }

    public static DBConnection getInstance() throws SQLException {
//        if (dbConnection == null) {
//            dbConnection = new DBConnection();
//        }
//        return dbConnection;

        return dbConnection == null ? dbConnection = new DBConnection() : dbConnection; // short version
    }

    public Connection getConnection() {
        return connection;
    }
}
