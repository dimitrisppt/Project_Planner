//package armadillo.models;
//
//import org.junit.Test;
//
//import java.sql.SQLException;
//import java.util.TreeSet;
//
//import static org.junit.Assert.*;
//
//public class PersonIT {
//    @Test
//    public void testPersonConstructorWithValidNamesThenGettersAndDeleteAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
//        Person p = new Person("Robert", "Greener");
//        int personId = p.getId();
//        assertEquals("Robert", p.getFirstName());
//        assertEquals("Greener", p.getLastName());
//        assertTrue(p.exists());
//        assertTrue(Person.exists(personId, null));
//        Person.delete(personId, null);
//        assertFalse(p.exists());
//        assertFalse(Person.exists(personId, null));
//    }
//
//    @Test(expected = ElementDoesNotExistException.class)
//    public void testGetFirstNameAfterPersonDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        Person.delete(p.getId(), null);
//        p.getFirstName();
//    }
//
//    @Test(expected = ElementDoesNotExistException.class)
//    public void testGetLastNameAfterPersonDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        Person.delete(p.getId(), null);
//        p.getLastName();
//    }
//
//    @Test
//    public void testGetAllPeople() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        TreeSet<Person> people = Person.getAllPeople(null);
//        people.add(new Person("Robert", "Greener"));
//        people.add(new Person("Test", "Two"));
//        people.add(new Person("Test", "Three"));
//        assertEquals(people, Person.getAllPeople(null));
//        for (Person person : people) {
//            Person.delete(person.getId(), null);
//        }
//    }
//
//    @Test
//    public void testGetFullName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        assertEquals("Robert Greener", p.getFullName());
//        Person.delete(p.getId(), null);
//    }
//
//    @Test(expected = ElementDoesNotExistException.class)
//    public void testGetIDAfterPersonDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        Person.delete(p.getId(), null);
//        p.getId();
//    }
//
//
//    @Test(expected = ElementDoesNotExistException.class)
//    public void testSetFirstNameWhenPersonHasBeenDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        Person.delete(p.getId(), null);
//        p.setFirstName("Rob");
//    }
//
//    @Test(expected = ElementDoesNotExistException.class)
//    public void testSetLastNameWhenPersonHasBeenDeleted() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        Person.delete(p.getId(), null);
//        p.setLastName("Green");
//    }
//
//    @Test
//    public void testSetFirstName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        p.setFirstName("Rob");
//        assertEquals("Rob", p.getFirstName());
//        Person.delete(p.getId(), null);
//    }
//
//    @Test
//    public void testSetLastName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        p.setLastName("Green");
//        assertEquals("Green", p.getLastName());
//        Person.delete(p.getId(), null);
//    }
//
//    @Test
//    public void testHashCode() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        assertEquals(p.getId(), p.hashCode());
//        Person.delete(p.getId(), null);
//    }
//
//    @Test
//    public void testGetPersonByIDWhenExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p1 = new Person("Robert", "Greener");
//        Person p2 = Person.getPersonByID(p1.getId());
//        assertTrue(p1.equals(p2));
//        Person.delete(p1.getId(), null);
//    }
//
//    @Test(expected = ElementDoesNotExistException.class)
//    public void testGetPersonByIDWhenNotExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p1 = new Person("Robert", "Greener");
//        int id = p1.getId();
//        Person.delete(id, null);
//        Person p2 = Person.getPersonByID(id);
//    }
//
//    @Test
//    public void testEqualsWhenSameObject() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p1 = new Person("Robert", "Greener");
//        Person p2 = p1;
//        assertTrue(p1.equals(p2));
//        Person.delete(p1.getId(), null);
//    }
//
//    @Test
//    public void testEqualsWithNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        assertFalse(p.equals(null));
//    }
//
//    @Test
//    public void testEqualsWithDifferentClass() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        assertFalse(p.equals("Hello"));
//    }
//
//    @Test
//    public void testEqualsWhenEqual() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p1 = new Person("Robert", "Greener");
//        Person p2 = Person.getPersonByID(p1.getId());
//        assertTrue(p1.equals(p2));
//        Person.delete(p1.getId(), null);
//    }
//
//    @Test
//    public void testEqualsWhenUnEqual() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p1 = new Person("Robert", "Greener");
//        Person p2 = new Person("Test", "One");
//        assertFalse(p1.equals(p2));
//        Person.delete(p1.getId(), null);
//        Person.delete(p2.getId(), null);
//    }
//
//    @Test
//    public void testAddTaskWhenTaskIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
//        Person p = new Person("Robert", "Greener");
//        try {
//            p.addTask(null);
//            assertEquals("This should never be reached", true, false);
//        } catch (IllegalArgumentException e) {
//            assertEquals("Exception Thrown", true, true);
//        }
//        Person.delete(p.getId(), null);
//    }
//}
