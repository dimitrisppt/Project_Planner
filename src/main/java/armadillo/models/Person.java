package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

public class Person implements Comparable<Person>{
    private int id;
    public final static String TABLE_NAME = "people";

    public Person(String first_name, String last_name) throws SQLException, ClassNotFoundException {
        if (first_name.length() > 30 || last_name.length() > 30)
            throw new IllegalArgumentException("names must be under 30 Characters in length");
        id = Database.executeInsertStatement(String.format("INSERT INTO %s (first_name, last_name) VALUES (%s, %s)", TABLE_NAME, first_name, last_name));
    }

    private Person(int id) {
        this.id = id;
    }

    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    public String getFirstName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT first_name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists())
            throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("first_name");
    }

    public String getLastName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT last_name FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!exists())
            throw new ElementDoesNotExistException(TABLE_NAME, id);
        rs.next();
        return rs.getString("last_name");
    }

    public void setFirstName(String firstName) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("UPDATE %s SET first_name=%s WHERE ID=%d", TABLE_NAME, firstName, id));
    }

    public void setLastName(String lastName) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("UPDATE %s SET last_name=%s WHERE ID=%d", TABLE_NAME, lastName, id));
    }

    public String getFullName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        return getFirstName() + " " + getLastName();
    }

    public static void deletePerson(int id) throws SQLException, ClassNotFoundException {
        Database.executeStatement(String.format("DELETE FROM %s WHERE ID=%d", TABLE_NAME, id));
    }

    public static Person getPersonByID(int id) throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists(id)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new Person(id);
    }

    public static TreeSet<Person> getAllPeople() throws SQLException, ClassNotFoundException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT id FROM %s", TABLE_NAME));
        TreeSet<Person> people = new TreeSet<>();
        try {
            while (rs.next()) {
                people.add(getPersonByID(rs.getInt("ID")));
            }
        } catch (ElementDoesNotExistException e) {
            // This will never happen
        }
        return people;
    }

    public TreeSet<Task> getTasks() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT task_id FROM people_to_tasks WHERE person_id=%d", id));
        TreeSet<Task> tasks = new TreeSet<>();
        while (rs.next()) {
            tasks.add(Task.getTaskByID(rs.getInt("task_id")));
        }
        return tasks;
    }

    public void addTask(Task task) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        Database.executeStatement(String.format("INSERT INTO people_to_tasks (person_id, task_id) VALUES (%d, %d)", id, task.getId()));
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

        Person person = (Person) o;

        return id == person.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Person o) {
        return this.id - o.id;
    }
}
