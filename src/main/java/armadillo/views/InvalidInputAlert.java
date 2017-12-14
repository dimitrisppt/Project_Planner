package armadillo.views;

import javafx.scene.control.Alert;

/**
 * The alert window which opens to notify the user that inavlid data has been input into the task form
 */
public class InvalidInputAlert extends Alert {
    
    /**
	 * This creates an alert when invalid information is passed to the program
	 * @param e The exception that has been thrown
	 */
	public InvalidInputAlert(IllegalArgumentException e) {
        super(AlertType.ERROR);
        this.setTitle("Invalid input");
        this.setHeaderText("You entered some invalid input");
        this.setContentText(e.getMessage());
    }
}
