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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class DashboardController extends GenericController{
    private Model.User User;
    @FXML private Label name;
    @FXML private Label calLeft;
    @FXML Label GoalDone;
    @FXML private Label nextGoal;
    @FXML private Label BMI;
    @FXML private Label suggestion;
    @FXML private BarChart ExerciseBar;
    @FXML private PieChart ConsumedCal;

    /**
     * Sets the user to the user that is logged in
     * @param User Person object logged in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the display
     * Gets any goals that have expired or been competed and removes them with a message
     * Gets bmi
     * Gets the calories of that day both consumed and burned and works out calories left
     * Gets the weight of the upcoming goal
     * Displays the amount of calories eaten by each mealtype in a pie chart
     * Displays how many calories are burnt by each exercise in a bar chart
     * Only displays the charts with the last 2 weeks of data on
     */
    public void setUpDisplay() {
        name.setText("Welcome " + User.getForename());

        if (User.getHeight()>0 && User.getWeight()>0){
            DecimalFormat df = new DecimalFormat("#.##");//format of decimal of bmi
            df.setRoundingMode(RoundingMode.CEILING);
            double bmi = User.getWeight()/Math.pow((double)(User.getHeight()/100.0),2.0);//works out bmi
            if (bmi > 0) {
                String bmiCategory = "";
                if (bmi < 16) {
                    bmiCategory = " severly underweight";
                } else if(bmi>= 16 && bmi < 19){
                    bmiCategory = " underweight";
                } else if(bmi>= 19 && bmi < 25){
                    bmiCategory = " healthy";
                } else if(bmi>= 25 && bmi < 30){
                    bmiCategory = " overweight";
                }else if(bmi>= 30 && bmi < 40){
                    bmiCategory = " obese";
                } else {
                    bmiCategory = " serverly obese";
                }
                BMI.setText("Your BMI is: " + df.format(bmi) + bmiCategory);//sets bmi label
            }
        }

        ArrayList<WeightGoal> allGoals = WeightGoal.getAll(User);
        for(WeightGoal wg: allGoals){
            if (wg.isMet()){
                wg.remove();
                GoalDone.setText("Congratulations, You completed your goal!");
            }
        }
        int calRemaining = 0;
        //Mifflin-St. Jeor equation
        if (User.getHeight()>0 && User.getWeight()>0){
            long ageMs = new Date().getTime() - User.getDOB().getTime();
            int age = Integer.parseInt(Long.toString(ageMs/31536000000L));
            int s = 5;
            if (User.getGender()=='F'){
                s=-161;
            }
            int toMaintainCal = (int)(10*User.getWeight() + 6.25*User.getHeight() - (5 * age) + s);
            User.setCal((int)(toMaintainCal * 1.2));//multiplyed by 1.2 as that is roughly how many calories people burn from walking around each day and other exercise that is not going to be added in
            if (!allGoals.isEmpty()){
                nextGoal.setText("The next goal is: " + Double.toString(allGoals.get(0).getTargetWeight()) + "kg, on " + allGoals.get(0).getDue());
                double distanceToGoal = User.getWeight()-allGoals.get(0).getTargetWeight();
                long timeMs = (new Date().getTime() - allGoals.get(0).getDue().getTime());
                double daysTillGoal = Integer.parseInt(Long.toString(timeMs/(86400000L)));
                double kgPerDay = distanceToGoal/daysTillGoal;
                int cals = (int)(toMaintainCal * (1+(kgPerDay/0.004285714285713)/100));
                if (cals>1000){
                    User.setCal(cals);
                }
            }
            User.update();
            int totalCal = User.getCal();
            int calBurned = 0;
            ArrayList<ExerciseSession> exerciseLinks = ExerciseSession.getDays(User,new Date());
            for (ExerciseSession el:exerciseLinks){
                calBurned += el.getCaloriesBurned();
            }
            int calConsumed = 0;
            ArrayList<Meal> meals = Meal.getDays(User,new Date());
            for (Meal d:meals){
                calConsumed += (d.getCalories());
            }
            calRemaining = (totalCal-calConsumed+calBurned);
            calLeft.setText(totalCal + " - " + calConsumed + " + " + calBurned + " = " + calRemaining);
        }

        ArrayList<String> suggestions = new ArrayList<>();
        if (User.getHeight()==0){
            suggestion.setText("Try going to Add Measurements and entering your height");
        } else if (User.getWeight()==0){
            suggestion.setText("Try going to Add Measurements");
        } else {
            Map.Entry<Double,Date> entry = User.getAllWeights().entrySet().iterator().next();
            if(entry.getValue().getTime()<(((new Date()).getTime())-86400000L)){
                suggestions.add("Try entering your weight for today");
            }
            if (WeightGoal.getAll(User).isEmpty()){
                suggestions.add("Try going to add goal and setting a personal goal");
            }
            if (calRemaining>100){
                suggestions.add("Remember you have more calories to eat");
            }
            if (calRemaining<-100){
                suggestions.add("Oh dear, You have over eaten, try doing an exercise");
            }
            if (suggestions.isEmpty()){
                suggestions.add("Go do some exercise, come back and add it");
                suggestions.add("Well Done!!");
                suggestions.add("Keep working towards your goals");
                suggestions.add("Try setting another goal to work towards");
            }
        }
        if (suggestion.getText().equals("")){
            Random random = new Random();
            suggestion.setText(suggestions.get(random.nextInt(suggestions.size())));
        }


        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Calories");
        //hashmap
        HashMap<String,Integer> data = new HashMap<>();
        for (int i = 0;i<14;i++){
            ArrayList<ExerciseSession> day = ExerciseSession.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            for (ExerciseSession s:day){
                if(data.containsKey(s.getExerciseName())){
                    data.put(s.getExerciseName(),data.get(s.getExerciseName()) + s.getCaloriesBurned());
                } else {
                    data.put(s.getExerciseName(),s.getCaloriesBurned());
                }
            }
        }
        for (Map.Entry<String,Integer> entry : data.entrySet()){
            series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
        ExerciseBar.getData().addAll(series1);

        HashMap<String,Integer> dataPie = new HashMap<>();
        for (int i = 0;i<14;i++){
            ArrayList<Meal> day = Meal.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            for (Meal m:day){
                if(dataPie.containsKey(m.getType())){
                    dataPie.put(m.getType(),data.get(m.getType()) + m.getCalories());
                } else {
                    dataPie.put(m.getType(),m.getCalories());
                }
            }
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String,Integer> entry : dataPie.entrySet()){
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        ConsumedCal.getData().addAll(pieChartData);
    }

    /**
     * Take the user to the add measurement page
     * @param event button pushed to add weight
     */
    @FXML
    private void GoToAddMeasurementsButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddMeasurements.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddMeasurementsController controller = loader.<AddMeasurementsController>getController();
        controller.setUser(User);
        controller.setUpDisplay(0,0);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * Goes to the add exercise page
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
     * Goes to the add food page
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
     * Goes to the add goal page
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
    /**
     * Goes to the personal details page
     * @param event go to personal detail button is pushed
     */
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
     * Goes to the history page
     * @param event history button pushed
     */
    @FXML
    private void GoToHistoryButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/History.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        HistoryController controller = loader.<HistoryController>getController();
        controller.setUser(User);
        controller.setUpDisplay(7,-1);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * Signs out the user by not passing the user and going to login page
     * @param event sign out button pushed
     */
    @FXML
    private void GoToSignOutButtonAction (ActionEvent event) {
        goToPage("../View/Login.fxml",event);
    }
}
