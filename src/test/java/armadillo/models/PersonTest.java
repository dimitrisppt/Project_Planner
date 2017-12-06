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

public class PersonTest {

    @Mock
    Database database;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenFirstNameIsNull() throws SQLException, ClassNotFoundException {
        new Person(null, "Greener", database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenLastNameIsNull() throws SQLException, ClassNotFoundException {
        new Person("Robert", null, database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenFirstNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        new Person(sb.toString(), "Greener", database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenLastNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        new Person("Robert", sb.toString(), database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenFirstNameIsNotValid() throws SQLException, ClassNotFoundException {
        new Person("R@A22", "Greener", database);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenLastNameIsNotValid() throws SQLException, ClassNotFoundException {
        new Person("Robert", "Gre@ene", database);
    }

    @Test
    public void testPackagePrivateConstructorDoesNotThrowException() {
        new Person(1, database);
    }

    @Test
    public void testPersonConstructorWithValidData() throws SQLException, ClassNotFoundException {
        Person p = new Person("Robert", "Greener", database);
        verify(database).executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameWhenNameIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        p.setFirstName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameWhenNameIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        p.setLastName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameWhenNameIsNotValid() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        p.setFirstName("R@b*ert");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameWhenNameIsNotValid() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        p.setLastName("G&reber");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameWhenNameIsTooLong() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        p.setFirstName(sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameWhenNameIsTooLong() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        p.setLastName(sb.toString());
    }

    @Test
    public void testHashCode() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = new Person("Robert", "Greener", database);
        assertEquals(1, p.hashCode());
    }

    @Test
    public void testCompareTo() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Robertson\")")).thenReturn(2);
        Person p1 = new Person("Robert", "Greener", database);
        Person p2 = new Person("Robert", "Robertson", database);
        assertEquals(-1, p1.compareTo(p2));
    }

    @Test
    public void testSetFirstNameWithValidDataAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(true).when(p).exists();
        p.setFirstName("Rob");
        verify(database).executeStatement("UPDATE people SET first_name=\"Rob\" WHERE ID=1");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetFirstNameWithValidDataAndNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(false).when(p).exists();
        p.setFirstName("Rob");
    }

    @Test
    public void testSetLastNameWithValidDataAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(true).when(p).exists();
        p.setLastName("Robertson");
        verify(database).executeStatement("UPDATE people SET last_name=\"Robertson\" WHERE ID=1");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetLastNameWithValidDataAndNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(false).when(p).exists();
        p.setLastName("Robertson");
    }

    @Test
    public void testDelete() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = new Person("Robert", "Greener", database);
        p.delete();
        verify(database).executeStatement("DELETE FROM people WHERE ID=1");
    }

    @Test
    public void testStaticDelete() throws SQLException, ClassNotFoundException {
        Person.delete(1, database);
        verify(database).executeStatement("DELETE FROM people WHERE ID=1");
    }

    @Test
    public void testStaticExistsWhenExists() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        assertEquals(true, Person.exists(1, database));
    }

