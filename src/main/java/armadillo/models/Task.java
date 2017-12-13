package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.TreeSet;

/**
 * This represents the task table in the database
 */
public class Task implements Comparable<Task>{
    /**
     * The ID of the task in the table
     */
    private int id;

    /**
     * The name of the table
     */
    public final static String TABLE_NAME = "tasks";

    /**
     * The Database connection
     */
    private Database database;

    /**
     * Creates a new Task
     * @param name The name of the task
     * @param description The description of the task
     * @param effort_estimate The effort estimate of the task
     * @param date_time The starting date and time of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public Task(String name, String description, long effort_estimate, Long date_time, Database database) throws SQLException, ClassNotFoundException {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        if (name.equals("")) throw new IllegalArgumentException("Name cannot be nothing");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        this.database = database;
        id = database.executeInsertStatement(String.format("INSERT INTO %s (name, description, effort_estimate, date_time) VALUES (\"%s\", \"%s\", %d, %s)", TABLE_NAME, name, description, effort_estimate, date_time).replace("\"null\"", "null"));
    }

    /**
     * Creates a new Task
     * @param name The name of the task
     * @param description  The description of the task
     * @param effort_estimate The effort estimate of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public Task(String name, String description, long effort_estimate, Database database) throws SQLException, ClassNotFoundException {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        if (name.equals("")) throw new IllegalArgumentException("Name cannot be nothing");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        this.database = database;
        id = database.executeInsertStatement(String.format("INSERT INTO %s (name, description, effort_estimate) VALUES (\"%s\", \"%s\", %d)", TABLE_NAME, name, description, effort_estimate).replace("\"null\"", "null"));

    }

    /**
     * Creates a new task with a given ID
     * @param id The ID of the task
     */
    Task(int id, Database database) {
        this.id = id;
        this.database = database;
    }

    /**
     * Gets a task by it's ID
     * @param id The ID of the task
     * @return the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public static Task getTaskByID(int id, Database database) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists(id, database)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Task(id, database);
    }

    /**
     * Gets all tasks in the table
     * @return A TreeSet of all tasks
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public static TreeSet<Task> getAllTasks(Database database) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(new Task(rs.getInt("ID"), database));
        }
        return tasks;
    }

    /**
     * Deletes this task from the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public void delete() throws SQLException, ClassNotFoundException {
        database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Deletes a task from the database by ID
     * @param id The ID of the task to delete
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public static void delete(int id, Database database) throws SQLException, ClassNotFoundException {
        database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Gets the ID of the task in the table
     * @return The ID of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    /**
     * Gets the name of the task
     * @return The name of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public String getName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("name");
    }

    /**
     * Sets the name of the task
     * @param name The name of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void setName(String name) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET name=\"%s\" WHERE ID=%d", TABLE_NAME, name, id));
    }

    /**
     * Gets the description of the task
     * @return The description of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public String getDescription() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT description FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("description");
    }

    /**
     * Sets the description of the task
     * @param description The description of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void setDescription(String description) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET description=\"%s\" WHERE ID=%d", TABLE_NAME, description, id));
    }

    /**
     * Gets the effort estimate of the task
     * @return The effort estimate of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public long getEffortEstimate() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT effort_estimate FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getLong("effort_estimate");
    }

    /**
     * Sets the effort estimate of the task
     * @param effortEstimate The effort estimate of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void setEffortEstimate(long effortEstimate) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET effort_estimate=%d WHERE ID=%d", TABLE_NAME, effortEstimate, id));
    }

    /**
     * Sets the date and time of the task
     * @param dateTime The date and time of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void setDateTime(Long dateTime) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET date_time=%s WHERE ID=%d", TABLE_NAME, dateTime, id));
    }

    /**
     * Gets the date and time of the task
     * @return The date and time of the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public Long getDateTime() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT date_time FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        long result = rs.getLong("date_time");
        if (result == 0) return null;
        else return result;
    }

    /**
     * Gets the resources for the task
     * @return A TreeSet of all the resources for the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public TreeSet<Resource> getResources() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT resource_id FROM resource_to_task WHERE task_id=%d", id));
        TreeSet<Resource> resources = new TreeSet<>();
        while (rs.next()) {
            resources.add(new Resource(rs.getInt("resource_id"), database));
        }
        return resources;
    }

    /**
     * Adds a resource to the task
     * @param resource The resource
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void addResource(Resource resource) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (resource == null) throw new IllegalArgumentException("resource cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("INSERT INTO resource_to_task (resource_id, task_id) VALUES (%d, %d)", resource.getId(), id));
    }

    /**
     * Gets all the people assigned to the task
     * @return A TreeSet of all the people assigned to the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public TreeSet<Person> getPeople() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT person_id FROM people_to_tasks WHERE task_id=%d", id));
        TreeSet<Person> people = new TreeSet<>();
        while (rs.next()) {
            people.add(new Person(rs.getInt("person_id"), database));
        }
        return people;
    }

    /**
     * Adds a person to the task
     * @param person The person
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void addPerson(Person person) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (person == null) throw new IllegalArgumentException("Person cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("INSERT INTO people_to_tasks (person_id, task_id) VALUES (%d, %d)", person.getId(), id));
    }

    /**
     * Gets all the prerequisite tasks for the task
     * @return A TreeSet of all the prerequisite tasks for the task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public TreeSet<Task> getPrerequisiteTasks() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT prereq_task_id FROM prereq_tasks WHERE this_task_id=%d", id));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(new Task(rs.getInt("prereq_task_id"), database));
        }
        return tasks;
    }

    /**
     * Adds a prerequisite task to the task
     * @param prerequisiteTask The prerequisite task
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     * @throws ElementDoesNotExistException If the element does not exist in the database
     */
    public void addPrerequisiteTask(Task prerequisiteTask) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (prerequisiteTask == null) throw new IllegalArgumentException("Task cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("INSERT INTO prereq_tasks (this_task_id, prereq_task_id) VALUES (%d, %d)", id, prerequisiteTask.getId()));
    }

    /**
     * Checks whether this element still exists in the database
     * @return true iff the task is in the database
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
     * @param id The ID of the task
     * @return true iff the task is still in the database
     * @throws SQLException
     * @throws ClassNotFoundException
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

        Task task = (Task) o;

        return id == task.id;
    }

    /**
     * Gets the hashCode for the task
     * @return The ID
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Sorts the Tasks by starting date and time
     * @param o The task to compare
     * @return The difference between the starting date and time
     */
    @Override
    public int compareTo(Task o) {
        long thisStart = 0;
        long oStart = 0;
        try {
            thisStart = this.getDateTime();
            oStart = o.getDateTime();
        } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException | NullPointerException e) {

        }
        if (thisStart < oStart) return -1;
        else if (thisStart == oStart && this.equals(o)) return 0;
        else return 1;
    }
}
