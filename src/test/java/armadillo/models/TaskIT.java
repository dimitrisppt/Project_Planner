package armadillo.models;

import org.junit.Test;

import java.sql.SQLException;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class TaskIT {
    private Database database = new Database();

    @Test
    public void testResourceConstructorWithValidNamesThenGettersAndDeleteAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        Task t1 = new Task("A", "B", 1, database);
        Task t2 = new Task("C", null, 5, 1002L, database);
        int t1Id = t1.getId();
        int t2Id = t2.getId();
        assertEquals("A", t1.getName());
        assertEquals("C", t2.getName());
        assertEquals("B", t1.getDescription());
        assertEquals(null, t2.getDescription());
        assertEquals(1, t1.getEffortEstimate());
        assertEquals(5, t2.getEffortEstimate());
        assertEquals(null, t1.getDateTime());
        assertEquals(new Long(1002L), t2.getDateTime());
        assertTrue(t1.exists());
        assertTrue(t2.exists());
        assertTrue(Task.exists(t1Id, database));
        assertTrue(Task.exists(t2Id, database));
        Task.delete(t1Id, database);
        t2.delete();
        assertFalse(t1.exists());
        assertFalse(Task.exists(t1Id, database));
        assertFalse(t2.exists());
        assertFalse(Resource.exists(t2Id, database));
    }

    @Test
    public void testSetName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, database);
        t.setName("C");
        assertEquals("C", t.getName());
        Task.delete(t.getId(), database);
    }

    @Test
    public void testSetDescription() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, database);
        t.setDescription("C");
        assertEquals("C", t.getDescription());
        Task.delete(t.getId(), database);
    }

    @Test
    public void testSetEffortEstimate() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, database);
        t.setEffortEstimate(5);
        assertEquals(5L, t.getEffortEstimate());
        Task.delete(t.getId(), database);
    }

    @Test
    public void testSetDateTimeToNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, 1002L, database);
        t.setDateTime(null);
        assertEquals(null, t.getDateTime());
        Task.delete(t.getId(), database);
    }

    @Test
    public void testSetDateTimeToNotNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, 1002L, database);
        t.setDateTime(500L);
        assertEquals(new Long(500L), t.getDateTime());
        Task.delete(t.getId(), database);
    }

    @Test
    public void testGetTaskByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t1 = new Task("A", "B", 1, database);
        Task t2 = Task.getTaskByID(t1.getId(), database);
        assertTrue(t1.equals(t2));
        Task.delete(t1.getId(), database);
    }

    @Test
    public void testGetAllTasks() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        TreeSet<Task> tasks = Task.getAllTasks(database);
        tasks.add(new Task("A", "B", 1, database));
        tasks.add(new Task("C", "D", 3, database));
        tasks.add(new Task("E", "F", 5, database));
        assertEquals(tasks, Task.getAllTasks(database));
        for (Task t : tasks) {
            t.delete();
        }
    }

    @Test
    public void testAddPersonAndGetAllPeople() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t1 = new Task("Task One", "Long desc", 10200201, database);
        Person p1 = new Person("Robert", "Greener", database);
        Person p2 = new Person("Robert", "Robertson", database);
        t1.addPerson(p1);
        t1.addPerson(p2);
        TreeSet<Person> people = new TreeSet<>();
        people.add(p1);
        people.add(p2);
        assertEquals(people, t1.getPeople());
        p1.delete();
        p2.delete();
        t1.delete();
    }

    @Test
    public void testAddResourceAndGetAllResources() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t1 = new Task("Task One", "Long desc", 10200201, database);
        Resource r1 = new Resource("Dogs", database);
        Resource r2 = new Resource("Logs", database);
        t1.addResource(r1);
        t1.addResource(r2);
        TreeSet<Resource> resources = new TreeSet<>();
        resources.add(r1);
        resources.add(r2);
        assertEquals(resources, t1.getResources());
        r1.delete();
        r2.delete();
        t1.delete();
    }

    @Test
    public void testAddPrerequisiteTaskAndGetPrerequisiteTasks() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t1 = new Task("Task One", "Long desc", 10200201, database);
        Task t2 = new Task("Task Two", "Desc", 10202144, database);
        t1.addPrerequisiteTask(t2);
        TreeSet<Task> tasks = new TreeSet<>();
        tasks.add(t2);
        assertEquals(tasks, t1.getPrerequisiteTasks());
        t1.delete();
        t2.delete();
    }
}


