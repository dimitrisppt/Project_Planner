package armadillo.controllers;

import armadillo.models.Database;
import armadillo.models.Person;
import armadillo.models.Resource;
import armadillo.views.ExceptionAlert;
import armadillo.views.InvalidInputAlert;
import armadillo.views.PeoplePanel;
import armadillo.views.ResourcesPanel;

import java.sql.SQLException;
import java.util.TreeSet;

/**
 * The controller for the people view
 */
public class PeopleController {
    /**
     * The database connection
     */
    private Database database;
    /**
     * The PeoplePanel
     */
    private PeoplePanel pp;

    /**
     * Creates a new controller
     * @param database The database
     */
    public PeopleController(Database database) {
        this.database = database;
        pp = new PeoplePanel(this);
        update();
    }

    /**
     * Deletes a person from the databse
     * @param p the person to delete
     */
    public void delete(Person p) {
        try {
            p.delete();
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("Delete");
        update();
    }

    /**
     * Creates a new Person
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     */
    public void add(String firstName, String lastName) {
        try {
            new Person(firstName, lastName, database);
            System.out.println("Add: " + firstName + " " + lastName);
            update();
            pp.clearText();
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        } catch (IllegalArgumentException e) {
            InvalidInputAlert iia = new InvalidInputAlert(e);
            iia.showAndWait();
        }

    }

    /**
     * Updates the PeoplePanel
     */
    public void update() {
        try {
            TreeSet<Person> people = Person.getAllPeople(database);
            pp.updatePeople(people);
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("update");
    }

    /**
     * Gets the PeoplePanel
     * @return the PeoplePanel
     */
    public PeoplePanel getPp() {
        return pp;
    }
}
