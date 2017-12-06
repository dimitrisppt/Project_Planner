package armadillo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TaskPanel extends Stage {


    public TaskPanel() {

        Button submit = new Button("Submit");

        submit.setId("submitButton");

        submit.setStyle("-fx-font-weight: bold");

        Label personInstructions = new Label("Select people to assign to task.");
        Label taskInstructions = new Label("Select prerequisite tasks or delete pre-existing tasks.");
        Label resourcesInstructions = new Label("Enter required resources.");

        Label blankLabel = new Label();
        TextField resourcesField = new TextField();

        Label taskLabel = new Label("Task Name:");
        taskLabel.setStyle("-fx-font-weight: bold");

        resourcesField.setId("resourcesField");

        // taskLabel.setAlignment(Pos.TOP_LEFT);

        TextField taskField = new TextField();
        //taskField.setAlignment(Pos.TOP_CENTER);
        taskField.setMaxSize(200,25);

        taskField.setId("taskField");

        Label taskDescription = new Label("Task Description:");
        taskDescription.setStyle("-fx-font-weight: bold");


        TextArea descriptionArea = new TextArea();
        descriptionArea.setWrapText(true);
        descriptionArea.setMaxSize(200,50);
        descriptionArea.setId("taskDescArea");


        Label spinnerLabelHours = new Label("Hours:");
        Label spinnerLabelMins = new Label("Minutes:");

        Spinner<Integer> spinnerHours = new Spinner<Integer>();
        int initialValueHours = 1;
        SpinnerValueFactory<Integer> valueFactoryHours = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, initialValueHours);

        Spinner<Integer> spinnerMins = new Spinner<Integer>();
        int initialValueMins = 0;

        SpinnerValueFactory<Integer> valueFactoryMins = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, initialValueMins);


        spinnerHours.setValueFactory(valueFactoryHours);
        spinnerMins.setValueFactory(valueFactoryMins);
        spinnerHours.setMaxWidth(50);
        spinnerMins.setMaxWidth(50);

        Label effortLabel = new Label("Effort Estimate:");
        effortLabel.setStyle("-fx-font-weight: bold");
        HBox spinnerHBox = new HBox(10);
        spinnerHBox.setPadding(new Insets(25,25,25,25));
        spinnerHBox.setPadding(new Insets(0, 0, 25, 0));
        spinnerHBox.setAlignment(Pos.CENTER);
        spinnerHBox.getChildren().add(effortLabel);
        spinnerHBox.getChildren().addAll(spinnerLabelHours, spinnerHours);
        spinnerHBox.getChildren().addAll(spinnerLabelMins, spinnerMins);


        ObservableList<String> people = FXCollections.observableArrayList (
                "Person #1", "Person #2");
        ListView<String> listOfPeople = new ListView<>(people);
        listOfPeople.setCellFactory(param -> new checkBoxCell());


        ObservableList<String> items = FXCollections.observableArrayList (
                "Task #1", "Task #2", "Task #3");
        ListView<String> listOfTasks = new ListView<String>(items);
        listOfTasks.setCellFactory(param -> new taskCell());

        ObservableList<String> resources = FXCollections.observableArrayList (
                "Resource #1", "Resource #2", "Resource #3");
        ListView<String> listOfResources = new ListView<String>(resources);
        listOfResources.setCellFactory(param -> new resourcesCell());



        this.initModality(Modality.APPLICATION_MODAL);
        HBox dialogHbox = new HBox(20);
        dialogHbox.setPadding(new Insets(25,25,25,25));
        BorderPane borderPane = new BorderPane();

        VBox peopleVbox = new VBox();
        peopleVbox.getChildren().add(personInstructions);
        //peopleVbox.getChildren().add(blankLabel);
        peopleVbox.setSpacing(25);
        peopleVbox.getChildren().add(listOfPeople);

        VBox taskVbox = new VBox();
        taskVbox.getChildren().add(taskInstructions);
        //taskVbox.getChildren().add(blankLabel);
        taskVbox.setSpacing(25);
        taskVbox.setPadding(new Insets(0,10,0,10));
        taskVbox.getChildren().add(listOfTasks);

        VBox resourcesVbox = new VBox();
        resourcesVbox.getChildren().add(resourcesInstructions);
        resourcesVbox.getChildren().add(resourcesField);
        resourcesVbox.getChildren().add(listOfResources);

        dialogHbox.setAlignment(Pos.CENTER);

        dialogHbox.getChildren().add(taskLabel);
        dialogHbox.getChildren().add(taskField);
        dialogHbox.getChildren().add(taskDescription);
        dialogHbox.getChildren().add(descriptionArea);

        VBox headerVBox = new VBox();
        headerVBox.getChildren().add(dialogHbox);
        headerVBox.getChildren().add(spinnerHBox);


        BorderPane buttonPane = new BorderPane();
        buttonPane.setRight(submit);
        borderPane.setBottom(buttonPane);

        borderPane.setLeft(peopleVbox);
        borderPane.setCenter(taskVbox);
        borderPane.setRight(resourcesVbox);

        borderPane.setTop(headerVBox);

        borderPane.setPadding(new Insets(25,25,25,25));

        Scene dialogScene = new Scene(borderPane, 850, 500);
        this.setScene(dialogScene);

    }

    static class taskCell extends ListCell<String> {

        HBox hbox = new HBox();


        CheckBox checkbox = new CheckBox();
        Button button = new Button("Delete");
        Label label = new Label("");

        public taskCell(){
            super();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(checkbox, label, button);
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

    static class checkBoxCell extends ListCell<String> {

        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item);

                CheckBox checkbox = new CheckBox();
                setGraphic(checkbox);
            }
        }
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
