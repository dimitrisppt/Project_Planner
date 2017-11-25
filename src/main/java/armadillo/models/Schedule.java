package armadillo.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

public class Schedule {
    public static TreeSet<TaskToTime> getCurrentSchedule() throws SQLException, ClassNotFoundException{
        ResultSet rs = Database.executeQuery(String.format("SELECT id FROM %s", "schedule"));
        TreeSet<TaskToTime> schedule = new TreeSet<>();
        try {
            while (rs.next()) {
                schedule.add(TaskToTime.getFromID(rs.getInt("ID")));
            }
        } catch (ElementDoesNotExistException e) {
            // This will never happen
        }
        return schedule;
    }

    public static TreeSet<TaskToTime> regenerateSchedule() throws SQLException, ClassNotFoundException {
        Database.executeStatement("DELETE FROM schedule");

        // Regenerate schedule here
        return getCurrentSchedule();
    }
}
