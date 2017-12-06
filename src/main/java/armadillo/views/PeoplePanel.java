package armadillo.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PeoplePanel {

    private Button peopleButton;
    private ActionEvent event;

    public PeoplePanel(Button peopleButton){

        this.peopleButton = peopleButton;

        peopleButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    Label nameLabel = new Label("Enter Name: ");


                    TextField nameField = new TextField();
                    nameField.setId("nameField");
                    nameField.setMaxSize(200,25);



                    ObservableList<String> people = FXCollections.observableArrayList (
                            "Nick", "Eric");
                    ListView<String> listOfPeople = new ListView<String>(people);
                    listOfPeople.setCellFactory(param -> new peopleCell());



                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.setPadding(new Insets(25,25,25,25));
                    dialogVbox.getChildren().add(nameLabel);
                    dialogVbox.getChildren().add(nameField);
                    dialogVbox.getChildren().add(listOfPeople);



                    Scene dialogScene = new Scene(dialogVbox, 500, 500);
                    dialog.setScene(dialogScene);
                    dialog.show();
                }

            });

    }


    static class peopleCell extends ListCell<String> {

        HBox hbox = new HBox();
        Pane pane = new Pane();

        Button button = new Button("Delete");
        Label label = new Label("");

        public peopleCell(){

            super();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(label, pane, button);
            hbox.setHgrow(pane, Priority.ALWAYS);

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
