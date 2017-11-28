package armadillo.models;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;
import java.util.TreeSet;

public class TestPerson {
    @Test
    public void testPersonConstructorWithValidNamesThenGettersAndDeleteAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        Person p = new Person("Robert", "Greener");
        int personId = p.getId();
        assertEquals("Robert", p.getFirstName());
        assertEquals("Greener", p.getLastName());
        assertTrue(p.exists());
        assertTrue(Person.exists(personId));
        Person.deletePerson(personId);
        assertFalse(p.exists());
        assertFalse(Person.exists(personId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenFirstNameIsNull() throws SQLException, ClassNotFoundException {
        new Person(null, "Greener");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenLastNameIsNull() throws SQLException, ClassNotFoundException {
        new Person("Robert", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenFirstNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        new Person(sb.toString(), "Greener");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonConstructorWhenLastNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        new Person("Robert", sb.toString());
    }


    @Test(expected = ElementDoesNotExistException.class)
    public void testGetFirstNameAfterPersonDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        Person.deletePerson(p.getId());
        p.getFirstName();
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetLastNameAfterPersonDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        Person.deletePerson(p.getId());
        p.getLastName();
    }

    @Test
    public void testGetAllPeople() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        TreeSet<Person> people = Person.getAllPeople();
        people.add(new Person("Robert", "Greener"));
        people.add(new Person("Test", "Two"));
        people.add(new Person("Test", "Three"));
        assertEquals(people, Person.getAllPeople());
        for (Person person : people) {
            Person.deletePerson(person.getId());
        }
    }

    @Test
    public void testGetFullName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        assertEquals("Robert Greener", p.getFullName());
        Person.deletePerson(p.getId());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetIDAfterPersonDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        Person.deletePerson(p.getId());
        p.getId();
    }

    @Test
    public void testSetFirstNameWhenNameIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        try {
            p.setFirstName(null);
            assertEquals("This should never be reached", true, false);
        } catch (IllegalArgumentException e) {
            assertEquals("Exception Thrown", true, true);
        }
        Person.deletePerson(p.getId());
    }

    @Test
    public void testSetLastNameWhenNameIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        try {
            p.setLastName(null);
            assertEquals("This should never be reached", true, false);
        } catch (IllegalArgumentException e) {
            assertEquals("Exception Thrown", true, true);
        }
        Person.deletePerson(p.getId());
    }

    @Test
    public void testSetFirstNameWhenNameIsTooLong() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        try {
            p.setFirstName(sb.toString());
            assertEquals("This should never be reached", true, false);
        } catch (IllegalArgumentException e) {
            assertEquals("Exception Thrown", true, true);
        }
        Person.deletePerson(p.getId());
    }

    @Test
    public void testSetLastNameWhenNameIsTooLong() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 30; i++) {
            sb.append('a');
        }
        try {
            p.setLastName(sb.toString());
            assertEquals("This should never be reached", true, false);
        } catch (IllegalArgumentException e) {
            assertEquals("Exception Thrown", true, true);
        }
        Person.deletePerson(p.getId());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetFirstNameWhenPersonHasBeenDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        Person.deletePerson(p.getId());
        p.setFirstName("Rob");
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetLastNameWhenPersonHasBeenDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        Person.deletePerson(p.getId());
        p.setLastName("Green");
    }

    @Test
    public void testSetFirstName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        p.setFirstName("Rob");
        assertEquals("Rob", p.getFirstName());
        Person.deletePerson(p.getId());
    }

    @Test
    public void testSetLastName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        p.setLastName("Green");
        assertEquals("Green", p.getLastName());
        Person.deletePerson(p.getId());
    }

    @Test
    public void testHashCode() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        assertEquals(p.getId(), p.hashCode());
        Person.deletePerson(p.getId());
    }

    @Test
    public void testGetPersonByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p1 = new Person("Robert", "Greener");
        Person p2 = Person.getPersonByID(p1.getId());
        assertTrue(p1.equals(p2));
        Person.deletePerson(p1.getId());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetPersonByIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p1 = new Person("Robert", "Greener");
        int id = p1.getId();
        Person.deletePerson(id);
        Person p2 = Person.getPersonByID(id);
    }

    @Test
    public void testEqualsWhenSameObject() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p1 = new Person("Robert", "Greener");
        Person p2 = p1;
        assertTrue(p1.equals(p2));
        Person.deletePerson(p1.getId());
    }

    @Test
    public void testEqualsWithNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        assertFalse(p.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        assertFalse(p.equals("Hello"));
    }

    @Test
    public void testEqualsWhenEqual() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p1 = new Person("Robert", "Greener");
        Person p2 = Person.getPersonByID(p1.getId());
        assertTrue(p1.equals(p2));
        Person.deletePerson(p1.getId());
    }

    @Test
    public void testEqualsWhenUnEqual() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p1 = new Person("Robert", "Greener");
        Person p2 = new Person("Test", "One");
        assertFalse(p1.equals(p2));
        Person.deletePerson(p1.getId());
        Person.deletePerson(p2.getId());
    }

    @Test
    public void testAddTaskWhenTaskIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Person p = new Person("Robert", "Greener");
        try {
            p.addTask(null);
            assertEquals("This should never be reached", true, false);
        } catch (IllegalArgumentException e) {
            assertEquals("Exception Thrown", true, true);
        }
        Person.deletePerson(p.getId());
    }
}
