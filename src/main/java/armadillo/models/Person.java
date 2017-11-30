package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.TreeSet;

/**
 * This represents the person table in the database
 */
public class Person implements Comparable<Person>{
    /**
     * The ID of the person in the table
     */
    private int id;

    /**
     * The name of the table
     */
    public final static String TABLE_NAME = "people";

    /**
     * Creates a new person
     * @param first_name The first name of the person
     * @param last_name The last name of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws IllegalArgumentException If one of the names is null or the length of the names is greater than 30
     */
    public Person(String first_name, String last_name) throws SQLException, ClassNotFoundException {
        if (first_name == null || last_name == null) throw new IllegalArgumentException("names cannot be null");
        if (first_name.length() > 30 || last_name.length() > 30)
            throw new IllegalArgumentException("names must be under 30 Characters in length");
        id = Database.executeInsertStatement(String.format("INSERT INTO %s (first_name, last_name) VALUES (\"%s\", \"%s\")", TABLE_NAME, first_name, last_name));
    }

    /**
     * Creates a new person with a given Id
     * @param id The ID of the person
     */
    private Person(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the person in the table
     * @return the ID of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    /**
     * Gets the first name of the person
     * @return the first name of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public String getFirstName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT first_name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists())
            throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("first_name");
    }

    /**
     * Gets the last name of the person
     * @return the last name of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public String getLastName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT last_name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists())
            throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("last_name");
    }

    /**
     * Sets the first name of the person
     * @param firstName the first name of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     * @throws IllegalArgumentException If firstName is null, or longer than 30 chars
     */
    public void setFirstName(String firstName) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (firstName == null) throw new IllegalArgumentException("firstName cannot be null");
        if (firstName.length() > 30) throw new IllegalArgumentException("firstName must be under 30 characters");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("UPDATE %s SET first_name=\"%s\" WHERE ID=%d", TABLE_NAME, firstName, id));
    }

    /**
     * Sets the last name of person
     * @param lastName the last name of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     * @throws IllegalArgumentException If lastName is null, or longer than 30 chars
     */
    public void setLastName(String lastName) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (lastName == null) throw new IllegalArgumentException("lastName cannot be null");
        if (lastName.length() > 30) throw new IllegalArgumentException("lastName must be under 30 characters");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("UPDATE %s SET last_name=\"%s\" WHERE ID=%d", TABLE_NAME, lastName, id));
    }

    /**
     * Gets the full name of the person
     * @return the full name of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public String getFullName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Deletes a person from the database by ID
     * @param id the ID of the person to delete
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public static void delete(int id) throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Deletes this person from the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public void delete() throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Gets a person by it's ID
     * @param id the ID of the person
     * @return the Person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public static Person getPersonByID(int id) throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists(id)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Person(id);
    }

    /**
     * Gets all People in the table
     * @return A TreeSet of all people
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public static TreeSet<Person> getAllPeople() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Person> people = new TreeSet<>();
        while (rs.next()) {
            people.add(new Person(rs.getInt("ID")));
        }
        return people;
    }

    /**
     * Gets all the tasks assigned to this Person
     * @return A TreeSet of all the Tasks assigned to this person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public TreeSet<Task> getTasks() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT task_id FROM people_to_tasks WHERE person_id=%d", id));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(Task.getTaskByID(rs.getInt("task_id")));
        }
        return tasks;
    }

    /**
     * Assigns a task to this Person
     * @param task the task to add
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void addTask(Task task) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (task == null) throw new IllegalArgumentException("Task cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("INSERT INTO people_to_tasks (person_id, task_id) VALUES (%d, %d)", id, task.getId()));
    }

    /**
     * Checks whether this element still exists in the database
     * @return true iff there is an element by the current Person's ID in the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public boolean exists() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    /**
     * Checks whether this element still exists in the database
     * @param id The ID
     * @return true iff there is an element by the supplied id in the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public static boolean exists(int id) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    /**
     * Checks whether 2 objects are equal
     * @param o The object to check
     * @return true iff they are the same class, and have the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return id == person.id;
    }

    /**
     * Gets the hashCode for the resource
     * @return the ID
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Sorts the People by ID (in ascending order)
     * @param o The Person to compare
     * @return The difference between ID's
     */
    @Override
    public int compareTo(Person o) {
        return this.id - o.id;
    }
}
