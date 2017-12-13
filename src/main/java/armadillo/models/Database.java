package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

/**
 * A helper class for accessing the database
 */
public class Database {
    /**
     * JDBC url of the task database
     */
    public final static String MAIN_URL = "jdbc:sqlite:build/resources/main/tasks.db";

    /**
     * The URL of the database
     */
    private String url;

    /**
     * Creates a database with the default URL
     */
    public Database() {
        url = MAIN_URL;
    }

    /**
     * Creates a database with a URL
     * @param url the JDBC url
     */
    public Database(String url) {
        this.url = url;
    }

    /**
     * Gets the connection to the database
     * @return The connection to the database
     * @throws SQLException If there is an error connecting to the database
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection(url);
        Statement statement = conn.createStatement();
        statement.execute("PRAGMA foreign_keys=ON");
        return conn;
    }

    /**
     * Executes an SQL Insert statement
     * @param sql The SQL statement
     * @return The ID of the inserted element
     * @throws SQLException If the SQL Statement is not valid, or a connection cannot be made to the database
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public int executeInsertStatement(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        sqlStatement.execute(sql);
        ResultSet rs = sqlStatement.getGeneratedKeys();
        int returnValue = rs.getInt(1);
        conn.close();
        return returnValue;
    }

    /**
     * Executes an SQL query
     * @param sql The SQL query
     * @return The result of the query
     * @throws SQLException If the SQL Statement is not valid, or a connection cannot be made to the database
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public CachedRowSet executeQuery(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        ResultSet rs = sqlStatement.executeQuery(sql);
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(rs);
        conn.close();
        return crs;
    }

    /**
     * Executes an SQL statement
     * @param sql The SQL statement
     * @throws SQLException If the SQL Statement is not valid, or a connection cannot be made to the database
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public void executeStatement(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection();
        Statement sqlStatement = conn.createStatement();
        sqlStatement.execute(sql);
        conn.close();
    }
}
