package armadillo.controllers;

import armadillo.models.Database;

import java.io.File;
import java.sql.SQLException;

public class CreateDatabase {
    public static void createDatabase(String url) throws SQLException, ClassNotFoundException {
        String[] split_url = url.split(":");
        if (split_url.length != 3) throw new IllegalArgumentException("URL not valid");
        File file = new File(split_url[2]);
        if (file.exists()) return;
        Database database = new Database(url);
        database.executeStatement("CREATE TABLE tasks (" +
                "ID integer PRIMARY KEY AUTOINCREMENT," +
                "name varchar(255) NOT NULL," +
                "description text," +
                "effort_estimate integer NOT NULL," +
                "date_time integer" +
        ");");

        database.executeStatement("CREATE TABLE prereq_tasks (" +
                "ID integer PRIMARY KEY AUTOINCREMENT," +
                "this_task_id integer NOT NULL," +
                "prereq_task_id integer NOT NULL," +
                "FOREIGN KEY(this_task_id) REFERENCES tasks(ID) ON DELETE CASCADE, " +
                "FOREIGN KEY(prereq_task_id) REFERENCES tasks(ID) ON DELETE CASCADE" +
        ");");

        database.executeStatement("CREATE TABLE people (" +
                "ID integer PRIMARY KEY AUTOINCREMENT," +
                "first_name varchar(30) NOT NULL," +
                "last_name varchar(30) NOT NULL" +
        ");");

        database.executeStatement("CREATE TABLE people_to_tasks (" +
                "ID integer PRIMARY KEY AUTOINCREMENT," +
                "person_id integer NOT NULL," +
                "task_id integer NOT NULL," +
                "FOREIGN KEY(person_id) REFERENCES people(ID) ON DELETE CASCADE," +
                "FOREIGN KEY(task_id) REFERENCES tasks(ID) ON DELETE CASCADE" +
        ");");

        database.executeStatement("CREATE TABLE resources (" +
                "ID integer PRIMARY KEY AUTOINCREMENT," +
                "name varchar(255) NOT NULL);");

        database.executeStatement("CREATE TABLE resource_to_task (" +
                "ID integer PRIMARY KEY AUTOINCREMENT," +
                "resource_id integer NOT NULL," +
                "task_id integer NOT NULL," +
                "FOREIGN KEY(resource_id) REFERENCES resources(ID) ON DELETE CASCADE," +
                "FOREIGN KEY(task_id) REFERENCES tasks(ID) ON DELETE CASCADE);");
    }
}
