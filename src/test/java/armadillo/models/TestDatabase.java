package armadillo.models;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabase {
    @Test
    public void testConstructorDoesNotThrowException() {
        new Database();
    }

    @Test
    public void testGetConnectionWorks() throws SQLException, ClassNotFoundException{
        Connection conn = new Database().getConnection();
        assertNotNull(conn);
        conn.close();
    }

    @Test
    public void testExecuteStatementWorksWithValidStatement() throws SQLException, ClassNotFoundException {
        new Database().executeStatement("SELECT * FROM resources");
    }

    @Test(expected = SQLException.class)
    public void testExecuteStatementWithInvalidStatement() throws SQLException, ClassNotFoundException {
        new Database().executeStatement("DOGSAREGODS");
    }

    @Test
    public void testExecuteInsertStatementWithValidStatement() throws SQLException, ClassNotFoundException {
        int id = new Database().executeInsertStatement("INSERT INTO resources (name) VALUES (\"logs\")");
        new Database().executeStatement("DELETE FROM resources WHERE ID=" + id);
    }

    @Test(expected = SQLException.class)
    public void testExecuteInsertStatementWithInvalidStatement() throws SQLException, ClassNotFoundException {
        new Database().executeInsertStatement("DOGSAREGODS");
    }

    @Test(expected = SQLException.class)
    public void testExecuteQueryWithInvalidStatement() throws SQLException, ClassNotFoundException {
        new Database().executeQuery("DOGSAREGODS");
    }

    @Test
    public void testExecuteQueryWithValidStatement() throws SQLException, ClassNotFoundException {
        int id = new Database().executeInsertStatement("INSERT INTO resources (name) VALUES (\"logs\")");
        CachedRowSet crs = new Database().executeQuery("SELECT * FROM resources WHERE id=" + id);
        crs.next();
        assertEquals(id, crs.getInt("ID"));
        assertEquals("logs", crs.getString("name"));
        new Database().executeStatement("DELETE FROM resources WHERE ID=" + id);
    }
}
