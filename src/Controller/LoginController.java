package Controller;

import Model.*;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
                //putting data in weekly summary
                HashMap<Date,ExerciseSession> sessionHashMap = ExerciseSession.getDateAll(u);
                for (Map.Entry<Date,ExerciseSession> entry : sessionHashMap.entrySet()){
                    if(entry.getKey().getTime()<Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(28).atStartOfDay(ZoneId.systemDefault()))).getTime()){
                        java.util.Date d = entry.getKey();
                        ExerciseSession s = entry.getValue();
                        sortExerciseSessionToWeeklySummary(u,d,s);
                        //add it to the current data in weekly summary
                        s.removeLink(u,d);
                    }
                }
                HashMap<Date, Meal> foodHashMap = Meal.getDateAll(u);
                for (Map.Entry<Date,Meal> entry : foodHashMap.entrySet()){
                    if(entry.getKey().getTime()<Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(28).atStartOfDay(ZoneId.systemDefault()))).getTime()){
                        java.util.Date d = entry.getKey();
                        Meal m = entry.getValue();
                        sortMealToWeeklySummary(u,d,m);
                        //add it to the current data in weekly summary
                        m.removeLink(u,d);
                    }
                }
                HashMap<Integer,Date> weights = u.getAllWeights();
                for (Map.Entry<Integer,Date> entry : weights.entrySet()){
                    if(entry.getValue().getTime()<Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(28).atStartOfDay(ZoneId.systemDefault()))).getTime()){
                        java.util.Date d = entry.getValue();
                        int i = entry.getKey();
                        sortWeightToWeeklySummary(u,d,i);
                        //add it to the current data in weekly summary
                        u.removeWeight(d);
                    }
                }
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
    private void sortExerciseSessionToWeeklySummary(User u,java.util.Date d,ExerciseSession s){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //Date.from(Instant.from(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays()));
        c.add(Calendar.DATE,dayOfWeek*-1);
        Date commence = c.getTime();
        ArrayList<String> sum = u.getWeeklySummary(commence);
        if(sum.isEmpty()){
            int previousWeight = 0;
            c.setTime(commence);
            c.add(Calendar.DATE,-7);
            Date prevCommence = c.getTime();
            ArrayList<String> prevWeek = u.getWeeklySummary(prevCommence);
            if (!prevWeek.isEmpty()){
                previousWeight = Integer.parseInt(prevWeek.get(5));
            }
            u.newSummary(commence,s.getCaloriesBurned(),0,previousWeight);
        } else {
            int id = Integer.parseInt(sum.get(0));
            int newCalBurn = Integer.parseInt(sum.get(3)) + s.getCaloriesBurned();
            int newCalCons = Integer.parseInt(sum.get(4));
            int newWeight = Integer.parseInt(sum.get(5));
            u.updateSummary(id,newCalBurn,newCalCons,newWeight);
        }
    }
    private void sortMealToWeeklySummary(User u,java.util.Date d,Meal m){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //Date.from(Instant.from(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays()));
        c.add(Calendar.DATE,dayOfWeek*-1);
        Date commence = c.getTime();
        ArrayList<String> sum = u.getWeeklySummary(commence);
        if(sum.isEmpty()){
            int previousWeight = 0;
            c.setTime(commence);
            c.add(Calendar.DATE,-7);
            Date prevCommence = c.getTime();
            ArrayList<String> prevWeek = u.getWeeklySummary(prevCommence);
            if (!prevWeek.isEmpty()){
                previousWeight = Integer.parseInt(prevWeek.get(5));
            }
            u.newSummary(commence,0,m.getCalories(),previousWeight);
        } else {
            int id = Integer.parseInt(sum.get(0));
            int newCalBurn = Integer.parseInt(sum.get(3));
            int newCalCons = Integer.parseInt(sum.get(4)) + m.getCalories();
            int newWeight = Integer.parseInt(sum.get(5));
            u.updateSummary(id,newCalBurn,newCalCons,newWeight);
        }
    }
    private void sortWeightToWeeklySummary(User u,java.util.Date d,int w){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //Date.from(Instant.from(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays()));
        c.add(Calendar.DATE,dayOfWeek*-1);
        Date commence = c.getTime();
        ArrayList<String> sum = u.getWeeklySummary(commence);
        if(sum.isEmpty()){
            u.newSummary(commence,0,0,w);
        } else {
            int id = Integer.parseInt(sum.get(0));
            int newCalBurn = Integer.parseInt(sum.get(3));
            int newCalCons = Integer.parseInt(sum.get(4));
            u.updateSummary(id,newCalBurn,newCalCons,w);
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
