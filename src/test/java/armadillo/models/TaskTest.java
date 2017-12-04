package armadillo.models;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.TreeSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaskTest {

    @Mock
    Database database;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenNameIsNull() throws SQLException, ClassNotFoundException {
        new Task(null, "Test", 100, database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i =  0; i <= 255; i++) {
            sb.append('a');
        }
        new Task(sb.toString(), "Test", 100, database);
    }

    @Test
    public void testConstructorWhenValidAndDescIsNotNull() throws SQLException, ClassNotFoundException {
        Task t = new Task("Test", "This is a test task", 100, database);
        verify(database).executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"Test\", \"This is a test task\", 100)");
    }

    @Test
    public void testConstructorWhenValidAndDescIsNull() throws SQLException, ClassNotFoundException {
        Task t = new Task("Test", null, 100, database);
        verify(database).executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"Test\", null, 100)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeConstructorWhenNameIsNull() throws SQLException, ClassNotFoundException {
        new Task(null, "Test", 100, 1002L, database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeConstructorWhenNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i =  0; i <= 255; i++) {
            sb.append('a');
        }
        new Task(sb.toString(), "Test", 100, 1002L, database);
    }

    @Test
    public void testDateTimeConstructorWhenValidAndDescIsNotNull() throws SQLException, ClassNotFoundException {
        Task t = new Task("Test", "This is a test task", 100, 1002L, database);
        verify(database).executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate, date_time) VALUES (\"Test\", \"This is a test task\", 100, 1002)");
    }

    @Test
    public void testDateTimeConstructorWhenValidAndDescIsNull() throws SQLException, ClassNotFoundException {
        Task t = new Task("Test", null, 100, 1002L, database);
        verify(database).executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate, date_time) VALUES (\"Test\", null, 100, 1002)");
    }

    @Test
    public void testPackagePrivateConstructorDoesNotThrowException() throws SQLException, ClassNotFoundException {
        new Task(1, database);
    }

    @Test
    public void testGetTaskByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM tasks WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        Task.getTaskByID(1, database);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetTaskByIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM tasks WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Task.getTaskByID(1, database);
    }

    @Test
    public void testGetAllTasksWhenNoTasks() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT id FROM tasks")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        assertEquals(new TreeSet<Task>(), Task.getAllTasks(database));
    }

    @Test
    public void testGetAllTasksWhenTasksExist() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT id FROM tasks")).thenReturn(crs);
        when(crs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(crs.getInt("ID")).thenReturn(1).thenReturn(2);
        TreeSet<Task> tasks = new TreeSet<>();
        tasks.add(new Task(1, database));
        tasks.add(new Task(2, database));
        assertEquals(tasks, Task.getAllTasks(database));
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = new Task("A", "B", 1, database);
        t.delete();
        verify(database).executeStatement("DELETE FROM tasks WHERE ID=1");
    }

    @Test
    public void testStaticDelete() throws SQLException, ClassNotFoundException {
        Task.delete(1, database);
        verify(database).executeStatement("DELETE FROM tasks WHERE ID=1");
    }

    @Test
    public void testGetIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(true).when(t).exists();
        assertEquals(1, t.getId());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.getId();
    }

    @Test
    public void testSetDateTimeNotNullWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(true).when(t).exists();
        t.setDateTime(5L);
        verify(database).executeStatement("UPDATE tasks SET date_time=5 WHERE ID=1");
    }

    @Test
    public void testSetDateTimeNullWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate, date_time) VALUES (\"A\", \"B\", 1, 5)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, 5L, database));
        Mockito.doReturn(true).when(t).exists();
        t.setDateTime(null);
        verify(database).executeStatement("UPDATE tasks SET date_time=null WHERE ID=1");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetDateTimeWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.setDateTime(5L);
    }

    @Test
    public void testGetDateTimeWhenExistsAndNotNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT date_time FROM tasks WHERE ID=1")).thenReturn(crs);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate, date_time) VALUES (\"A\", \"B\", 1, 5)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, 5L, database));
        when(crs.getLong("date_time")).thenReturn(5L);
        Mockito.doReturn(true).when(t).exists();
        assertEquals(new Long(5L), t.getDateTime());
    }

    @Test
    public void testGetDateTimeWhenExistsAndNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT date_time FROM tasks WHERE ID=1")).thenReturn(crs);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        when(crs.getLong("date_time")).thenReturn(0L);
        Mockito.doReturn(true).when(t).exists();
        assertEquals(null, t.getDateTime());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetDateTimeWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.getDateTime();
    }

    @Test
    public void testGetNameWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT name FROM tasks WHERE ID=1")).thenReturn(crs);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        when(crs.getString("name")).thenReturn("A");
        Mockito.doReturn(true).when(t).exists();
        assertEquals("A", t.getName());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetNameWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.getName();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameWhenNameIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, database);
        t.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameWhenNameIsTooLong() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, database);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 255; i++) {
            sb.append('a');
        }
        t.setName(sb.toString());
    }

    @Test
    public void testSetNameWithValidDataAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(true).when(t).exists();
        t.setName("DOG");
        verify(database).executeStatement("UPDATE tasks SET name=\"DOG\" WHERE ID=1");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetNameWithValidDataAndNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.setName("Dog");
    }

    @Test
    public void testGetDescriptionWhenExistsAndNotNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT description FROM tasks WHERE ID=1")).thenReturn(crs);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        when(crs.getString("description")).thenReturn("B");
        Mockito.doReturn(true).when(t).exists();
        assertEquals("B", t.getDescription());
    }

    @Test
    public void testGetDescriptionWhenExistsAndNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT description FROM tasks WHERE ID=1")).thenReturn(crs);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", null, 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", null, 1, database));
        when(crs.getString("description")).thenReturn(null);
        Mockito.doReturn(true).when(t).exists();
        assertEquals(null, t.getDescription());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetDescriptionWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.getDescription();
    }

    @Test
    public void testSetDescriptionWithValidDataAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(true).when(t).exists();
        t.setDescription("DOG");
        verify(database).executeStatement("UPDATE tasks SET description=\"DOG\" WHERE ID=1");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetDescriptionWithValidDataAndNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.setDescription("Dog");
    }

    @Test
    public void testGetEffortEstimateWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT effort_estimate FROM tasks WHERE ID=1")).thenReturn(crs);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        when(crs.getLong("effort_estimate")).thenReturn(1L);
        Mockito.doReturn(true).when(t).exists();
        assertEquals(1L, t.getEffortEstimate());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetEffortEstimateWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.getEffortEstimate();
    }

    @Test
    public void testSetEffortEstimateWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(true).when(t).exists();
        t.setEffortEstimate(2);
        verify(database).executeStatement("UPDATE tasks SET effort_estimate=2 WHERE ID=1");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetEffortEstimateWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.setEffortEstimate(2);
    }

    @Test
    public void testGetResourcesWhenNoResources() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT resource_id FROM resource_to_task WHERE task_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Task t = new Task("A", "B", 1, database);
        assertEquals(new TreeSet<Resource>(), t.getResources());
    }

    @Test
    public void testGetResourcesWhenResourcesExist() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT resource_id FROM resource_to_task WHERE task_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(crs.getInt("resource_id")).thenReturn(1).thenReturn(2);
        Task t = new Task("A", "B", 1, database);
        TreeSet<Resource> resources = new TreeSet<>();
        resources.add(new Resource(1, database));
        resources.add(new Resource(2, database));
        assertEquals(resources, t.getResources());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddResourceWhenResourceIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, database);
        t.addResource(null);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testAddResourceWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = mock(Resource.class);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.addResource(r);
    }

    @Test
    public void testAddResourceWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = mock(Resource.class);
        when(r.getId()).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(true).when(t).exists();
        t.addResource(r);
        verify(database).executeStatement("INSERT INTO resource_to_task (resource_id, task_id) VALUES (1, 1)");
    }

    @Test
    public void testGetPeopleWhenNoPeople() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT person_id FROM people_to_tasks WHERE task_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Task t = new Task("A", "B", 1, database);
        assertEquals(new TreeSet<Person>(), t.getPeople());
    }

    @Test
    public void testGetPeopleWhenPeopleExist() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT person_id FROM people_to_tasks WHERE task_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(crs.getInt("person_id")).thenReturn(1).thenReturn(2);
        Task t = new Task("A", "B", 1, database);
        TreeSet<Person> people = new TreeSet<>();
        people.add(new Person(1, database));
        people.add(new Person(2, database));
        assertEquals(people, t.getPeople());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPersonWhenPersonIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = new Task("A", "B", 1, database);
        t.addPerson(null);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testAddPersonWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = mock(Person.class);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(false).when(t).exists();
        t.addPerson(p);
    }

    @Test
    public void testAddPersonWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = mock(Person.class);
        when(p.getId()).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = Mockito.spy(new Task("A", "B", 1, database));
        Mockito.doReturn(true).when(t).exists();
        t.addPerson(p);
        verify(database).executeStatement("INSERT INTO people_to_tasks (person_id, task_id) VALUES (1, 1)");
    }

    @Test
    public void testExistsWhenExists() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM tasks WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        Task t = new Task("A", "B", 1, database);
        assertEquals(true, t.exists());
    }

    @Test
    public void testExistsWhenNotExists() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM tasks WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Task t = new Task("A", "B", 1, database);
        assertEquals(false, t.exists());
    }

    @Test
    public void testStaticExistsWhenExists() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM tasks WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        assertEquals(true, Task.exists(1, database));
    }

    @Test
    public void testStaticExistsWhenNotExists() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM tasks WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        assertEquals(false, Task.exists(1, database));
    }

    @Test
    public void testEqualsWhenSameObject() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t1 = new Task("A", "B", 1, database);
        Task t2 = t1;
        assertTrue(t1.equals(t2));
    }

    @Test
    public void testEqualsWithNull() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = new Task("A", "B", 1, database);
        assertFalse(t.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = new Task("A", "B", 1, database);
        assertFalse(t.equals("Hello"));
    }

    @Test
    public void testEqualsWhenEqual() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t1 = new Task("A", "B", 1, database);
        Task t2 = new Task(1, database);
        assertTrue(t1.equals(t2));
    }

    @Test
    public void testEqualsWhenUnEqual() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"C\", \"D\", 5)")).thenReturn(2);
        Task t1 = new Task("A", "B", 1, database);
        Task t2 = new Task("C", "D", 5, database);
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testHashCode() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        Task t = new Task("A", "B", 1, database);
        assertEquals(1, t.hashCode());
    }

    @Test
    public void testCompareTo() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"A\", \"B\", 1)")).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO tasks (name, description, effort_estimate) VALUES (\"C\", \"D\", 5)")).thenReturn(2);
        Task t1 = new Task("A", "B", 1, database);
        Task t2 = new Task("C", "D", 5, database);
        assertEquals(-1, t1.compareTo(t2));
    }
}

