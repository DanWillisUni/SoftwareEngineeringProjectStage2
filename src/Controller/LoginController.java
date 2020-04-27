package Controller;

import Model.GenericDatabaseController;
import Model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends GenericController{
    @FXML private Label errorMsg;
    @FXML private TextField email;
    @FXML private PasswordField password;
    /**
     * if email and password valid
     * begin to load dashboard
     * from the email get the id
     * fetch all the info from database
     * create new Person object
     * set the user on dashboard to the new Person
     * @param event login button pressed
     */
    @FXML
    private void LoginHandleSubmitButtonAction (ActionEvent event) {
        GenericDatabaseController db = new GenericDatabaseController();
        errorMsg.setText("");
        //validation
        User u = User.getFromEmail(email.getText().toString());
        if (u!=null){
            if (u.getPassword().equals(password.getText())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Dashboard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                DashboardController controller = loader.<DashboardController>getController();
                controller.setUser(u);
//                if(db.removeOverdueGoals(u.getId())){//checks for any overdue goals
//                    controller.GoalDone.setText("Goal Removed as it was overdue");
//                }
                controller.setUpDisplay();
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                stage.setFullScreen(true);
                stage.show();
            } else {
                errorMsg.setText("Incorrect password details");
                password.setText("");
            }
        } else {
            errorMsg.setText("Incorrect email details");
            password.setText("");
            email.setText("");
        }
    }
    /**
     * go to registration page
     * @param event go to registration
     */
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) {
        goToPage("../View/Registration.fxml",event);
    }
    /**
     * exits the application
     * @param event exit button pushed
     */
    @FXML
    private void Exit (ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
}
