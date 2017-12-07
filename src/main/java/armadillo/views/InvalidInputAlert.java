package armadillo.views;

import javafx.scene.control.Alert;

public class InvalidInputAlert extends Alert {
    public InvalidInputAlert(IllegalArgumentException e) {
        super(AlertType.ERROR);
        this.setTitle("Invalid input");
        this.setHeaderText("You entered some invalid input");
        this.setContentText(e.getMessage());
    }
}
