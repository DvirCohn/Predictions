package fxAppliction;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxAppliction/app/AppController.fxml");
        fxmlLoader.setLocation(url);

        Parent root = fxmlLoader.load(url.openStream());
        Scene scene = new Scene(root,800,600);
        scene.getStylesheets().add("AppControllerCSS.css");
        stage.setScene(scene);
        stage.show();

    }
}

