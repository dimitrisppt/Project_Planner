package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.TreeSet;

public class Resource implements Comparable<Resource> {
    private int id;
    public final static String TABLE_NAME = "resources";

    public Resource(String name) throws SQLException, ClassNotFoundException {
        if (name.length() > 255) throw new IllegalArgumentException("name must be under 255 chars");
        id = Database.executeInsertStatement(String.format("INSERT INTO %s (name) VALUES (\"%s\");", TABLE_NAME, name));

    }

    private Resource(int id) {
        this.id = id;
    }

    public static Resource getResourceByID(int id) throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists(id)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Resource(id);
    }

    public static TreeSet<Resource> getAllResources() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Resource> resources = new TreeSet<>();
        while (rs.next()) {
            resources.add(new Resource(rs.getInt("ID")));
        }
        return resources;
    }

    public static void deleteResource(int id) throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    public String getName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("name");
    }

    public void setName(String name) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("UPDATE %s SET name=\"%s\" WHERE ID=%d", TABLE_NAME, name, id));
    }

    public TreeSet<Task> getTasks() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT task_id FROM resource_to_task WHERE resource_id=%d", id));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(Task.getTaskByID(rs.getInt("task_id")));
        }
        return tasks;
    }

    public void addTask(Task task) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("INSERT INTO resource_to_task (resource_id, task_id) VALUES (%d, %d)", id, task.getId()));
    }

    public boolean exists() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    public static boolean exists(int id) throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT * FROM %S WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        return id == resource.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Resource o) {
        return this.id - o.id;
    }
}
