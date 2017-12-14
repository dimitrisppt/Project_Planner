package armadillo.views;

import armadillo.controllers.TaskController;
import armadillo.models.ElementDoesNotExistException;
import armadillo.models.Person;
import armadillo.models.Resource;
import armadillo.models.Task;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

/**
 * The window in which a user can input details used to create and add a new task to the database
 */
public class TaskPanel extends Stage {
    
	/**
	 * A controller used to interact with teh database of tasks
	 */
	private final TaskController taskController;
   
	/**
	 * A list of resources in the database
	 */
	private ObservableList<Resource> resources;
    
	/**
	 * A list of people in the database
	 */
	private ObservableList<Person> people;
    
	/**
	 * A list of tasks in the database
	 */
	private ObservableList<Task> tasks;
    
	/**
	 * Spinner used to input the effort estimate of a task
	 */
	private Spinner<Integer> spinnerHours;
    
	/**
	 * Spinner used to input the effort estimate of a task
	 */
	private Spinner<Integer> spinnerMins;
	
	/**
	 * This takes user input for the name of the task
	 */
    private TextField taskField;
    
    /**
	 * This takes user input for the description of the task
	 */
    private TextArea descriptionArea;
    
    /**
	 * This allows a user to select a date on which a task should be started
	 */
    private DatePicker dateSelection;
    
    /**
	 * Spinner used to input the start time of a task
	 */
    private Spinner<Integer> spinnerTimeHours;
    
    /**
	 * Spinner used to input the start time of a task
	 */
    private Spinner<Integer> spinnerTimeMins;

    /**
	 * This initialises UI elements
	 * @param taskController Controller used to interact with the database
	 */
    public TaskPanel(TaskController taskController) {
        this.taskController = taskController;

        Button submit = new Button("Submit");
        submit.setOnAction(event -> {
        	taskController.add(getTaskName(), getTaskDescription(), getHours(), getMinutes(), getDateTime());
        	System.out.println("Selected date: " + getDateTime());
        });
        submit.setId("submitButton");
        submit.setStyle("-fx-font-weight: bold");

        Label personInstructions = new Label("Select people to assign to task.");
        Label taskInstructions = new Label("Select prerequisite tasks or delete pre-existing tasks.");
        Label resourcesInstructions = new Label("Select required resources.");


        Label taskLabel = new Label("Task Name:");
        taskLabel.setStyle("-fx-font-weight: bold");


        // taskLabel.setAlignment(Pos.TOP_LEFT);

        taskField = new TextField();
        //taskField.setAlignment(Pos.TOP_CENTER);
        taskField.setMaxSize(200,25);

        taskField.setId("taskField");

        Label taskDescription = new Label("Task Description:");
        taskDescription.setStyle("-fx-font-weight: bold");


        descriptionArea = new TextArea();
        descriptionArea.setWrapText(true);
        descriptionArea.setMaxSize(200,50);
        descriptionArea.setId("taskDescArea");


        Label spinnerLabelHours = new Label("Hours:");
        Label spinnerLabelMins = new Label("Minutes:");



        spinnerHours = new Spinner<Integer>();
        int initialValueHours = 1;
        SpinnerValueFactory<Integer> valueFactoryHours = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, initialValueHours);

        spinnerMins = new Spinner<Integer>();
        int initialValueMins = 0;

