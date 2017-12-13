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

/**
 * This is the controller for the TaskPanel
 */
public class TaskController {
    /**
     * The database connection
     */
    private Database database;

    /**
     * The TaskPanel
     */
    private TaskPanel tp;

    /**
     * All the resources checked on the TaskPanel
     */
    private Set<Resource> selectedResources;

    /**
     * All the people checked on the TaskPanel
     */
    private Set<Person> selectedPeople;

    /**
     * All the prerequisite Tasks that have been checked on the TaskPanel
     */
    private Set<Task> selectedPrerequisiteTasks;

    /**
     * Creates a new TaskController
     * @param database the Database
     */
    public TaskController(Database database) {
        this.database = database;
        tp = new TaskPanel(this);
        selectedResources = new TreeSet<>();
        selectedPeople = new TreeSet<>();
        selectedPrerequisiteTasks = new TreeSet<>();
        update();
    }

    /**
     * Deletes a task from the database
     * @param t the task to delete
     */
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

    /**
     * Adds a new task to the database
     * @param name The name of the task
     * @param desc The description of the database
     * @param effort_hh the effort estimate hh
     * @param effort_mm the effort estimate mm
     * @param dateTime the start dateTime of the task (in epoch seconds)
     */
    public void add(String name, String desc, int effort_hh, int effort_mm, long dateTime) {
        try {
            Pair<Boolean, String> valid = taskValid(selectedPrerequisiteTasks, selectedPeople, dateTime, effort_hh * 60 * 60 + effort_mm * 60);
            if (!valid._1) throw new IllegalArgumentException(valid._2);
            Task t = new Task(name, desc, effort_hh * 60 * 60 + effort_mm * 60, dateTime, database);
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

    /**
     * A simple pair class
     * @param <T> The first type
     * @param <U> The second type
     */
    public static class Pair<T, U> {
        /**
         * The first element in the pair
         */
        public T _1;

        /**
         * The second element in the pair
         */
        public U _2;

        /**
         * Creates a new pair
         * @param arg1 the first element in the pair
         * @param arg2 the second element in the pair
         */
        public Pair(T arg1, U arg2) {
            this._1 = arg1;
            this._2 = arg2;
        }
    }

    /**
     * Checks whether a proposed task is valid
     * @param prereqTasks All the prerequisite tasks
     * @param people All the people assigned to the task
     * @param startDateTime The start time of the task
     * @param effortEstimate the effort estimate of the task
     * @return (true, null) if valid, (false, message) otherwise
     * @throws SQLException If there is an error with the database
     * @throws ClassNotFoundException If SQLite plugin is not found
     * @throws ElementDoesNotExistException Should not happen
     */
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
                if ((t_end.isAfter(start) && t_end.isBefore(end))
                        || (t_start.isAfter(start) && t_start.isBefore(end))
                        || (start.isAfter(t_start) && start.isBefore(t_end))
                        || (end.isAfter(t_start) && end.isBefore(t_end))) return new Pair<Boolean, String>(false, "People can only work on one task at a time");
            }
        }
        return new Pair<Boolean, String>(true, null);
    }

    /**
     * Updates the resources on the task panel
     */
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

    /**
     * Updates the people on the task panel
     */
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

    /**
     * Updates the tasks on the task panel
     */
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

    /**
     * Updates everything on the task panel
     */
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

    /**
     * Select a resource
     * @param r the Resource to add
     */
    public void addSelectedResource(Resource r) {
        selectedResources.add(r);
    }

    /**
     * Deselect a resource
     * @param r the Resource to remove
     */
    public void removeSelectedResource(Resource r) {
        selectedResources.remove(r);
    }

    /**
     * Select a person
     * @param p the Person to add
     */
    public void addSelectedPerson(Person p) {
        selectedPeople.add(p);
    }

    /**
     * Deselect a person
     * @param p the Person to remove
     */
    public void removeSelectedPerson(Person p) {
        selectedPeople.remove(p);
    }

    /**
     * Select a task
     * @param t the Task to select
     */
    public void addSelectedPrerequisiteTask(Task t) {
        selectedPrerequisiteTasks.add(t);
    }

    /**
     * Deselect a Task
     * @param t the Task to remove
     */
    public void removeSelectedPrerequisiteTask(Task t) {
        selectedPrerequisiteTasks.remove(t);
    }

    /**
     * Gets the TaskPanel
     * @return the TaskPanel
     */
    public TaskPanel getTp() {
        return tp;
    }
}
