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

public class ResourceTest {

    @Mock
    Database database;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test(expected = IllegalArgumentException.class)
    public void testResourceConstructorWhenNameIsNull() throws SQLException, ClassNotFoundException {
        new Resource(null, database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResourceConstructorWhenFirstNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 255; i++) {
            sb.append('a');
        }
        new Resource(sb.toString(), database);
    }

    @Test
    public void testResourceConstructorWithValidData() throws SQLException, ClassNotFoundException {
        Resource r = new Resource("Data", database);
        verify(database).executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")");
    }

    @Test
    public void testPackagePrivateConstructorDoesNotThrowException() {
        new Resource(1, database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameWhenNameIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = new Resource("Data", database);
        r.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameWhenNameIsTooLong() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = new Resource("Data", database);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 255; i++) {
            sb.append('a');
        }
        r.setName(sb.toString());
    }

    @Test
    public void testHashCode() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = new Resource("Data", database);
        assertEquals(1, r.hashCode());
    }

    @Test
    public void testCompareTo() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Base\")")).thenReturn(2);
        Resource r1 = new Resource("Data", database);
        Resource r2 = new Resource("Base", database);
        assertEquals(-1, r1.compareTo(r2));
    }

    @Test
    public void testSetNameWithValidDataAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = Mockito.spy(new Resource("Data", database));
        Mockito.doReturn(true).when(r).exists();
        r.setName("DOG");
        verify(database).executeStatement("UPDATE resources SET name=\"DOG\" WHERE ID=1");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetNameWithValidDataAndNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = Mockito.spy(new Resource("Data", database));
        Mockito.doReturn(false).when(r).exists();
        r.setName("Dog");
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = new Resource("Data", database);
        r.delete();
        verify(database).executeStatement("DELETE FROM resources WHERE ID=1");
    }

    @Test
    public void testStaticDelete() throws SQLException, ClassNotFoundException {
        Resource.delete(1, database);
        verify(database).executeStatement("DELETE FROM resources WHERE ID=1");
    }

    @Test
    public void testStaticExistsWhenExists() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM resources WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        assertEquals(true, Resource.exists(1, database));
    }

    @Test
    public void testStaticExistsWhenNotExists() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM resources WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        assertEquals(false, Resource.exists(1, database));
    }

    @Test
    public void testExistsWhenExists() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM resources WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        Resource r = new Resource("Data", database);
        assertEquals(true, r.exists());
    }

    @Test
    public void testExistsWhenNotExists() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM resources WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Resource r = new Resource("Data", database);
        assertEquals(false, r.exists());
    }

    @Test
    public void testGetPersonByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM resources WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        Resource r = Resource.getResourceByID(1, database);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetPersonByIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM resources WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Resource r = Resource.getResourceByID(1, database);
    }

    @Test
    public void testGetIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = Mockito.spy(new Resource("Data", database));
        Mockito.doReturn(true).when(r).exists();
        assertEquals(1, r.getId());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = Mockito.spy(new Resource("Dogs", database));
        Mockito.doReturn(false).when(r).exists();
        r.getId();
    }

    @Test
    public void testGetNameWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT name FROM resources WHERE ID=1")).thenReturn(crs);
        Resource r = Mockito.spy(new Resource("Data", database));
        when(crs.getString("name")).thenReturn("Data");
        Mockito.doReturn(true).when(r).exists();
        assertEquals("Data", r.getName());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetNameWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = Mockito.spy(new Resource("Data", database));
        Mockito.doReturn(false).when(r).exists();
        r.getName();
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testAddTaskWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = mock(Task.class);
        Resource r = Mockito.spy(new Resource("Data", database));
        Mockito.doReturn(false).when(r).exists();
        r.addTask(t);
    }

    @Test
    public void testAddTaskWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = mock(Task.class);
        when(t.getId()).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = Mockito.spy(new Resource("Data", database));
        Mockito.doReturn(true).when(r).exists();
        r.addTask(t);
        verify(database).executeStatement("INSERT INTO resource_to_task (resource_id, task_id) VALUES (1, 1)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTaskWhenTaskIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource r = new Resource("Data", database);
        r.addTask(null);
    }

    @Test
    public void testEqualsWhenSameObject() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r1 = new Resource("Data", database);
        Resource r2 = r1;
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualsWithNull() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = new Resource("Data", database);
        assertFalse(r.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = new Resource("Data", database);
        assertFalse(r.equals("Hello"));
    }

    @Test
    public void testEqualsWhenEqual() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r1 = new Resource("Data", database);
        Resource r2 = new Resource(1, database);
        assertTrue(r1.equals(r2));
    }

    @Test
    public void testEqualsWhenUnEqual() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Base\")")).thenReturn(2);
        Resource r1 = new Resource("Data", database);
        Resource r2 = new Resource("Base", database);
        assertFalse(r1.equals(r2));
    }

    @Test
    public void testGetTasksWhenNoTasks() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT task_id FROM resource_to_task WHERE resource_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Resource r = new Resource("Data", database);
        assertEquals(new TreeSet<Task>(), r.getTasks());
    }

    @Test
    public void testGetTasksWhenTasksExist() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO resources (name) VALUES (\"Data\")")).thenReturn(1);
        Resource r = new Resource("Data", database);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT task_id FROM resource_to_task WHERE resource_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(crs.getInt("task_id")).thenReturn(1).thenReturn(2);
        TreeSet<Task> tasks = new TreeSet<>();
        tasks.add(new Task(1, database));
        tasks.add(new Task(2, database));
        assertEquals(tasks, r.getTasks());
    }

    @Test
    public void testGetAllResourcesWhenNoPeople() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT id FROM resources")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        assertEquals(new TreeSet<Resource>(), Resource.getAllResources(database));
    }

    @Test
    public void testGetAllResourcesWhenResourcesExist() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT id FROM resources")).thenReturn(crs);
        when(crs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(crs.getInt("ID")).thenReturn(1).thenReturn(2);
        TreeSet<Resource> resources = new TreeSet<>();
        resources.add(new Resource(1, database));
        resources.add(new Resource(2, database));
        assertEquals(resources, Resource.getAllResources(database));
    }
 }
