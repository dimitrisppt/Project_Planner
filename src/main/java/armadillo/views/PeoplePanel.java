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

/**
 * The window in which a user can view, add, and delete people who can be assigned to tasks
 */
public class PeoplePanel extends Stage{
	
    /**
	 * Controller used to interact with database 
	 */
    private final PeopleController peopleController;
    
    /**
	 * This takes user input for the first name of the person
	 */
    private TextField nameField;
    
    /**
	 * This takes user input for the last name of the person
	 */
    private TextField surnameField;
    
    /**
	 * List of people currently in the system, which is displayed
	 */
    private ObservableList<Person> people;

    /**
	 * Initialises UI components
	 * @param peopleController Controller used to interact with the database
	 */
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

    /**
	 * Resets the text fields
	 */
    public void clearText() {
        nameField.clear();
        surnameField.clear();
    }

    /**
	 * This returns the user input of the persons first name
	 */
    public String getFirstNameEntry() {
        return nameField.getText();
    }

    /**
	 * This returns the user input of the persons last name
	 */
    public String getLastNameEntry() {
        return surnameField.getText();
    }

    /**
	 * Updates list of people to be displayed
	 * @param people The set of people to be displayed
	 */  
    public void updatePeople(Set<Person> people) {
        this.people.setAll(people);
    }

    /**
	 * Defines a custom cell to be used in the list component displaying the people currently in the system
	 */
    static class peopleCell extends ListCell<Person> {

        /**
    	 * HBox used for formatting cell
    	 */
        HBox hbox = new HBox();
        
        /**
    	 * Pane used to hold cell contents
    	 */
        Pane pane = new Pane();

        /**
    	 * Button used to delete people
    	 */
        Button button = new Button("Delete");
        
        /**
    	 * Label used to display persons name
    	 */
        Label label = new Label("");

        /**
    	 * Initialises cell UI components
    	 * @param peopleController Controller used to interact with the database
    	 */
        public peopleCell(PeopleController peopleController){

            super();
            hbox.setSpacing(5);
            hbox.getChildren().addAll(label, pane, button);
            hbox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> {peopleController.delete(getItem());});

        }

        @Override
        /**
    	 * This sets values for each element within the cell whenever a cell update is required
    	 * @param item The people to be displayed that has been fetched from the database
    	 * @param empty Whether not the cell should be empty
    	 */
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
