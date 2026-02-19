package natto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import natto.ui.MainWindow;

/**
 * MainApp class that extends JavaFX Application. This is the entry point of the application.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));

        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                getClass().getResource("/css/main.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle("Natto");
        stage.show();

        MainWindow controller = fxmlLoader.getController();
        controller.setNatto(new Natto());
    }
}
