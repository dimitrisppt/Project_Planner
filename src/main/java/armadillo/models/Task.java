package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.TreeSet;

public class Task implements Comparable<Task>{
    private int id;
    public final static String TABLE_NAME = "tasks";

    public Task(String name, String description, long effort_estimate) throws SQLException, ClassNotFoundException {
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        id = new Database().executeInsertStatement(String.format("INSERT INTO %s (name, description, effort_estimate) VALUES (\"%s\", \"%s\", \"%s\");", TABLE_NAME, name, description, effort_estimate));
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

    public static void deleteTask(int id) throws SQLException, ClassNotFoundException {
        new Database().executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
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