        SpinnerValueFactory<Integer> valueFactoryMins = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, initialValueMins);

        spinnerHours.setValueFactory(valueFactoryHours);
        spinnerMins.setValueFactory(valueFactoryMins);
        spinnerHours.setMaxWidth(70);
        spinnerMins.setMaxWidth(70);

        Label dateLabel	= new Label("Select start date:  ");
        dateSelection = new DatePicker(LocalDate.now());
        dateSelection.setEditable(false);

        spinnerTimeHours = new Spinner<Integer>();
        SpinnerValueFactory<Integer> valueFactorySelectHours = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        spinnerTimeHours.setValueFactory(valueFactorySelectHours);

        spinnerTimeMins = new Spinner<Integer>();
        SpinnerValueFactory<Integer> valueFactorySelectMins = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        spinnerTimeMins.setValueFactory(valueFactorySelectMins);

        spinnerTimeHours.setMaxWidth(70);
        spinnerTimeMins.setMaxWidth(70);

        Label select = new Label("Select start time (24hr clock):  ");
        Label select2 = new Label(" : ");
        select2.setStyle("-fx-font-weight: bold");

        Label effortLabel = new Label("Effort Estimate:");
        effortLabel.setStyle("-fx-font-weight: bold");
        HBox spinnerHBox = new HBox(10);
        spinnerHBox.setPadding(new Insets(25,25,25,25));
        spinnerHBox.setPadding(new Insets(0, 0, 25, 0));
        spinnerHBox.setAlignment(Pos.CENTER);
        spinnerHBox.getChildren().add(effortLabel);
        spinnerHBox.getChildren().addAll(spinnerLabelHours, spinnerHours);
        spinnerHBox.getChildren().addAll(spinnerLabelMins, spinnerMins);


        VBox dateTimeVBox = new VBox();
        dateTimeVBox.setAlignment(Pos.CENTER);
        dateTimeVBox.setPadding(new Insets(0, 0, 25, 0));
        dateTimeVBox.setSpacing(5);
        HBox dateHBox = new	HBox();
        dateHBox.setAlignment(Pos.CENTER);
        HBox timeHBox = new HBox();
        timeHBox.setAlignment(Pos.CENTER);

        dateHBox.getChildren().addAll(dateLabel, dateSelection);
        timeHBox.getChildren().addAll(select,spinnerTimeHours, select2, spinnerTimeMins);
        dateTimeVBox.getChildren().addAll(dateHBox, timeHBox);


        spinnerHBox.getChildren().addAll(dateTimeVBox);


        people = FXCollections.observableArrayList ();
        ListView<Person> listOfPeople = new ListView<>(people);
        listOfPeople.setCellFactory(param -> new peopleCell(taskController));


        tasks = FXCollections.observableArrayList ();
        ListView<Task> listOfTasks = new ListView<>(tasks);
        listOfTasks.setCellFactory(param -> new taskCell(taskController));

        resources = FXCollections.observableArrayList ();
        ListView<Resource> listOfResources = new ListView<>(resources);
        listOfResources.setCellFactory(param -> new resourcesCell(taskController));



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
//        resourcesVbox.getChildren().add(resourcesField);
        resourcesVbox.setSpacing(25);
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

    /**
	 * Updates list of resources displayed
	 * @param allResources The set of resources to be displayed
	 */
    public void updateResources(Set<Resource> allResources) {
        resources.setAll(allResources);
    }

    /**
	 * Updates list of people displayed
	 * @param allPeople The set of people to be displayed
	 */
    public void updatePeople(Set<Person> allPeople) {
        people.setAll(allPeople);
    }

    /**
	 * Updates list of tasks displayed
	 * @param allTasks The set of tasks to be displayed
	 */
    public void updateTasks(Set<Task> allTasks) {
        tasks.setAll(allTasks);
    }

    /**
	 * This returns the user input pertaining to the name of the task
	 */
    public String getTaskName() {
        return taskField.getText();
    }

    /**
	 * This returns the user input pertaining to the description of the task
	 */
    public String getTaskDescription() {
        return descriptionArea.getText();
    }

    /**
	 * This returns the user input pertaining to the hours of the effort estimate
	 */
    public int getHours() {
        return spinnerHours.getValue();
    }

    /**
	 * This returns the user input pertaining to the minutes of the effort estimate
	 */
    public int getMinutes() {
        return spinnerMins.getValue();
    }

    /**
	 * This returns the user input pertaining to the starting date and time of the task, converted to seconds from the epoch
	 */
    public long getDateTime() {
    	LocalDate dateTime = dateSelection.getValue();
    	return dateTime.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() + spinnerTimeHours.getValue()*60*60 + spinnerTimeMins.getValue()*60;
    }

    /**
	 * Resets input fields
	 */
    public void clear() {
        taskField.clear();
        descriptionArea.clear();
        spinnerHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, 1));
        spinnerMins.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300,1));
        resources.clear();
        people.clear();
        tasks.clear();
    }

    /**
	 * This defines a custom cell to be used in the list component displaying the previously entered tasks
	 */
    static class taskCell extends ListCell<Task> {

    	/**
    	 * Controller used to interact with the database
    	 */
        private TaskController taskController;
        
        /**
    	 * Checkbox used to mark a task as a prerequisite
    	 */
        private CheckBox checkbox;
        
        /**
    	 * Button used to delete a task
    	 */
        private Button button;
        
        /**
    	 * Label to display task name
    	 */
        private Label label;

        /**
    	 * This initialises the elements within the cell
    	 * @param taskController controller used to interact with the database
    	 */
        public taskCell(TaskController taskController){
            // super();
            this.taskController = taskController;
            button = new Button("Delete");
            label = new Label("");
            button.setOnAction(event -> {
                taskController.delete(getItem());
            });
        }

        @Override
        /**
    	 * This sets values for each element within the cell whenever a cell update is required
    	 * @param item The task to be displayed that has been fetched from the database
    	 * @param empty Whether not the cell should be empty
    	 */
        public void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                try {
                    label.setText(item.getName());
                } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e) {
                    ExceptionAlert ea = new ExceptionAlert(e);
                    ea.show();
                }

                checkbox = new CheckBox();
                checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        taskController.addSelectedPrerequisiteTask(getItem());
                    } else {
                        taskController.removeSelectedPrerequisiteTask(getItem());
                    }
                });

                HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.getChildren().addAll(checkbox, label, button);
                //label = new Label("");
                setGraphic(hbox);
            }
        }
    }

    /**
	 * This defines a custom cell to be used in the list component displaying the people who can be assigned to a task
	 */
    static class peopleCell extends ListCell<Person> {

        /**
    	 * Controller used to interact with database
    	 */
        private final TaskController taskController;
        
        /**
    	 * Checkbox used to mark a person as being assigned to a task
    	 */
        private CheckBox checkbox;

        /**
    	 * Assigns controller
    	 * @param taskController controller used
    	 */
        public peopleCell(TaskController taskController) {
            this.taskController = taskController;
        }
        
        /**
    	 * This sets values for each element within the cell whenever a cell update is required
    	 * @param item The person to be displayed that has been fetched from the database
    	 * @param empty Whether not the cell should be empty
    	 */
        public void updateItem(Person item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                try {
                    setText(item.getFullName());
                } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e) {
                    ExceptionAlert ea = new ExceptionAlert(e);
                    ea.show();
                }

                checkbox = new CheckBox();
                checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        taskController.addSelectedPerson(getItem());
                    } else {
                        taskController.removeSelectedPerson(getItem());
                    }
                });
                setGraphic(checkbox);
            }
        }
    }

    /**
	 * Defines a custom cell to be used in the list component displaying the resources which can be assigned to a task
	 */
    static class resourcesCell extends ListCell<Resource> {

        /**
    	 * Controller used to interact with the database
    	 */
        private final TaskController taskController;
        
        /**
    	 * Checkbox used to mark a resource as being assigned to a task
    	 */
        private CheckBox checkbox;

        /**
    	 * Assigns controller
    	 * @param taskController Controller to be used
    	 */
        public resourcesCell(TaskController taskController) {
            this.taskController = taskController;
        }

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
                    setText(item.getName());
                } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e) {
                    ExceptionAlert ea = new ExceptionAlert(e);
                    ea.show();
                }

                checkbox = new CheckBox();
                checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        taskController.addSelectedResource(getItem());
                    } else {
                        taskController.removeSelectedResource(getItem());
                    }
                });
                setGraphic(checkbox);
            }
        }
    }


}
