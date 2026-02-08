package milo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Milo using FXML.
 */
public class Main extends Application {

    private Milo milo = new Milo();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);

            // Set window constraints from Part 5
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.setTitle("Milo Chatbot");

            // Links the Milo logic to the UI Controller
            fxmlLoader.<MainWindow>getController().setMilo(milo);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}