    @Test
    public void testStaticExistsWhenNotExists() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        assertEquals(false, Person.exists(1, database));
    }

    @Test
    public void testExistsWhenExists() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        Person p = new Person("Robert", "Greener", database);
        assertEquals(true, p.exists());
    }

    @Test
    public void testExistsWhenNotExists() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Person p = new Person("Robert", "Greener", database);
        assertEquals(false, p.exists());
    }

    @Test
    public void testGetPersonByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        Person p = Person.getPersonByID(1, database);
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetPersonByIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Person p = Person.getPersonByID(1, database);
    }

    @Test
    public void testGetIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(true).when(p).exists();
        assertEquals(1, p.getId());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(false).when(p).exists();
        p.getId();
    }

    @Test
    public void testGetFirstNameWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT first_name FROM people WHERE ID=1")).thenReturn(crs);
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        when(crs.getString("first_name")).thenReturn("Robert");
        Mockito.doReturn(true).when(p).exists();
        assertEquals("Robert", p.getFirstName());
    }

    @Test
    public void testGetLastNameWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT last_name FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.getString("last_name")).thenReturn("Greener");
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(true).when(p).exists();
        assertEquals("Greener", p.getLastName());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetFirstNameWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(false).when(p).exists();
        p.getFirstName();
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetLastNameWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(false).when(p).exists();
        p.getLastName();
    }

    @Test
    public void testGetFullNameWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT last_name FROM people WHERE ID=1")).thenReturn(crs);
        when(database.executeQuery("SELECT first_name FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.getString("last_name")).thenReturn("Greener");
        when(crs.getString("first_name")).thenReturn("Robert");
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(true).when(p).exists();
        assertEquals("Robert Greener", p.getFullName());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetFullNameWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(false).when(p).exists();
        p.getFullName();
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testAddTaskWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = mock(Task.class);
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(false).when(p).exists();
        p.addTask(t);
    }

    @Test
    public void testAddTaskWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Task t = mock(Task.class);
        when(t.getId()).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = Mockito.spy(new Person("Robert", "Greener", database));
        Mockito.doReturn(true).when(p).exists();
        p.addTask(t);
        verify(database).executeStatement("INSERT INTO people_to_tasks (person_id, task_id) VALUES (1, 1)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTaskWhenTaskIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        p.addTask(null);
    }

    @Test
    public void testEqualsWhenSameObject() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p1 = new Person("Robert", "Greener", database);
        Person p2 = p1;
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testEqualsWithNull() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = new Person("Robert", "Greener", database);
        assertFalse(p.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        Person p = new Person("Robert", "Greener", database);
        assertFalse(p.equals("Hello"));
    }

    @Test
    public void testEqualsWhenEqual() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT * FROM people WHERE ID=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true);
        Person p1 = new Person("Robert", "Greener", database);
        Person p2 = Person.getPersonByID(1, database);
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testEqualsWhenUnEqual() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Test\", \"One\")")).thenReturn(2);
        Person p1 = new Person("Robert", "Greener", database);
        Person p2 = new Person("Test", "One", database);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testGetTasksWhenNoTasks() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT task_id FROM people_to_tasks WHERE person_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        Person p = new Person("Robert", "Greener", database);
        assertEquals(new TreeSet<Task>(), p.getTasks());
    }

    @Test
    public void testGetTasksWhenTasksExist() throws SQLException, ClassNotFoundException {
        when(database.executeInsertStatement("INSERT INTO people (first_name, last_name) VALUES (\"Robert\", \"Greener\")")).thenReturn(1);
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT task_id FROM people_to_tasks WHERE person_id=1")).thenReturn(crs);
        when(crs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(crs.getInt("task_id")).thenReturn(1).thenReturn(2);
        Person p = new Person("Robert", "Greener", database);
        TreeSet<Task> tasks = new TreeSet<>();
        tasks.add(new Task(1, database));
        tasks.add(new Task(2, database));
        assertEquals(tasks, p.getTasks());
    }

    @Test
    public void testGetAllPeopleWhenNoPeople() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT id FROM people")).thenReturn(crs);
        when(crs.next()).thenReturn(false);
        assertEquals(new TreeSet<Person>(), Person.getAllPeople(database));
    }

    @Test
    public void testGetAllPeopleWhenPeopleExist() throws SQLException, ClassNotFoundException {
        CachedRowSet crs = mock(CachedRowSet.class);
        when(database.executeQuery("SELECT id FROM people")).thenReturn(crs);
        when(crs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(crs.getInt("ID")).thenReturn(1).thenReturn(2);
        TreeSet<Person> people = new TreeSet<>();
        people.add(new Person(1, database));
        people.add(new Person(2, database));
        assertEquals(people, Person.getAllPeople(database));
    }
 }
