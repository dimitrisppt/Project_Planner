package armadillo.controllers;

import armadillo.models.*;
import armadillo.views.ExceptionAlert;
import armadillo.views.InvalidInputAlert;
import armadillo.views.ResourcesPanel;
import armadillo.views.TaskPanel;
import armadillo.views.View;

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

//    public void add(String newResourceText) {
//        try {
//            new Resource(rp.getNewResourceText(), database);
//        } catch (SQLException | ClassNotFoundException e) {
//            ExceptionAlert ea = new ExceptionAlert(e);
//            ea.showAndWait();
//        } catch (IllegalArgumentException e) {
//            InvalidInputAlert iia = new InvalidInputAlert(e);
//            iia.showAndWait();
//        }
//        System.out.println("Add: " + newResourceText);
//        update();
//        rp.clearNewResourceText();
//    }

    public void add(String name, String desc, int hh, int mm) {
        try {
            Task t = new Task(name, desc, hh * 60 * 60 + mm * 60, database);
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
        tp.clear();
        update();
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
