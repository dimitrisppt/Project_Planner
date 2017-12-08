package armadillo.models;

import static org.junit.Assert.*;

import armadillo.controllers.CreateDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    @Before
    public void createDatabase() throws SQLException, ClassNotFoundException {
        CreateDatabase.createDatabase("jdbc:sqlite:build/resources/test/tasks.db");
    }

    @After
    public void deleteDatabase() throws SQLException, ClassNotFoundException {
        File file = new File("build/resources/test/tasks.db");
        file.delete();
    }

    @Test
    public void testConstructorDoesNotThrowException() {
        new Database();
    }

    @Test
    public void testURLConstructorDoesNotThrowException() {
        new Database(Database.MAIN_URL);
    }

    @Test
    public void testGetConnectionWorks() throws SQLException, ClassNotFoundException{
        Connection conn = new Database("jdbc:sqlite:build/resources/test/tasks.db").getConnection();
        assertNotNull(conn);
        conn.close();
    }

    @Test
    public void testExecuteStatementWorksWithValidStatement() throws SQLException, ClassNotFoundException {
        new Database("jdbc:sqlite:build/resources/test/tasks.db").executeStatement("SELECT * FROM resources");
    }

    @Test(expected = SQLException.class)
    public void testExecuteStatementWithInvalidStatement() throws SQLException, ClassNotFoundException {
        new Database("jdbc:sqlite:build/resources/test/tasks.db").executeStatement("DOGSAREGODS");
    }

    @Test
    public void testExecuteInsertStatementWithValidStatement() throws SQLException, ClassNotFoundException {
        int id = new Database("jdbc:sqlite:build/resources/test/tasks.db").executeInsertStatement("INSERT INTO resources (name) VALUES (\"logs\")");
        new Database("jdbc:sqlite:build/resources/test/tasks.db").executeStatement("DELETE FROM resources WHERE ID=" + id);
    }

    @Test(expected = SQLException.class)
    public void testExecuteInsertStatementWithInvalidStatement() throws SQLException, ClassNotFoundException {
        new Database("jdbc:sqlite:build/resources/test/tasks.db").executeInsertStatement("DOGSAREGODS");
    }

    @Test(expected = SQLException.class)
    public void testExecuteQueryWithInvalidStatement() throws SQLException, ClassNotFoundException {
        new Database("jdbc:sqlite:build/resources/test/tasks.db").executeQuery("DOGSAREGODS");
    }

    @Test
    public void testExecuteQueryWithValidStatement() throws SQLException, ClassNotFoundException {
        int id = new Database("jdbc:sqlite:build/resources/test/tasks.db").executeInsertStatement("INSERT INTO resources (name) VALUES (\"logs\")");
        CachedRowSet crs = new Database("jdbc:sqlite:build/resources/test/tasks.db").executeQuery("SELECT * FROM resources WHERE id=" + id);
        crs.next();
        assertEquals(id, crs.getInt("ID"));
        assertEquals("logs", crs.getString("name"));
        new Database("jdbc:sqlite:build/resources/test/tasks.db").executeStatement("DELETE FROM resources WHERE ID=" + id);
    }
}
