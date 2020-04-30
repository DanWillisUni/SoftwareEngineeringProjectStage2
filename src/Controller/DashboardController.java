package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardController extends GenericController{
    private Model.User User;
    @FXML private Label name;
    @FXML private Label calLeft;
    @FXML Label GoalDone;
    @FXML private Label nextGoal;
    @FXML private Label BMI;

    /**
     * sets the user to the user that is logged in
     * @param User Person object logged in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the display
     * gets any goals that have expired or been competed and removes them with a message
     * gets bmi
     * gets the calories of that day both consumed and burned and works out calories left
     * gets the weight of the upcoming goal
     * gets the weights and dates
     * only displays the chart of the last 2 weeks to track weight
     */
    public void setUpDisplay() {
        name.setText("Welcome " + User.getForename());

        if (User.getHeight()>0 && User.getWeight()>0){
            DecimalFormat df = new DecimalFormat("#.##");//format of decimal of bmi
            df.setRoundingMode(RoundingMode.CEILING);
            double bmi = User.getWeight()/Math.pow((double)(User.getHeight()/100.0),2.0);//works out bmi
            if (bmi > 0){
                BMI.setText("Your BMI is: " + df.format(bmi));//sets bmi label
            }
        }

        ArrayList<WeightGoal> allGoals = WeightGoal.getAll(User);
        for(WeightGoal wg: allGoals){
            if (wg.isMet()){
                wg.remove();
                GoalDone.setText("Congratulations, You completed your goal!");
            }
        }
        //Mifflin-St. Jeor equation
        if (User.getHeight()>0 && User.getWeight()>0){
            long ageMs = new Date().getTime() - User.getDOB().getTime();
            int age = Integer.parseInt(Long.toString(ageMs/31536000000L));
            int s = 5;
            if (User.getGender()=='F'){
                s=-161;
            }
            int toMaintainCal = (int)(10*User.getWeight() + 6.25*User.getHeight() - (5 * age) + s);
            User.setCal(toMaintainCal);
            if (!allGoals.isEmpty()){
                nextGoal.setText("The next goal is: " + Integer.toString(allGoals.get(0).getTargetWeight()) + "kg, on " + allGoals.get(0).getDue());
                double distanceToGoal = User.getWeight()-allGoals.get(0).getTargetWeight();
                long timeMs = (new Date().getTime() - allGoals.get(0).getDue().getTime());
                double daysTillGoal = Integer.parseInt(Long.toString(timeMs/(86400000L)));
                double kgPerDay = distanceToGoal/daysTillGoal;
                User.setCal((int)(toMaintainCal * (1+(kgPerDay/0.004285714285713)/100)));
            }
            User.update();
            int totalCal = User.getCal();
            int calBurned = 0;
            ArrayList<ExerciseSession> exerciseLinks = ExerciseSession.getTodays(User);
            for (ExerciseSession el:exerciseLinks){
                calBurned += el.getCaloriesBurned();
            }
            int calConsumed = 0;
            ArrayList<Meal> meals = Meal.getTodays(User);
            for (Meal d:meals){
                calConsumed += (d.getCalories());
            }
            calLeft.setText(totalCal + " - " + calConsumed + " + " + calBurned + " = " + (totalCal-calConsumed+calBurned));
        }

    }

    /**
     * take the user to the add weight button
     * @param event button pushed to add weight
     */
    @FXML
    private void GoToAddWeightButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddWeight.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddWeightController controller = loader.<AddWeightController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * goes to the add exercise page
     * @param event add exercise button pressed
     */
    @FXML
    private void GoToAddExerciseSessionButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddExerciseSession.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddExerciseSessionController controller = loader.<AddExerciseSessionController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * goes to the add food page
     * @param event add food button pushed
     */
    @FXML
    private void GoToAddFoodButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddFood.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddFoodController controller = loader.<AddFoodController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * goes to the add goal page
     * @param event add goal button pressed
     */
    @FXML
    private void GoToAddGoalButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddGoal.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddGoalController controller = loader.<AddGoalController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    @FXML
    private void GoToPersonalDetailsButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/PersonalDetails.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        PersonalDetailsController controller = loader.<PersonalDetailsController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * signs out the user by not passing the user and going to login page
     * @param event sign out button pushed
     */
    @FXML
    private void GoToSignOutButtonAction (ActionEvent event) {
        goToPage("../View/Login.fxml",event);
    }
}
