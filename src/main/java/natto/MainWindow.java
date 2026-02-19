package natto.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import natto.Natto;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Natto natto;

    private final Image userImage =
            new Image(this.getClass().getResourceAsStream("/images/Chillguy.jpg"));
    private final Image nattoImage =
            new Image(this.getClass().getResourceAsStream("/images/natto.jpg"));

    /**
     * Initializes the main window, setting up necessary bindings and loading resources.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        System.out.println(getClass().getResource("/images/catWP.png"));
    }

    public void setNatto(Natto n) {
        natto = n;
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(
                        natto.getGreeting(),
                        nattoImage
                )
        );
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = natto.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, nattoImage)
        );

        userInput.clear();
        if ("bye".equals(input.trim())) {
            javafx.application.Platform.exit();
        }
    }
}
