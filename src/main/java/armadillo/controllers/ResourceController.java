package armadillo.controllers;

import armadillo.models.Database;
import armadillo.models.Resource;
import armadillo.views.ExceptionAlert;
import armadillo.views.InvalidInputAlert;
import armadillo.views.ResourcesPanel;

import java.sql.SQLException;
import java.util.TreeSet;

public class ResourceController {
    private Database database;
    private ResourcesPanel rp;

    public ResourceController(Database database) {
        this.database = database;
        rp = new ResourcesPanel(this);
        update();
    }

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

    public ResourcesPanel getRp() {
        return rp;
    }
}
