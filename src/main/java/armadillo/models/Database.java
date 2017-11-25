package armadillo.models;

import java.sql.*;

public class Database {
    public final String URL = "jdbc:sqlite:build/resources/test/test.db";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(url);
    }

    public static void executeStatement(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        sqlStatement.execute(sql);
        conn.close();
    }

    public static ResultSet executeInsertStatement(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        sqlStatement.execute(sql);
        ResultSet rs = sqlStatement.getGeneratedKeys();
        conn.close();
        return rs;
    }

    public static ResultSet executeQuery(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        ResultSet rs = sqlStatement.executeQuery(sql);
        conn.close();
        return rs;
    }
}
