package armadillo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ResourcesPanel {

    private Button resourcesButton;

    public ResourcesPanel(Button resourcesButton) {

        this.resourcesButton = resourcesButton;

        // ------------- Change to MVC ---------------
        resourcesButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Label recourseLabel = new Label("Enter Resource: ");

                TextField recourseField = new TextField();
                recourseField.setMaxSize(200,25);

                ObservableList<String> resource = FXCollections.observableArrayList (
                        "Resource #1", "Resource #2");
                ListView<String> listOfResources = new ListView<String>(resource);
                listOfResources.setCellFactory(param -> new resourcesCell());

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                VBox dialogVbox = new VBox(20);
                dialogVbox.setPadding(new Insets(25,25,25,25));
                dialogVbox.getChildren().add(recourseLabel);
                dialogVbox.getChildren().add(recourseField);
                dialogVbox.getChildren().add(listOfResources);

                Scene dialogScene = new Scene(dialogVbox, 500, 500);
                dialog.setScene(dialogScene);
                dialog.show();
            }

        });

    }

    static class resourcesCell extends ListCell<String> {
        HBox hbox = new HBox();
        Button button = new Button("Delete");
        Label label = new Label("");

        public resourcesCell(){
            super();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(label, button);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(item);
                setGraphic(hbox);
            }
        }

    }



}
