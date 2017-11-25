package armadillo;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

		TaskPanel taskPanel = new TaskPanel(task);
		PeoplePanel peoplePanel = new PeoplePanel(people);
		ResourcesPanel resourcesPanel = new ResourcesPanel(resource);

		
		primaryStage.show();
		
    }

    public void createCalendar() {

	}


    
}
