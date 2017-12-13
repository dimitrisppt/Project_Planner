package armadillo.controllers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import java.io.File;
import java.sql.SQLException;

public class CreateDatabaseTest {
    @After
    public void deleteDatabase() throws SQLException, ClassNotFoundException {
        File file = new File("build/resources/test/tasks.db");
        file.delete();
    }

    @Test
    public void testConstructorDoesNotThrowException() {
        CreateDatabase cd = new CreateDatabase();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDatabaseWhenURLIsNotValid() throws SQLException, ClassNotFoundException{
        CreateDatabase.createDatabase("hello");
    }

    @Test
    public void testCreateDatabaseWhenValid() throws SQLException, ClassNotFoundException {
        CreateDatabase.createDatabase("jdbc:sqlite:build/resources/test/tasks.db");
        // Twice to test file.exists
        CreateDatabase.createDatabase("jdbc:sqlite:build/resources/test/tasks.db");
    }
}
