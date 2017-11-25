package armadillo.models;

import java.sql.*;
import java.util.TreeSet;

public class Resource implements Comparable<Resource> {
    private int id;
    public final static String TABLE_NAME = "resources";

    public Resource(String name) throws SQLException, ClassNotFoundException {
        ResultSet rs = Database.executeInsertStatement(String.format("INSERT INTO %s (name) VALUES (%s)", TABLE_NAME, name));
        id = rs.getInt("ID");
    }

    private Resource(int id) {
        this.id = id;
    }

    public static Resource getResourceByID(int id) {
        return new Resource(id);
    }

    public static TreeSet<Resource> getAllResources() throws SQLException, ClassNotFoundException {
        ResultSet rs = Database.executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Resource> resources = new TreeSet<>();
        while (rs.next()) {
            resources.add(getResourceByID(rs.getInt("ID")));
        }
        return resources;
    }

    public static void deleteResource(int id) throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    public int getId() {
        return id;
    }

    public String getName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        ResultSet rs = Database.executeQuery(String.format("SELECT name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) {
            throw new ElementDoesNotExistException(TABLE_NAME, id);
        } else {
            rs.beforeFirst();
            return rs.getString("name");
        }
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
