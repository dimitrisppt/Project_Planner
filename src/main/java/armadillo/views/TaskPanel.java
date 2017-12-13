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

public class TaskPanel extends Stage {
    private final TaskController taskController;
    private ObservableList<Resource> resources;
    private ObservableList<Person> people;
    private ObservableList<Task> tasks;
    private Spinner<Integer> spinnerHours;
    private Spinner<Integer> spinnerMins;
    private TextField taskField;
    private TextArea descriptionArea;
    private DatePicker dateSelection;
    private Spinner<Integer> spinnerTimeHours;
    private Spinner<Integer> spinnerTimeMins;

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

    public void updateResources(Set<Resource> allResources) {
        resources.setAll(allResources);
    }

    public void updatePeople(Set<Person> allPeople) {
        people.setAll(allPeople);
    }

    public void updateTasks(Set<Task> allTasks) {
        tasks.setAll(allTasks);
    }

    public String getTaskName() {
        return taskField.getText();
    }

    public String getTaskDescription() {
        return descriptionArea.getText();
    }

    public int getHours() {
        return spinnerHours.getValue();
    }

    public int getMinutes() {
        return spinnerMins.getValue();
    }

    public long getDateTime() {
    	LocalDate dateTime = dateSelection.getValue();
    	//return ((long)dateTime.getYear()*100000000 + (long)dateTime.getMonthValue()*1000000 + (long)dateTime.getDayOfMonth()*10000 + (long)spinnerTimeHours.getValue() * 100 + (long)spinnerTimeMins.getValue());
    	return dateTime.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() + spinnerTimeHours.getValue()*60*60 + spinnerTimeMins.getValue()*60;

    }

    public void clear() {
        taskField.clear();
        descriptionArea.clear();
        spinnerHours.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, 1));
        spinnerMins.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300,1));
        resources.clear();
        people.clear();
        tasks.clear();
    }

    static class taskCell extends ListCell<Task> {


        private TaskController taskController;
        private CheckBox checkbox;
        private Button button;
        private Label label;

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

    static class peopleCell extends ListCell<Person> {

        private final TaskController taskController;
        private CheckBox checkbox;

        public peopleCell(TaskController taskController) {
            this.taskController = taskController;
        }

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


    static class resourcesCell extends ListCell<Resource> {

        private final TaskController taskController;
        private CheckBox checkbox;

        public resourcesCell(TaskController taskController) {
            this.taskController = taskController;
        }

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
