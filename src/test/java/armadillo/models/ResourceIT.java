package armadillo.models;

import armadillo.controllers.CreateDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class ResourceIT {

    private Database database = new Database("jdbc:sqlite:build/resources/test/tasks.db");

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
    public void testResourceConstructorWithValidNamesThenGettersAndDeleteAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        Resource r1 = new Resource("Logs", database);
        Resource r2 = new Resource("Dogs", database);
        int r1Id = r1.getId();
        int r2Id = r2.getId();
        assertEquals("Logs", r1.getName());
        assertEquals("Dogs", r2.getName());
        assertTrue(r1.exists());
        assertTrue(r2.exists());
        assertTrue(Resource.exists(r1Id, database));
        assertTrue(Resource.exists(r2Id, database));
        Resource.delete(r1Id, database);
        r2.delete();
        assertFalse(r1.exists());
        assertFalse(Resource.exists(r1Id, database));
        assertFalse(r2.exists());
        assertFalse(Resource.exists(r2Id, database));
    }

    @Test
    public void testGetAllResources() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        TreeSet<Resource> resources = Resource.getAllResources(database);
        resources.add(new Resource("Dogs", database));
        resources.add(new Resource("Logs", database));
        resources.add(new Resource("Cats", database));
        assertEquals(resources, Resource.getAllResources(database));
        for (Resource r : resources) {
            r.delete();
        }
    }

    @Test
    public void testSetName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = new Resource("Logs", database);
        r.setName("Dogs");
        assertEquals("Dogs", r.getName());
        Resource.delete(r.getId(), database);
    }

    @Test
    public void testGetResourceByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r1 = new Resource("Logs", database);
        Resource r2 = Resource.getResourceByID(r1.getId(), database);
        assertTrue(r1.equals(r2));
        Resource.delete(r1.getId(), database);
    }

    @Test
    public void testAddTaskAndGetAllTasks() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r1 = new Resource("Logs", database);
        Task t1 = new Task("Task One", "Long desc", 10200201, database);
        Task t2 = new Task("Task Two", "Desc", 10202144, database);
        r1.addTask(t1);
        r1.addTask(t2);
        TreeSet<Task> tasks = new TreeSet<>();
        tasks.add(t1);
        tasks.add(t2);
        assertEquals(tasks, r1.getTasks());
        t1.delete();
        t2.delete();
        r1.delete();
    }
}
