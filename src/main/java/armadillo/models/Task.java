package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.TreeSet;

public class Task implements Comparable<Task>{
    private int id;
    public final static String TABLE_NAME = "tasks";
    private Database database;

    public Task(String name, String description, long effort_estimate, Database database) throws SQLException, ClassNotFoundException {
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        this.database = database;
        id = database.executeInsertStatement(String.format("INSERT INTO %s (name, description, effort_estimate) VALUES (\"%s\", \"%s\", \"%s\");", TABLE_NAME, name, description, effort_estimate));
    }

    private Task(int id) {
        this.id = id;
    }

    Task(int id, Database database) {
        this.id = id;
    }

    public static Task getTaskByID(int id) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists(id)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Task(id);
    }

    public static TreeSet<Task> getAllTasks() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = new Database().executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(new Task(rs.getInt("ID")));
        }
        return tasks;
    }

<<<<<<< HEAD
    /**
     * Deletes this task from the database
     * @throws SQLException If there is an error with the SQL Statement, should not happen
     * @throws ClassNotFoundException If the SQLite JDBC plugin is not installed
     */
    public void delete() throws SQLException, ClassNotFoundException {
        database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }


    public static void deleteTask(int id) throws SQLException, ClassNotFoundException {
        new Database().executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
=======
    public static void delete(int id) throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    public static void delete() throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
>>>>>>> model-implementation
    }

    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    public String getName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = new Database().executeQuery(String.format("SELECT name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("name");
    }

    public void setName(String name) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        new Database().executeStatement(String.format("UPDATE %s SET name=\"%s\" WHERE ID=%d", TABLE_NAME, name, id));
    }

    public String getDescription() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = new Database().executeQuery(String.format("SELECT description FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("description");
    }

    public void setDescription(String description) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        new Database().executeStatement(String.format("UPDATE %s SET description=\"%s\" WHERE ID=%d", TABLE_NAME, description, id));
    }

    public long getEffortEstimate() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = new Database().executeQuery(String.format("SELECT effort_estimate FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getLong("effort_estimate");
    }

    public void setEffortEstimate(long effortEstimate) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        new Database().executeStatement(String.format("UPDATE %s SET effort_estimate=%d WHERE ID=%d", TABLE_NAME, effortEstimate, id));
    }

    public TreeSet<Resource> getResources() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT resource_id FROM resource_to_task WHERE task_id=%d", id));
        TreeSet<Resource> resources = new TreeSet<>();
        try {
            while (rs.next()) {
                resources.add(Resource.getResourceByID(rs.getInt("resource_id")));
            }
        }
        catch (ElementDoesNotExistException e) {
            //Check if Ok
        }
        return resources;
    }

    public void addResource(Resource resource) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (resource == null) throw new IllegalArgumentException("resource cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("INSERT INTO resource_to_task (resource_id, task_id) VALUES (%d, %d)", resource.getId(), id));
    }

    public TreeSet<Person> getPeople() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT person_id FROM people_to_tasks WHERE task_id=%d", id));
        TreeSet<Person> people = new TreeSet<>();
        while (rs.next()) {
            people.add(Person.getPersonByID(rs.getInt("person_id")));
        }
        return people;
    }

    public void addPerson(Person person) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (person == null) throw new IllegalArgumentException("Person cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("INSERT INTO people_to_tasks (person_id, task_id) VALUES (%d, %d)", person.getId(), id));
    }

    public boolean exists() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = new Database().executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    public static boolean exists(int id) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = new Database().executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Task o) {
        return this.id - o.id;
    }
}
