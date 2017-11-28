package armadillo.models;

import javax.sql.rowset.CachedRowSet;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TaskToTime implements Comparable<TaskToTime>{
    private int id;
    public final static String TABLE_NAME = "schedule";

    public TaskToTime(Task t, LocalDateTime dateTime) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        long timeSinceEpoch = dateTime.atZone(ZoneOffset.UTC).toEpochSecond();
        id = Database.executeInsertStatement(String.format("INSERT INTO %s (task_id, time) VALUES (%s, %s)", TABLE_NAME, t.getId(), timeSinceEpoch));
    }

    private TaskToTime(int id) {
        this.id = id;
    }

    public int getId() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        if (!exists()) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return id;
    }

    public static TaskToTime getFromID(int id) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        if (!exists(id)) throw new ElementDoesNotExistException(TABLE_NAME, id);
        return new TaskToTime(id);
    }

    public Task getTask() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT task_id FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) {
            throw new ElementDoesNotExistException(TABLE_NAME, id);
        } else {
            rs.beforeFirst();
            return Task.getTaskByID(rs.getInt("task_id"));
        }
    }

    public LocalDateTime getTime() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        CachedRowSet rs = Database.executeQuery(String.format("SELECT time FROM %s WHERE ID=%d", TABLE_NAME, id));
        if (!rs.next()) {
            throw new ElementDoesNotExistException(TABLE_NAME, id);
        } else {
            rs.beforeFirst();
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(rs.getLong("time")), ZoneOffset.UTC);
        }
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

        TaskToTime that = (TaskToTime) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(TaskToTime o) {
        return this.id - o.id;
    }
}
