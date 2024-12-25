package whitman.cs370proj.composer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Flora florataagen
 * @author Paul laolarou726
 * @author Tina pudding2718
 */

public class MainApplication extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch();
    }

    /**
     * Opens Scale Player scene from FXML
     *
     * @param primaryStage stage upon which to build scene
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Pane root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Main.fxml")));
        } catch (IOException e) {
            System.out.println("Unable to load file \"Main.fxml\".\n" + e);
            System.exit(0);
        }
        primaryStage.setOnCloseRequest(event -> {
            primaryStage.close();
            System.exit(0);
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Scale Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}