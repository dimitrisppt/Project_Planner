package armadillo.controllers;

import armadillo.models.Database;
import armadillo.models.Resource;
import armadillo.views.ExceptionAlert;
import armadillo.views.InvalidInputAlert;
import armadillo.views.ResourcesPanel;

import java.sql.SQLException;
import java.util.TreeSet;

/**
 * The controller for the ResourcesPanel
 */
public class ResourceController {
    /**
     * THe database connection
     */
    private Database database;

    /**
     * The ResourcesPanel
     */
    private ResourcesPanel rp;

    /**
     * Creates a new ResourceController
     * @param database The Database
     */
    public ResourceController(Database database) {
        this.database = database;
        rp = new ResourcesPanel(this);
        update();
    }

    /**
     * Deletes a Resource form the database
     * @param r the Resource to delete
     */
    public void delete(Resource r) {
        try {
            r.delete();
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("Delete");
        update();
    }

    /**
     * Adds a new Resource to the database
     * @param newResourceText the name of the Resource
     */
    public void add(String newResourceText) {
        try {
            new Resource(rp.getNewResourceText(), database);
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        } catch (IllegalArgumentException e) {
            InvalidInputAlert iia = new InvalidInputAlert(e);
            iia.showAndWait();
        }
        System.out.println("Add: " + newResourceText);
        update();
        rp.clearNewResourceText();
    }

    /**
     * Updates the ResourcesPanel
     */
    public void update() {
        try {
            TreeSet<Resource> allResources = Resource.getAllResources(database);
            rp.updateResources(allResources);
        } catch (SQLException | ClassNotFoundException e) {
            ExceptionAlert ea = new ExceptionAlert(e);
            ea.showAndWait();
        }
        System.out.println("update");
    }

    /**
     * Gets the ResourcesPanel
     * @return the ResourcesPanel
     */
    public ResourcesPanel getRp() {
        return rp;
    }
}
