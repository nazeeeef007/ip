package milo;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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

    private Milo milo;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image miloImage = new Image(this.getClass().getResourceAsStream("/images/DaMilo.png"));

    @FXML
    public void initialize() {
        // Automatically scroll to the bottom when the height of the container increases
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Show initial welcome message
        dialogContainer.getChildren().add(
                DialogBox.getMiloDialog("Hello! I'm Milo. How can I help you today?", miloImage)
        );
    }

    public void setMilo(Milo m) {
        milo = m;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Milo's reply.
     * Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        String response = milo.getResponse(input);

        /* * Optional Enhancement from Part 5:
         * If your Milo class has a getCommandType() method, you can pass it here
         * to change the color of the chat bubble.
         */
        // String commandType = milo.getCommandType();

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMiloDialog(response, miloImage)
        );

        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}