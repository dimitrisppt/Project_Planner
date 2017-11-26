package armadillo.models;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class Database {
    public final static String URL = "jdbc:sqlite:build/resources/main/tasks.db";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(URL);
    }

    public static void executeStatement(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        sqlStatement.execute(sql);
        conn.close();
    }

    public static int executeInsertStatement(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        sqlStatement.execute(sql);
        ResultSet rs = sqlStatement.getGeneratedKeys();
        int returnValue = rs.getInt(1);
        conn.close();
        return returnValue;
    }

    public static CachedRowSet executeQuery(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        ResultSet rs = sqlStatement.executeQuery(sql);
        CachedRowSet crs = new CachedRowSetImpl();
        crs.populate(rs);
        conn.close();
        return crs;
    }
}
