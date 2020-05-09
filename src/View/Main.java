package View;

import Controller.GenericController;
import Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        GenericController genericController = new GenericController();
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/Login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Health Tracker");
        primaryStage.setScene(new Scene(root));
        LoginController controller = loader.<LoginController>getController();
        controller.setConnection(genericController.getConnection());
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
