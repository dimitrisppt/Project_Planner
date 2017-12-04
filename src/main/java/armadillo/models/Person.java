package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * The Database connection
     */
    private Database database;

    /**
     * Creates a new person
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws IllegalArgumentException If one of the names is null or the length of the names is greater than 30, or if the name is not valid
     */
    public Person(String firstName, String lastName) throws SQLException, ClassNotFoundException {
        if (firstName == null || lastName == null) throw new IllegalArgumentException("names cannot be null");
        if (firstName.length() > 30 || lastName.length() > 30)
            throw new IllegalArgumentException("names must be under 30 Characters in length");
        Pattern p = Pattern.compile("^([ \\u00c0-\\u01ffa-zA-Z'\\-])+$");
        Matcher m1 = p.matcher(firstName);
        Matcher m2 = p.matcher(lastName);
        this.database = database;
        id = database.executeInsertStatement(String.format("INSERT INTO %s (first_name, last_name) VALUES (\"%s\", \"%s\")", TABLE_NAME, first_name, last_name));
    }

    /**
     * Creates a new person with a given Id
     * @param id The ID of the person
     */
    Person(int id, Database database) {
        this.id = id;
        this.database = database;
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
        CachedRowSet rs = database.executeQuery(String.format("SELECT first_name FROM %s WHERE ID=%d", TABLE_NAME, id));
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
        CachedRowSet rs = database.executeQuery(String.format("SELECT last_name FROM %s WHERE ID=%d", TABLE_NAME, id));
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
     * @throws IllegalArgumentException If firstName is null, or longer than 30 chars, or if the name is not valid
     */
    public void setFirstName(String firstName) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (firstName == null) throw new IllegalArgumentException("firstName cannot be null");
        if (firstName.length() > 30) throw new IllegalArgumentException("firstName must be under 30 characters");
        Pattern p = Pattern.compile("^([ \\u00c0-\\u01ffa-zA-Z'\\-])+$");
        Matcher m = p.matcher(firstName);
        if (!m.find()) throw new IllegalArgumentException("firstName must be a valid name (no special characters, numbers etc.)");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET first_name=\"%s\" WHERE ID=%d", TABLE_NAME, firstName, id));
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
        Pattern p = Pattern.compile("^([ \\u00c0-\\u01ffa-zA-Z'\\-])+$");
        Matcher m = p.matcher(lastName);
        if (!m.find()) throw new IllegalArgumentException("lastName must be a valid name (no special characters, numbers etc.)");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET last_name=\"%s\" WHERE ID=%d", TABLE_NAME, lastName, id));
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
     * @param database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public static void delete(int id, Database database) throws SQLException, ClassNotFoundException {
        database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Deletes this person from the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public void delete() throws SQLException, ClassNotFoundException {
        database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Gets a person by it's ID
     * @param id the ID of the person
     * @param database
     * @return the Person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public static Person getPersonByID(int id, Database database) throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists(id, database)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Person(id, database);
    }

    /**
     * Gets all People in the table
     * @return A TreeSet of all people
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @param database
     */
    public static TreeSet<Person> getAllPeople(Database database) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Person> people = new TreeSet<>();
        while (rs.next()) {
            people.add(new Person(rs.getInt("ID"), database));
        }
        return people;
    }

    /**
     * Gets all the tasks assigned to this Person
     * @return A TreeSet of all the Tasks assigned to this person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public TreeSet<Task> getTasks() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT task_id FROM people_to_tasks WHERE person_id=%d", id));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(new Task(rs.getInt("task_id"), database));
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
        database.executeStatement(String.format("INSERT INTO people_to_tasks (person_id, task_id) VALUES (%d, %d)", id, task.getId()));
    }

    /**
     * Checks whether this element still exists in the database
     * @return true iff there is an element by the current Person's ID in the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public boolean exists() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT * FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    /**
     * Checks whether this element still exists in the database
     * @param id The ID
     * @param database
     * @return true iff there is an element by the supplied id in the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public static boolean exists(int id, Database database) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT * FROM %s WHERE ID=%d", TABLE_NAME, id));
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
