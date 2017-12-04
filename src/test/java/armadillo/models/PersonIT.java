package armadillo.models;

import org.junit.Test;

import java.sql.SQLException;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class PersonIT {

    private Database database = new Database();

    @Test
    public void testPersonConstructorWithValidNamesThenGettersAndDeleteAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        Person p1 = new Person("Robert", "Greener", database);
        Person p2 = new Person("Test", "Test", database);
        int person1Id = p1.getId();
        int person2Id = p2.getId();
        assertEquals("Robert", p1.getFirstName());
        assertEquals("Greener", p1.getLastName());
        assertEquals("Test", p2.getFirstName());
        assertEquals("Test", p2.getLastName());
        assertTrue(p1.exists());
        assertTrue(p2.exists());
        assertTrue(Person.exists(person1Id, database));
        assertTrue(Person.exists(person2Id, database));
        Person.delete(person1Id, database);
        p2.delete();
        assertFalse(p1.exists());
        assertFalse(Person.exists(person1Id, database));
        assertFalse(p2.exists());
        assertFalse(Person.exists(person2Id, database));
    }

    @Test
    public void testGetAllPeople() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        TreeSet<Person> people = Person.getAllPeople(database);
        people.add(new Person("Robert", "Greener", database));
        people.add(new Person("Test", "Two", database));
        people.add(new Person("Test", "Three", database));
        assertEquals(people, Person.getAllPeople(database));
        for (Person person : people) {
            Person.delete(person.getId(), database);
        }
    }

    @Test
    public void testGetFullName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        assertEquals("Robert Greener", p.getFullName());
        Person.delete(p.getId(), database);
    }

    @Test
    public void testSetFirstName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        p.setFirstName("Rob");
        assertEquals("Rob", p.getFirstName());
        Person.delete(p.getId(), database);
    }

    @Test
    public void testSetLastName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener", database);
        p.setLastName("Green");
        assertEquals("Green", p.getLastName());
        Person.delete(p.getId(), database);
    }

    @Test
    public void testGetPersonByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p1 = new Person("Robert", "Greener", database);
        Person p2 = Person.getPersonByID(p1.getId(), database);
        assertTrue(p1.equals(p2));
        Person.delete(p1.getId(), database);
    }

    @Test
    public void testAddTaskAndGetAllTasks() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p1 = new Person("Robert", "Greener", database);
        Task t1 = new Task("Task One", "Long desc", 10200201, database);
        Task t2 = new Task("Task Two", "Desc", 10202144, database);
        p1.addTask(t1);
        p1.addTask(t2);
        TreeSet<Task> tasks = new TreeSet<>();
        tasks.add(t1);
        tasks.add(t2);
        assertEquals(tasks, p1.getTasks());
        t1.delete();
        t2.delete();
        p1.delete();
    }
}
