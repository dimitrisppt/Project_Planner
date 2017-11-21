package armadillo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.StyledEditorKit;
import java.awt.*;

public class View extends Application{
	
	public static void main(String[] args) {
        launch(args);
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

				Button submit = new Button("Submit");
				submit.setStyle("-fx-font-weight: bold");

				Label personInstructions = new Label("Select people to assign to task.");
				Label taskInstructions = new Label("Select prerequisite tasks or delete pre-existing tasks.");
				Label resourcesInstructions = new Label("Enter required resources.");

				Label blankLabel = new Label();
				TextField resourcesField = new TextField();
				
				Label taskLabel = new Label("Task Name:");
				taskLabel.setStyle("-fx-font-weight: bold");
		        // taskLabel.setAlignment(Pos.TOP_LEFT);

		        TextField taskField = new TextField();
		        //taskField.setAlignment(Pos.TOP_CENTER);
		        taskField.setMaxSize(200,25);

		        Label taskDescription = new Label("Task Description:");
		        taskDescription.setStyle("-fx-font-weight: bold");

		        TextField descriptionField = new TextField();
				descriptionField.setMaxSize(200,100);

				Label spinnerLabelHours = new Label("Hours:");
				Label spinnerLabelMins = new Label("Minutes:");

				Spinner<Integer> spinnerHours = new Spinner<Integer>();
				int initialValueHours = 1;
				SpinnerValueFactory<Integer> valueFactoryHours = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, initialValueHours);

				Spinner<Integer> spinnerMins = new Spinner<Integer>();
				int initialValueMins = 0;
				SpinnerValueFactory<Integer> valueFactoryMins = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, initialValueHours);

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

			
				Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
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
		        dialogHbox.getChildren().add(descriptionField);

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
		
		
		// ------------- Change to MVC ---------------
		resource.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Label recourseLabel = new Label("Enter Resource: ");
		
		        TextField recourseField = new TextField();
		        recourseField.setMaxSize(200,25);

				ObservableList<String> resource = FXCollections.observableArrayList (
						"Resource #1", "Resource #2");
				ListView<String> listOfResources = new ListView<String>(resource);
				listOfResources.setCellFactory(param -> new peopleCell());
		
		        final Stage dialog = new Stage();
		        dialog.initModality(Modality.APPLICATION_MODAL);
		        VBox dialogVbox = new VBox(20);
				dialogVbox.setPadding(new Insets(25,25,25,25));
		        dialogVbox.getChildren().add(recourseLabel);
		        dialogVbox.getChildren().add(recourseField);
		        dialogVbox.getChildren().add(listOfResources);
		
		        Scene dialogScene = new Scene(dialogVbox, 500, 500);
		        dialog.setScene(dialogScene);
		        dialog.show();
			}
			
		});
				
		
		primaryStage.show();
		
    }


    
}
