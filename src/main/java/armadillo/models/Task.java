package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.TreeSet;

public class Task implements Comparable<Task>{
    private int id;
    public final static String TABLE_NAME = "tasks";
    private Database database;

    public Task(String name, String description, long effort_estimate, Long date_time, Database database) throws SQLException, ClassNotFoundException {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        if (name.equals("")) throw new IllegalArgumentException("Name cannot be nothing");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        this.database = database;
        id = database.executeInsertStatement(String.format("INSERT INTO %s (name, description, effort_estimate, date_time) VALUES (\"%s\", \"%s\", %d, %s)", TABLE_NAME, name, description, effort_estimate, date_time).replace("\"null\"", "null"));
    }

    public Task(String name, String description, long effort_estimate, Database database) throws SQLException, ClassNotFoundException {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        if (name.equals("")) throw new IllegalArgumentException("Name cannot be nothing");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        this.database = database;
        id = database.executeInsertStatement(String.format("INSERT INTO %s (name, description, effort_estimate) VALUES (\"%s\", \"%s\", %d)", TABLE_NAME, name, description, effort_estimate).replace("\"null\"", "null"));

    }

    Task(int id, Database database) {
        this.id = id;
        this.database = database;
    }

    public static Task getTaskByID(int id, Database database) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists(id, database)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Task(id, database);
    }

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


    public static void delete(int id, Database database) throws SQLException, ClassNotFoundException {
        database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    public String getName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("name");
    }

    public void setName(String name) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET name=\"%s\" WHERE ID=%d", TABLE_NAME, name, id));
    }

    public String getDescription() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT description FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("description");
    }

    public void setDescription(String description) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET description=\"%s\" WHERE ID=%d", TABLE_NAME, description, id));
    }

    public long getEffortEstimate() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT effort_estimate FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getLong("effort_estimate");
    }

    public void setEffortEstimate(long effortEstimate) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET effort_estimate=%d WHERE ID=%d", TABLE_NAME, effortEstimate, id));
    }

    public void setDateTime(Long dateTime) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("UPDATE %s SET date_time=%s WHERE ID=%d", TABLE_NAME, dateTime, id));
    }

    public Long getDateTime() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT date_time FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        long result = rs.getLong("date_time");
        if (result == 0) return null;
        else return result;
    }

    public TreeSet<Resource> getResources() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT resource_id FROM resource_to_task WHERE task_id=%d", id));
        TreeSet<Resource> resources = new TreeSet<>();
        while (rs.next()) {
            resources.add(new Resource(rs.getInt("resource_id"), database));
        }
        return resources;
    }

    public void addResource(Resource resource) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (resource == null) throw new IllegalArgumentException("resource cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("INSERT INTO resource_to_task (resource_id, task_id) VALUES (%d, %d)", resource.getId(), id));
    }

    public TreeSet<Person> getPeople() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT person_id FROM people_to_tasks WHERE task_id=%d", id));
        TreeSet<Person> people = new TreeSet<>();
        while (rs.next()) {
            people.add(new Person(rs.getInt("person_id"), database));
        }
        return people;
    }

    public void addPerson(Person person) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (person == null) throw new IllegalArgumentException("Person cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("INSERT INTO people_to_tasks (person_id, task_id) VALUES (%d, %d)", person.getId(), id));
    }

    public TreeSet<Task> getPrerequisiteTasks() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT prereq_task_id FROM prereq_tasks WHERE this_task_id=%d", id));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(new Task(rs.getInt("prereq_task_id"), database));
        }
        return tasks;
    }

    public void addPrerequisiteTask(Task prerequisiteTask) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (prerequisiteTask == null) throw new IllegalArgumentException("Task cannot be null");
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        database.executeStatement(String.format("INSERT INTO prereq_tasks (this_task_id, prereq_task_id) VALUES (%d, %d)", id, prerequisiteTask.getId()));
    }

    public boolean exists() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT * FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    public static boolean exists(int id, Database database) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = database.executeQuery(String.format("SELECT * FROM %s WHERE ID=%d", TABLE_NAME, id));
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
