package armadillo.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * This tests the Test Database created in the createTestDatabase task in build.gradle
 *
 * @author Robert Greener
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTestDatabase {
    @Test
    public void aDatabaseShouldExist() {
        String url = "jdbc:sqlite:build/resources/test/test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            assertNotEquals(null, conn);
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            assertEquals(null, e);
        }
    }

    @Test
    public void bDatabaseShouldContainOriginalValues() {
        String url = "jdbc:sqlite:build/resources/test/test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            assertNotEquals(null, conn);
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from names");
            Set<String> expectedResults = new HashSet<String>();
            expectedResults.add("Robert");
            expectedResults.add("Tom");
            Set<String> actualResults = new HashSet<>();
            while(rs.next()) {
                actualResults.add(rs.getString("name"));
            }
            assertEquals(expectedResults,actualResults);
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            assertEquals(null, e);
        }
    }

    @Test
    public void cDatabaseShouldNotContainUnexpectedValues() {
        String url = "jdbc:sqlite:build/resources/test/test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            assertNotEquals(null, conn);
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from names");
            Set<String> expectedResults = new HashSet<String>();
            expectedResults.add("Robert");
            expectedResults.add("Tom");
            expectedResults.add("Juliet");
            Set<String> actualResults = new HashSet<>();
            while(rs.next()) {
                actualResults.add(rs.getString("name"));
            }
            assertNotEquals(actualResults, expectedResults);
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            assertEquals(null, e);
        }
    }

    @Test
    public void dDatabaseShouldBeAbleToBeAddedTo() {
        String url = "jdbc:sqlite:build/resources/test/test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            assertNotEquals(null, conn);
            Statement statement = conn.createStatement();
            statement.execute("insert into names values ('Juliet')");
            ResultSet rs = statement.executeQuery("select * from names");
            Set<String> expectedResults = new HashSet<String>();
            expectedResults.add("Robert");
            expectedResults.add("Tom");
            expectedResults.add("Juliet");
            Set<String> actualResults = new HashSet<>();
            while(rs.next()) {
                actualResults.add(rs.getString("name"));
            }
            assertEquals(expectedResults, actualResults);
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            assertEquals(null, e);
        }
    }

    @Test
    public void eEatabaseShouldStillContainNewValues() {
        String url = "jdbc:sqlite:build/resources/test/test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            assertNotEquals(null, conn);
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from names");
            Set<String> expectedResults = new HashSet<String>();
            expectedResults.add("Robert");
            expectedResults.add("Tom");
            expectedResults.add("Juliet");
            Set<String> actualResults = new HashSet<>();
            while(rs.next()) {
                actualResults.add(rs.getString("name"));
            }
            assertEquals(expectedResults, actualResults);
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            assertEquals(null, e);
        }
    }
}
