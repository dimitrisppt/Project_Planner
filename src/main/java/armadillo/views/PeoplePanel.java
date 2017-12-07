package armadillo.views;

import armadillo.controllers.PeopleController;
import armadillo.models.ElementDoesNotExistException;
import armadillo.models.Person;
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
import java.util.TreeSet;

public class PeoplePanel extends Stage{
    private final PeopleController peopleController;
    private TextField nameField;
    private TextField surnameField;
    private ObservableList<Person> people;

    public PeoplePanel(PeopleController peopleController) {
        this.peopleController = peopleController;


        Label nameLabel = new Label("Enter Name: ");
        Label surnameLabel = new Label("Enter Surname: ");

        nameField = new TextField();
        surnameField = new TextField();

        Button submit = new Button("Submit");
        submit.setOnAction(event -> {peopleController.add(getFirstNameEntry(), getLastNameEntry());});
        submit.setId("submitPeopleButton");
        submit.setStyle("-fx-font-weight: bold");

        BorderPane buttonPane = new BorderPane();
        buttonPane.setRight(submit);

        nameField.setId("nameField");
        surnameField.setId("surnameField");
        nameField.setMaxSize(200,25);
        surnameField.setMaxSize(200,25);


        people = FXCollections.observableArrayList ();
        ListView<Person> listOfPeople = new ListView<>(people);
        listOfPeople.setCellFactory(param -> new peopleCell(peopleController));



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
        dialogVbox.getChildren().add(buttonPane);

        Scene dialogScene = new Scene(dialogVbox, 500, 500);
        this.setScene(dialogScene);
    }

    public void clearText() {
        nameField.clear();
        surnameField.clear();
    }

    public String getFirstNameEntry() {
        return nameField.getText();
    }

    public String getLastNameEntry() {
        return surnameField.getText();
    }

    public void updatePeople(Set<Person> people) {
        this.people.setAll(people);
    }


    static class peopleCell extends ListCell<Person> {

        HBox hbox = new HBox();
        Pane pane = new Pane();

        Button button = new Button("Delete");
        Label label = new Label("");

        public peopleCell(PeopleController peopleController){

            super();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(label, pane, button);
            hbox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> {peopleController.delete(getItem());});

        }

        @Override
        public void updateItem(Person item, boolean empty) {

            super.updateItem(item, empty);
            if (empty) {

                setText(null);
                setGraphic(null);
            } else {
                try {
                    label.setText(item.getFullName());
                } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e) {
                    ExceptionAlert ea = new ExceptionAlert(e);
                    ea.show();
                }
                setGraphic(hbox);
            }
        }

    }


}
