package Controller;

import Model.User;
import Model.WeightGoal;
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
import java.util.ArrayList;

public class LoginController extends GenericController{
    @FXML private Label errorMsg;//error message label
    @FXML private TextField email;//email textbox
    @FXML private PasswordField password;//password password box
    @FXML private TextField emailReset;//email to reset textbox
    @FXML private PasswordField passwordReset;//password to reset box
    @FXML private PasswordField passwordReset2;//password to reset repeat box
    /**
     * Attempt to get a new User obj from an email
     * If no User with that email exists, display appropriate error message
     * Get the hashed password of the User and compare it to the hashed password box
     * If the password isnt correct put display appropriate error message
     * Check to see if any goals are overdue
     * Remove any overdue goals
     * Put a message about removing the goal on the dashboard
     * Go to the dashboard
     * @param event login button pressed
     */
    @FXML
    private void LoginHandleSubmitButtonAction (ActionEvent event) {
        errorMsg.setText("");
        //validation
        User u = User.getFromEmail(email.getText().toString());
        if (u!=null){//if there is a user with that email
            if (u.getPassword().equals(User.passwordHash(password.getText()))){//if the hashed password matches the users hashed password
                //load dashboard
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
                //check for overdue goals
                ArrayList<WeightGoal> allGoals = WeightGoal.getAll(u);
                for(WeightGoal wg: allGoals){
                    if (wg.isOverdue()){//if the goal is overdue
                        wg.remove();//remove it
                        controller.GoalDone.setText("An overdue goal was removed");//put message on the dashboard about overdue goal
                    }
                }
                //continue setting up and displaying the dashboard
                controller.setUpDisplay();
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                stage.setFullScreen(true);
                stage.show();
            } else {
                errorMsg.setText("Incorrect password details");//password was wrong
                password.setText("");
            }
        } else {
            errorMsg.setText("Incorrect email details");//email was wrong
            password.setText("");
            email.setText("");
        }
    }
    /**
     * Go to the registration page
     * @param event go to registration button pushed
     */
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) {
        goToPage("../View/Registration.fxml",event);
    }
    /**
     * Exits the application
     * @param event exit button pushed
     */
    @FXML
    private void Exit (ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

    public void LoginHandleResetButtonAction(ActionEvent actionEvent) {
        errorMsg.setText("");
        //validation
        User u = User.getFromEmail(emailReset.getText().toString());
        if (u!=null){//if there is a user with that email
            //password validation
            if (passwordReset.getText()!=null){
                if (!passwordReset.getText().equals("")){
                    if (passwordReset.getText().toString().length()>=20){
                        errorMsg.setText("Error: password too long for reset");
                        passwordReset.setText("");
                    } else {
                        if (!passwordReset.getText().equals(passwordReset2.getText())) {
                            errorMsg.setText("Passwords do not match");
                            passwordReset2.setText("");
                        } else {
                            u.setPassword(passwordReset.getText());
                            errorMsg.setText("Your password has been reset");
                            passwordReset.setText("");
                            passwordReset2.setText("");
                            emailReset.setText("");
                        }
                    }
                } else {
                    errorMsg.setText("Error: password null for reset");
                }
            }else{
                errorMsg.setText("Error: password null for reset");
            }
        } else {
            errorMsg.setText("Incorrect email for reset details");//email was wrong
            password.setText("");
            email.setText("");
        }
    }
}
