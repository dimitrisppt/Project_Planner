package armadillo.views;

import armadillo.controllers.ResourceController;
import armadillo.models.ElementDoesNotExistException;
import armadillo.models.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Set;

/**
 * The window in which a user can view, add, and delete resources which can be used in tasks
 */
public class ResourcesPanel extends Stage {
    
    /**
	 * Controller used to interact with database
	 */
	private final ResourceController resourceController;
	
    /**
	 * List of resources currently in the system, which is displayed
	 */
    private ObservableList<Resource> resources;
    
    /**
	 * This takes user input for the name of the resource
	 */
    private TextField resourceField;

    /**
	 * This initialises UI elements within the window
	 * @param resourceController The controller used to interact with the database
	 */
    public ResourcesPanel(ResourceController resourceController) {
        this.resourceController = resourceController;


        Label resourceLabel = new Label("Enter Resource: ");


        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            resourceController.add(this.getNewResourceText());
        });
        submit.setId("submitResourceButton");
        submit.setStyle("-fx-font-weight: bold");

        BorderPane buttonPane = new BorderPane();
        buttonPane.setRight(submit);

        resourceField = new TextField();
        resourceField.setId("resourceField");
        resourceField.setMaxSize(200,25);

        resources = FXCollections.observableArrayList ();
        ListView<Resource> listOfResources = new ListView<>(resources);
        listOfResources.setCellFactory(param -> new resourcesCell(resourceController));


        this.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(25,25,25,25));
        dialogVbox.getChildren().add(resourceLabel);
        dialogVbox.getChildren().add(resourceField);
        dialogVbox.getChildren().add(listOfResources);
        dialogVbox.getChildren().add(buttonPane);



        Scene dialogScene = new Scene(dialogVbox, 500, 500);
        this.setScene(dialogScene);
    }

    /**
	 * Updates list of resources to be displayed
	 * @param resources The set of resources to be displayed
	 */
    public void updateResources(Set<Resource> resources) {
        this.resources.setAll(resources);
    }

    /**
	 * This returns the user input for the name of the resource
	 */
    public String getNewResourceText() {
        return resourceField.getText();
    }

    /**
	 * Resets the text field
	 */
    public void clearNewResourceText() {
        resourceField.clear();
    }

    /**
	 * Defines a custom cell to be used in the list component displaying the resources currently available
	 */
    static class resourcesCell extends ListCell<Resource> {

        /**
    	 * HBox used to format the cell contents
    	 */
        private HBox hbox = new HBox();
        
        /**
    	 * Button used to delete resources from the database
    	 */
        private Button button = new Button("Delete");
        
        /**
    	 * Label used to display resource name
    	 */
        private Label label = new Label("");

        /**
    	 * Initialises UI elements
    	 * @param resourceController Controller used to interact with the database
    	 */
        public resourcesCell(ResourceController resourceController){

            super();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(label, button);
            button.setOnAction(event -> {resourceController.delete(this.getItem());});
        }

        @Override
        /**
    	 * This sets values for each element within the cell whenever a cell update is required
    	 * @param item The resource to be displayed that has been fetched from the database
    	 * @param empty Whether not the cell should be empty
    	 */
        public void updateItem(Resource item, boolean empty) {

            super.updateItem(item, empty);

            if (empty) {

                setText(null);
                setGraphic(null);
            } else {
                try {
                    label.setText(item.getName());
                } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e ) {
                    ExceptionAlert ea = new ExceptionAlert(e);
                    ea.show();
                }
                setGraphic(hbox);
            }
        }

    }



}
