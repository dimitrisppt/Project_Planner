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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class View extends Application{
	
	private ObservableList<Task> tasks;
	
	public static void main(String[] args) throws Exception {
		CreateDatabase.createDatabase(Database.MAIN_URL);
        launch(args);
    }


	@Override
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

		
		ScrollPane schedulePane = new ScrollPane();
		
		tasks = FXCollections.observableArrayList ();
	    ListView<Task> tasksForSchedule = new ListView<>(tasks);
	    tasksForSchedule.setCellFactory(param -> new scheduleCell(tc, this));
		
	    schedulePane.setContent(tasksForSchedule);
	    schedulePane.setFitToWidth(true);
		
		VBox vCenter = new VBox();
		vCenter.setSpacing(15);
		vCenter.setAlignment(Pos.CENTER);
		vCenter.getChildren().addAll(scenetitle, schedulePane);
		
		pane.setTop(topPane);
		pane.setCenter(vCenter);

		tc.updateSchedule(this);

		primaryStage.show();
		
    }
	
	public void updateTasks(Set<Task> allTasks) {
        tasks.setAll(allTasks);
        System.out.println("Test");
    }
	
	
	static class scheduleCell extends ListCell<Task> {

        BorderPane borderPane = new BorderPane();
        Label taskNameLabel = new Label("");
        Label taskDescriptionLabel = new Label("");
        Label dateTimeLabel = new Label("");
        private TaskController taskController;

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
            
            borderPane.setTop(taskNameLabel);
            borderPane.setCenter(vbox);
            borderPane.setBottom(button);
        }

        @Override
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
                    
                } catch (SQLException | ClassNotFoundException | ElementDoesNotExistException e) {
                    ExceptionAlert ea = new ExceptionAlert(e);
                    ea.show();	
                }
                setGraphic(borderPane);
            }
        }
    }

    
}
