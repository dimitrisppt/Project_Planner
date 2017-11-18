package main.java.armadillo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class View extends Application{
	
	public static void main(String[] args) {
        launch(args);
    }

	@Override
    public void start(Stage primaryStage) throws Exception{
		
		
		// Initialise object of the main scene
		primaryStage.setTitle("Project Planner");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		Scene scene = new Scene(grid, 600, 475);
		primaryStage.setScene(scene);
		
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);
		
		Button task = new Button("Enter Task");
		grid.add(task, 0, 1);
		
		Button people = new Button("Add People");
		grid.add(people, 1, 1);

		Button resource = new Button("Add Resources");
		grid.add(resource, 2, 1);
		
		
		
		// ------------- Change to MVC ---------------
		task.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				Label taskLabel = new Label("Task Name: ");
		        // taskLabel.setAlignment(Pos.TOP_LEFT);

		        TextField taskField = new TextField();
		        //taskField.setAlignment(Pos.TOP_CENTER);
		        taskField.setMaxSize(200,25);

		        Label effortLabel = new Label("Effort Estimate: ");

		        TextField effortField = new TextField();
		        effortField.setMaxSize(200,25);

		        ListView<String> listOfTasks = new ListView<String>();
		        ObservableList<String> items = FXCollections.observableArrayList (
		                "Task #1", "Task #2", "Task #3");
		        listOfTasks.setItems(items);

			
				Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				VBox dialogVbox = new VBox(20);
	
		        dialogVbox.getChildren().add(taskLabel);
		        dialogVbox.getChildren().add(taskField);
		        dialogVbox.getChildren().add(effortLabel);
		        dialogVbox.getChildren().add(effortField);
		        dialogVbox.getChildren().add(listOfTasks);
		        
		        Scene dialogScene = new Scene(dialogVbox, 500, 600);
		        dialog.setScene(dialogScene);
		        dialog.show();
					
			}
		});
		
		
		// ------------- Change to MVC ---------------
		people.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Label nameLabel = new Label("Enter Name: ");

		        TextField nameField = new TextField();
		        nameField.setMaxSize(200,25);

		        ListView<String> listOfPeople = new ListView<String>();
		        ObservableList<String> items = FXCollections.observableArrayList (
		                "Nick", "Eric");
		        listOfPeople.setItems(items);

		        final Stage dialog = new Stage();
		        dialog.initModality(Modality.APPLICATION_MODAL);
		        VBox dialogVbox = new VBox(20);

		        dialogVbox.getChildren().add(nameLabel);
		        dialogVbox.getChildren().add(nameField);
		        dialogVbox.getChildren().add(listOfPeople);

		        Scene dialogScene = new Scene(dialogVbox, 500, 600);
		        dialog.setScene(dialogScene);
		        dialog.show();
			}
			
		});
		
		
		// ------------- Change to MVC ---------------
		resource.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Label recourseLabel = new Label("Enter Recourse: ");
		
		        TextField recourseField = new TextField();
		        recourseField.setMaxSize(200,25);
		
		        final Stage dialog = new Stage();
		        dialog.initModality(Modality.APPLICATION_MODAL);
		        VBox dialogVbox = new VBox(20);
		        dialogVbox.getChildren().add(recourseLabel);
		        dialogVbox.getChildren().add(recourseField);
		
		        Scene dialogScene = new Scene(dialogVbox, 500, 600);
		        dialog.setScene(dialogScene);
		        dialog.show();
			}
			
		});
				
		
		primaryStage.show();
		
    }


    
}
