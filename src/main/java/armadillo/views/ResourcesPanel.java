package armadillo.views;

import armadillo.controllers.ResourceController;
import armadillo.models.ElementDoesNotExistException;
import armadillo.models.Resource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Set;

public class ResourcesPanel extends Stage {
    private final ResourceController resourceController;
    private ObservableList<Resource> resources;
    private TextField resourceField;

    public ResourcesPanel(ResourceController resourceController) {
        this.resourceController = resourceController;


        Label resourceLabel = new Label("Enter Resource: ");


        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
            resourceController.add(this.getNewResourceText());
        });
        submit.setId("submitResourceButton");
        submit.setStyle("-fx-font-weight: bold");

        Button close = new Button("Close");
        close.setOnAction(event -> {this.close();});
        close.setId("closeButton");
        close.setStyle("-fx-font-weight: bold");

        BorderPane buttonPane = new BorderPane();
        buttonPane.setLeft(submit);
        buttonPane.setRight(close);

        resourceField = new TextField();
        resourceField.setId("resourceField");
        resourceField.setMaxSize(200,25);

        HBox resourceHBox = new HBox();
        resourceHBox.setSpacing(15);
        resourceHBox.getChildren().addAll(resourceLabel, resourceField);

        resources = FXCollections.observableArrayList ();
        ListView<Resource> listOfResources = new ListView<>(resources);
        listOfResources.setCellFactory(param -> new resourcesCell(resourceController));
        listOfResources.setId("listResources");


        this.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(25,25,25,25));
        dialogVbox.getChildren().add(resourceHBox);
        dialogVbox.getChildren().add(listOfResources);
        dialogVbox.getChildren().add(buttonPane);



        Scene dialogScene = new Scene(dialogVbox, 500, 500);
        this.setScene(dialogScene);
    }

    public void updateResources(Set<Resource> resources) {
        this.resources.setAll(resources);
    }

    public String getNewResourceText() {
        return resourceField.getText();
    }

    public void clearNewResourceText() {
        resourceField.clear();
    }

    static class resourcesCell extends ListCell<Resource> {

        private HBox hbox = new HBox();
        private Pane pane = new Pane();

        private Button button = new Button("Delete");
        private Label label = new Label("");

        public resourcesCell(ResourceController resourceController){

            super();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(label, pane, button);
            hbox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> {resourceController.delete(this.getItem());});


        }

        @Override
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
