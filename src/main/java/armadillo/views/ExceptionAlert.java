package armadillo.views;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionAlert extends Alert {
    public ExceptionAlert(Exception e) {
        super(AlertType.ERROR);
        this.setTitle("An exception has occured");
        this.setHeaderText("Something unexpected has happened");
        this.setContentText(e.getMessage());
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        Label label = new Label("The stacktrace of the exception was:");
        TextArea textArea = new TextArea(stringWriter.toString());
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expandedContent = new GridPane();
        expandedContent.setMaxWidth(Double.MAX_VALUE);
        expandedContent.add(label, 0, 0);
        expandedContent.add(textArea, 0, 1);
        this.getDialogPane().setExpandableContent(expandedContent);
    }
}
