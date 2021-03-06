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
import java.sql.Connection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class LoginController extends GenericController{
    private Connection c;
    @FXML private Label errorMsg;//error message label
    @FXML private TextField email;//email textbox
    @FXML private PasswordField password;//password password box
    @FXML private TextField emailReset;//email to reset textbox
    @FXML private PasswordField passwordReset;//password to reset box
    @FXML private PasswordField passwordReset2;//password to reset repeat box

    public void setConnection(Connection c){
        this.c=c;
    }

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
        User u = null;
        String emailText = email.getText();
        if (email.getText()!=null) {
            if (!email.getText().equals("")) {
                if (email.getText().toString().length() < 60) {
                    if (email.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                        if (GenericDatabaseController.isInTable(email.getText(), "user", "email", c)) {
                            u = User.getFromEmail(emailText.toString(),c);
                        }
                    }
                }
            }
        }
        if (u!=null){//if there is a user with that email
            if (u.getPassword().equals(u.passwordHash(password.getText()))){//if the hashed password matches the users hashed password
                //putting all the data in weekly summary
                HashMap<ArrayList<Date>,ArrayList<ExerciseSession>> sessionHashMap = ExerciseSession.getDateAll(u,c);//weekly summary for exercise sessions
                for (Map.Entry<ArrayList<Date>,ArrayList<ExerciseSession>> entry : sessionHashMap.entrySet()){//for all exercisesessions
                    ArrayList<Date> datesArr = entry.getKey();
                    ArrayList<ExerciseSession> sessions = entry.getValue();
                    for (int i = 0;i<datesArr.size();i++){
                        if(datesArr.get(i).getTime()<Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(28).atStartOfDay(ZoneId.systemDefault()))).getTime()){//if they are over 4 weeks ago
                            //add it to the current data in weekly summary
                            java.util.Date d = datesArr.get(i);
                            ExerciseSession s = sessions.get(i);
                            sortExerciseSessionToWeeklySummary(u,d,s);//sorts putting the exercise session into the weekly summary
                            u.removeExerciseSessionLink(d,s,c);//removes the link
                        }
                    }
                }
                //food weekly summary
                HashMap<ArrayList<Date>, ArrayList<Meal>> foodHashMap = Meal.getDateAll(u,c);
                for (Map.Entry<ArrayList<Date>, ArrayList<Meal>> entry : foodHashMap.entrySet()){
                    ArrayList<Date> dates = entry.getKey();
                    ArrayList<Meal> meals = entry.getValue();
                    for (int i = 0;i<dates.size();i++){
                        if(dates.get(i).getTime()<Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(28).atStartOfDay(ZoneId.systemDefault()))).getTime()){
                            //add it to the current data in weekly summary
                            java.util.Date d = dates.get(i);
                            Meal m = meals.get(i);
                            sortMealToWeeklySummary(u,d,m);
                            u.removeFoodLink(d,m,c);//remove the link
                        }
                    }
                }
                LinkedHashMap<Date,Double> weights = u.getAllWeights(c);
                for (Map.Entry<Date,Double> entry : weights.entrySet()){
                    if(entry.getKey().getTime()<Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(28).atStartOfDay(ZoneId.systemDefault()))).getTime()){
                        java.util.Date d = entry.getKey();
                        double i = entry.getValue();
                        sortWeightToWeeklySummary(u,d,i); //add it to the current data in weekly summary
                        u.removeWeight(d,c);//remove the link
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
                controller.setUser(u,c);
                //check for overdue goals
                ArrayList<WeightGoal> allGoals = WeightGoal.getAll(u,c);
                for(WeightGoal wg: allGoals){
                    if (wg.isOverdue()){//if the goal is overdue
                        wg.remove(c);//remove it
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
     * Get the week commencing date by using a calender instance
     * Get the weekly summary of that week already
     * If it doesnt exist create one and add in the previous weeks weight
     * If it already exists add it and update it
     * @param u user obj
     * @param d date of exercise
     * @param s exerciseSession obj
     */
    private void sortExerciseSessionToWeeklySummary(User u,java.util.Date d,ExerciseSession s){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,dayOfWeek*-1);
        Date commence = calendar.getTime();
        WeeklySummary ws = u.getWeeklySummary(commence,c);
        if(ws==null){
            ws=new WeeklySummary(u,commence,c);
            ws.add(c);
        }
        ws.addCalBurnt(s.getCaloriesBurned());
        ws.updateSummary(c);
    }
    /**
     * Get the week commencing date by using a calender instance
     * Get the weekly summary of that week already
     * If it doesnt exist create one and add in the previous weeks weight
     * If it already exists add it and update it
     * @param u user obj
     * @param d date of exercise
     * @param m Meal obj
     */
    private void sortMealToWeeklySummary(User u,java.util.Date d,Meal m){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,dayOfWeek*-1);
        Date commence = calendar.getTime();
        WeeklySummary ws = u.getWeeklySummary(commence,c);
        if(ws==null){
            ws=new WeeklySummary(u,commence,c);
            ws.add(c);
        }
        ws.addCalConsu(m.getCalories());
        ws.updateSummary(c);
    }
    /**
     * Get the week commencing date by using a calender instance
     * Get the weekly summary of that week already
     * If it doesnt exist create one and add in the previous weeks weight
     * If it already exists add it and update it
     * @param u user obj
     * @param d date of exercise
     * @param w weight
     */
    private void sortWeightToWeeklySummary(User u,java.util.Date d,double w){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE,dayOfWeek*-1);
        Date commence = calendar.getTime();
        WeeklySummary ws = u.getWeeklySummary(commence,c);
        if(ws==null){
            ws=new WeeklySummary(u,commence,w);
            ws.add(c);
        } else {
            ws.setWeight(w);
        }
        ws.updateSummary(c);
    }
    /**
     * Go to the registration page
     * @param event go to registration button pushed
     */
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) {
        goToRegistration(c,event);
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
    /**
     * Resets the password of the user
     * @param actionEvent
     */
    public void LoginHandleResetButtonAction(ActionEvent actionEvent) {
        errorMsg.setText("");
        //validation
        User u = null;
        if (email.getText()!=null) {
            if (!emailReset.getText().equals("")) {
                if (emailReset.getText().toString().length() < 60) {
                    if (emailReset.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                        if (GenericDatabaseController.isInTable(emailReset.getText(), "user", "email", c)) {
                            u = User.getFromEmail(emailReset.getText().toString(),c);
                        }
                    }
                }
            }
        }
        //in real life somewhere here a confirmation email would be sent to the email
        //then the user would click a link in the email that confirms the reset
        if (u!=null){//if there is a user with that email
            //password validation
            if (passwordReset.getText()!=null){
                if (!passwordReset.getText().equals("")){
                    if (passwordReset.getText().toString().length()>=20){
                        errorMsg.setText("Error: password too long for reset");
                        passwordReset.setText("");
                        passwordReset2.setText("");
                    } else {
                        if (passwordReset.getText().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
                            if (!passwordReset.getText().equals(passwordReset2.getText())) {
                                errorMsg.setText("Passwords do not match");
                                passwordReset2.setText("");
                            } else {
                                u.setPassword(passwordReset.getText(),c);//reset the password
                                errorMsg.setText("Your password has been reset");
                                passwordReset.setText("");
                                passwordReset2.setText("");
                                emailReset.setText("");
                            }
                        } else {
                            errorMsg.setText("Minimum eight characters, at least one letter and one number:");
                            passwordReset.setText("");
                            passwordReset2.setText("");
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
            passwordReset.setText("");
            passwordReset2.setText("");
            emailReset.setText("");
        }
    }
}
