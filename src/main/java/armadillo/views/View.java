package armadillo.views;

import armadillo.controllers.PeopleController;
import armadillo.controllers.ResourceController;
import armadillo.models.Database;
import armadillo.views.PeoplePanel;
import armadillo.views.ResourcesPanel;
import armadillo.views.TaskPanel;
import javafx.application.Application;
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
	
	public static void main(String[] args) {
        launch(args);
    }


	@Override
    public void start(Stage primaryStage) throws Exception{
		Database database = new Database();
		ResourceController rc = new ResourceController(database);
		PeopleController pc = new PeopleController(database);

		
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
            TaskPanel tp = new TaskPanel();
            tp.show();
        });

		Button people = new Button("Add People");
		people.setOnAction(event -> {
			PeoplePanel pp = pc.getPp();
			pp.show();
		});

		Button resource = new Button("Add Resources");
		resource.setOnAction(event -> {
			ResourcesPanel rp = rc.getRp();
			rp.show();
		});


		HBox hTop = new HBox();
		hTop.setSpacing(15);
		hTop.setAlignment(Pos.CENTER);
		hTop.getChildren().addAll(task, people, resource);

		BorderPane topPane = new BorderPane();
		Button refreshButton = new Button("Refresh");
		refreshButton.setAlignment(Pos.BOTTOM_LEFT);
		topPane.setCenter(hTop);
		topPane.setBottom(refreshButton);


		pane.setTop(topPane);
		pane.setCenter(scenetitle);



		primaryStage.show();
		
    }



    
}
