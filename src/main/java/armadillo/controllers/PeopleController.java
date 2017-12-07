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

public class PeopleController {
    private Database database;
    private PeoplePanel pp;

    public PeopleController(Database database) {
        this.database = database;
        pp = new PeoplePanel(this);
        update();
    }

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

    public PeoplePanel getPp() {
        return pp;
    }
}
