package armadillo.views;

import java.util.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;

import armadillo.controllers.CreateDatabase;
import armadillo.controllers.PeopleController;
import armadillo.controllers.ResourceController;
import armadillo.controllers.TaskController;
import armadillo.models.Database;
import armadillo.models.ElementDoesNotExistException;
import armadillo.models.Task;
import armadillo.models.Person;
import armadillo.models.Resource;
import armadillo.views.PeoplePanel;
import armadillo.views.ResourcesPanel;
import armadillo.views.TaskPanel;
import armadillo.views.TaskPanel.taskCell;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The main window of the application which opens on startup
 */
public class View extends Application{
	
	/**
	 * The list of tasks that have been placed into the system which are displayed as the schedule
	 */
	private ObservableList<Task> tasks;
	
	/**
	 * Main method for application, this calls to create the database and launch the program
	 */
	public static void main(String[] args) throws Exception {
		CreateDatabase.createDatabase(Database.MAIN_URL);
        launch(args);
    }


	@Override
	/**
	 * Method which creates and positions elements within the initial UI
	 */
    public void start(Stage primaryStage) throws Exception{

		Database database = new Database();
		ResourceController rc = new ResourceController(database);
		PeopleController pc = new PeopleController(database);
		TaskController tc=  new TaskController(database);

		
		// Initialise object of the main scene
		primaryStage.setTitle("Project Planner");
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(25,25,25,25));
		Scene scene = new Scene(pane, 600, 475);
		primaryStage.setScene(scene);

		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

		Button task = new Button("Enter Task");
		task.setOnAction(event -> {
            TaskPanel tp = tc.getTp();
            tc.update();
            tp.show();
        });

		Button people = new Button("Add People");
		people.setOnAction(event -> {
			PeoplePanel pp = pc.getPp();
			pc.update();
			pp.show();
		});

		Button resource = new Button("Add Resources");
		resource.setOnAction(event -> {
			ResourcesPanel rp = rc.getRp();
			rc.update();
			rp.show();
		});


		HBox hTop = new HBox();
		hTop.setSpacing(15);
		hTop.setAlignment(Pos.CENTER);
		hTop.getChildren().addAll(task, people, resource);

		BorderPane topPane = new BorderPane();
		Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction(event -> {
			tc.updateSchedule(this);
		});
		refreshButton.setAlignment(Pos.BOTTOM_LEFT);
		topPane.setCenter(hTop);
		topPane.setBottom(refreshButton);

		
		
		
		tasks = FXCollections.observableArrayList ();
	    ListView<Task> tasksForSchedule = new ListView<>(tasks);
	    tasksForSchedule.setCellFactory(param -> new scheduleCell(tc, this));
		
		VBox vCenter = new VBox();
		vCenter.setSpacing(15);
		vCenter.setAlignment(Pos.CENTER);
		vCenter.getChildren().addAll(scenetitle, tasksForSchedule);
		
		pane.setTop(topPane);
		pane.setCenter(vCenter);

		tc.updateSchedule(this);

		primaryStage.show();
		
    }
	
	/**
	 * This refreshes the schedule view
	 * @param allTasks The list of tasks to be displayed as the schedule
	 */
	public void updateTasks(Set<Task> allTasks) {
        tasks.setAll(allTasks);
    }
	
	/**
	 * This defines a custom cell to be used in the list component for the schedule
	 */
	static class scheduleCell extends ListCell<Task> {

        BorderPane borderPane = new BorderPane();
        Label taskNameLabel = new Label("");
        Label taskDescriptionLabel = new Label("");
        Label dateTimeLabel = new Label("");
        Button seeMore = new Button("More details");
        private TaskController taskController;

        /**
    	 * This initialises the elements contained within each cell of the schedule
    	 * @param taskController controller used to interact with the model of the task database
    	 * @param view A reference to the main view window used to call the method which refreshes the schedule
    	 */
        public scheduleCell(TaskController taskController, View view){
            super();
            
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(dateTimeLabel, taskDescriptionLabel);
           
            Button button = new Button("Delete");
            this.taskController = taskController;
            button.setOnAction(event -> {
                taskController.delete(getItem());
                taskController.updateSchedule(view);
            });
            
            
            HBox buttonsHBox = new HBox();
            
            buttonsHBox.getChildren().addAll(button, seeMore);
            
            borderPane.setTop(taskNameLabel);
            borderPane.setCenter(vbox);
            borderPane.setBottom(buttonsHBox);
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
                    taskNameLabel.setText(item.getName());
                    taskNameLabel.setStyle("-fx-font-weight: bold");
                    if(item.getDescription().equals("")) {
                    	taskDescriptionLabel.setText("[No description given]");
                    }else {
                    	taskDescriptionLabel.setText(item.getDescription());
                    }
                    if(item.getDateTime() == null) {
                    	dateTimeLabel.setText("--/--/---- --:--");
                    }else {
                    	DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    	Date date = new Date(item.getDateTime()*1000);
                        dateTimeLabel.setText(format.format(date));
                    }
                   
                    String people = "";
                    for(Person e:item.getPeople()) {
                    	people += "\n" + e.getFullName();
                    }
                    
                    String resources = "";
                    for(Resource e:item.getResources()) {
                    	resources += "\n" + e.getName();
                    }
                    
                    String tasks = "";
                    for(Task e:item.getPrerequisiteTasks()) {
                    	tasks += "\n" + e.getName();  	
                    }
                    
                    
                    String detailsString = "Task Name: " + item.getName() + "\nTask description: " + item.getDescription() + "\nEffort Estimate: " + item.getEffortEstimate()/3600 + "Hrs, " + (item.getEffortEstimate() % 3600) / 60 + "Mins\nPeople assigned to task: " + people + "\nResources required: " + resources + "\nPrerequisite tasks: " + tasks; 
                    String name = item.getName();
                    seeMore.setOnAction(event -> {
                    	Alert alert = new Alert(AlertType.INFORMATION);
                    	alert.setTitle(name);
                    	alert.setHeaderText(null);
                    	alert.setContentText(detailsString);
                    	alert.showAndWait();
                    });
                    
                } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e) {
                    ExceptionAlert ea = new ExceptionAlert(e);
                    ea.show();	
                }
                setGraphic(borderPane);
            }
        }
    }

    
}
