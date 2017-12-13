package armadillo.controllers;

import armadillo.models.*;
import armadillo.views.ExceptionAlert;
import armadillo.views.InvalidInputAlert;
import armadillo.views.ResourcesPanel;
import armadillo.views.TaskPanel;
import armadillo.views.View;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

public class TaskController {
    private Database database;
    private TaskPanel tp;
    private Set<Resource> selectedResources;
    private Set<Person> selectedPeople;
    private Set<Task> selectedPrerequisiteTasks;

    public TaskController(Database database) {
        this.database = database;
        tp = new TaskPanel(this);
        selectedResources = new TreeSet<>();
        selectedPeople = new TreeSet<>();
        selectedPrerequisiteTasks = new TreeSet<>();
        update();
    }

    public void delete(Task t) {
        try {
            t.delete();
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("Delete");
        update();
    }

    public void add(String name, String desc, int hh, int mm, long dateTime) {
        try {
            Pair<Boolean, String> valid = taskValid(selectedPrerequisiteTasks, selectedPeople, dateTime, hh * 60 * 60 + mm * 60);
            if (!valid._1) throw new IllegalArgumentException(valid._2);
            Task t = new Task(name, desc, hh * 60 * 60 + mm * 60, dateTime, database);
            for (Person p : selectedPeople) {
                t.addPerson(p);
            }
            for (Task prereq : selectedPrerequisiteTasks) {
                t.addPrerequisiteTask(prereq);
            }
            for (Resource r : selectedResources) {
                t.addResource(r);
            }
        } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        } catch (IllegalArgumentException e) {
            InvalidInputAlert iia = new InvalidInputAlert(e);
            iia.showAndWait();
        }
        selectedPeople.clear();
        selectedResources.clear();
        selectedPrerequisiteTasks.clear();
        tp.clear();
        update();
    }

    public static class Pair<T, U> {
        public T _1;
        public U _2;

        public Pair(T arg1, U arg2) {
            this._1 = arg1;
            this._2 = arg2;
        }
    }
    public static Pair<Boolean, String> taskValid(Set<Task> prereqTasks, Set<Person> people,
            long startDateTime, long effortEstimate) throws SQLException, ClassNotFoundException, ElementDoesNotExistException {

        LocalDateTime start = LocalDateTime.ofEpochSecond(startDateTime, 0, ZoneOffset.UTC);
        LocalDateTime end = LocalDateTime.ofEpochSecond(startDateTime + effortEstimate, 0, ZoneOffset.UTC);
        if (start.isBefore(LocalDateTime.now())) return new Pair<Boolean, String>(false, "Task must start in the future");
        for (Task t : prereqTasks) {
            if (start.isBefore(LocalDateTime.ofEpochSecond(t.getDateTime() +
                t.getEffortEstimate(), 0, ZoneOffset.UTC))) return new Pair<Boolean, String>(false, "Task must start after prerequisite tasks have finished");
        }
        for (Person p : people) {
            for (Task t : p.getTasks()) {
                LocalDateTime t_start = LocalDateTime.ofEpochSecond(t.getDateTime(), 0, ZoneOffset.UTC);
                LocalDateTime t_end = LocalDateTime.ofEpochSecond(t.getDateTime() + t.getEffortEstimate(), 0, ZoneOffset.UTC);
                if (!start.isAfter(t_end) || !end.isBefore(t_start)) return new Pair<Boolean, String>(false, "People can only work on one task at a time");
            }
        }
        return new Pair<Boolean, String>(true, null);
    }

    public void updateResources() {
        try {
            TreeSet<Resource> allResources = Resource.getAllResources(database);
            tp.updateResources(allResources);
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("update");
    }

    public void updatePeople() {
        try {
            TreeSet<Person> people = Person.getAllPeople(database);
            tp.updatePeople(people);
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("update");
    }

    public void updateTasks() {
        try {
            TreeSet<Task> tasks = Task.getAllTasks(database);
            tp.updateTasks(tasks);
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("update");
    }

    public void update() {
        updateResources();
        updatePeople();
        updateTasks();
    }

    public void updateSchedule(View view) {
    	 try {
             TreeSet<Task> tasks = Task.getAllTasks(database);
             view.updateTasks(tasks);
         } catch (SQLException | ClassNotFoundException e) {
             ExceptionAlert ea = new ExceptionAlert(e);
             ea.showAndWait();
         }
         System.out.println("update");
    }

    public void addSelectedResource(Resource r) {
        selectedResources.add(r);
    }

    public void removeSelectedResource(Resource r) {
        selectedResources.remove(r);
    }

    public void addSelectedPerson(Person p) {
        selectedPeople.add(p);
    }

    public void removeSelectedPerson(Person p) {
        selectedPeople.remove(p);
    }

    public void addSelectedPrerequisiteTask(Task t) {
        selectedPrerequisiteTasks.add(t);
    }

    public void removeSelectedPrerequisiteTask(Task t) {
        selectedPrerequisiteTasks.remove(t);
    }

    public TaskPanel getTp() {
        return tp;
    }
}
