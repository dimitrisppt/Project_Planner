package armadillo.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PeoplePanel extends Stage{

    public PeoplePanel(){


        Label nameLabel = new Label("Enter Name: ");
        Label surnameLabel = new Label("Enter Surname: ");

        TextField nameField = new TextField();
        TextField surnameField = new TextField();

        nameField.setId("nameField");
        surnameField.setId("surnameField");
        nameField.setMaxSize(200,25);
        surnameField.setMaxSize(200,25);



        ObservableList<String> people = FXCollections.observableArrayList (
                "Nick", "Eric");
        ListView<String> listOfPeople = new ListView<String>(people);
        listOfPeople.setCellFactory(param -> new peopleCell());



        this.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        HBox nameHBox = new HBox();
        HBox surnameHBox = new HBox();

        nameHBox.setSpacing(30);
        surnameHBox.setSpacing(15);

        nameHBox.getChildren().addAll(nameLabel, nameField);
        surnameHBox.getChildren().addAll(surnameLabel, surnameField);

        dialogVbox.setPadding(new Insets(25,25,25,25));
        dialogVbox.getChildren().add(nameHBox);
        dialogVbox.getChildren().add(surnameHBox);
        dialogVbox.getChildren().add(listOfPeople);



        Scene dialogScene = new Scene(dialogVbox, 500, 500);
        this.setScene(dialogScene);
        this.show();


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
