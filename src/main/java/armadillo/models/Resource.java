package armadillo.models;

import javafx.beans.Observable;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.TreeSet;

/**
 * This is a class that represents the resource table in the database, while providing access
 * to the resource_to_task table through getTasks() and addTask()
 */
public class Resource implements Comparable<Resource> {
    /**
     * The ID of the entry in the database
     */
    private int id;

    /**
     * This is the table name
     */
    public final static String TABLE_NAME = "resources";

    /**
     * Creates a new Resource
     * @param name the name of the resource, not nul
     * @throws SQLException If there is an error with the SQL statements, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     * @throws IllegalArgumentException If name.length() > 255 or name is null
     */
    public Resource(String name) throws SQLException, ClassNotFoundException {
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        id = Database.executeInsertStatement(String.format("INSERT INTO %s (name) VALUES (\"%s\");", TABLE_NAME, name));

    }

    /**
     * Creates a new Resource object referencing a ID in the database
     * @param id the id of the resource
     */
    private Resource(int id) {
        this.id = id;
    }

    /**
     * Gets a resource by it's ID
     * @param id the ID of the resource in the database
     * @return the Resource
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     * @throws ElementDoesNotExistException If the element has been deleted from the database
     */
    public static Resource getResourceByID(int id) throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists(id)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Resource(id);
    }

    /**
     * Gets all the resources from the database
     * @return a TreeSet containing all the resources
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     */
    public static TreeSet<Resource> getAllResources() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Resource> resources = new TreeSet<>();
        while (rs.next()) {
            resources.add(new Resource(rs.getInt("ID")));
        }
        return resources;
    }

    /**
     * Deletes a resource by ID
     * @param id the id to delete from the database
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     */
    public static void delete(int id) throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Delete this resource
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     */
    public void delete() throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    /**
     * Gets the ID of this Resource in the database
     * @return the id of the element in the database
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     * @throws ElementDoesNotExistException If the element has been deleted from the database
     */
    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    /**
     * Gets the name of this Resource
     * @return the name of the Resource
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     * @throws ElementDoesNotExistException If the element has been deleted from the database
     */
    public String getName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("name");
    }

    /**
     * Sets the name of this Resource
     * @param name the new name of the Resource, not null
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     * @throws ElementDoesNotExistException If the element has been deleted from the database
     * @throws IllegalArgumentException If name.length() > 255 or name is null
     */
    public void setName(String name) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("UPDATE %s SET name=\"%s\" WHERE ID=%d", TABLE_NAME, name, id));
    }

    /**
     * Gets all the tasks this resource is assigned to
     * @return A TreeSet of Tasks this resource is assigned to
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     */
    public TreeSet<Task> getTasks() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT task_id FROM resource_to_task WHERE resource_id=%d", id));
        TreeSet<Task> tasks = new TreeSet<>();
        try {
            while (rs.next()) {
                tasks.add(Task.getTaskByID(rs.getInt("task_id")));
            }
        }
        catch (ElementDoesNotExistException e) {
            //Check if Ok
        }
        return tasks;
    }

    /**
     * Assigns a Task to this resource
     * @param task the Task to assign
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     * @throws ElementDoesNotExistException If the element has been deleted from the database
     */
    public void addTask(Task task) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (task == null) throw new IllegalArgumentException("task cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("INSERT INTO resource_to_task (resource_id, task_id) VALUES (%d, %d)", id, task.getId()));
    }

    /**
     * Checks whether this Resource still exists
     * @return true iff the resource is in the database
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     */
    public boolean exists() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        return rs.next();
    }

    /**
     * Checks whether a Resource still exists in the database
     * @param id The ID of the Resource in the database
     * @return true iff the resource is still in the database
     * @throws SQLException If the SQL statement is not valid, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not in the classpath
     */
    public static boolean exists(int id) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        return rs.next();
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

        Resource resource = (Resource) o;

        return id == resource.id;
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
     * Sorts the Resources by ID (in ascending order)
     * @param o The resource to compare
     * @return The difference between ID's
     */
    @Override
    public int compareTo(Resource o) {
        return this.id - o.id;
    }
}
