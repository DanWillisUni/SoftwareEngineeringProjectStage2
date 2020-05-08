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
    @FXML private Label calLeft;//calories left statement
    @FXML Label GoalDone;//message box for the user to have a goal
    @FXML private Label nextGoal;//next goal of the user
    @FXML private Label BMI;//bmi of the user
    @FXML private Label suggestion;//a suggestion of an activity for them to do
    @FXML private BarChart ExerciseBar;//exercise bar chart
    @FXML private PieChart ConsumedCal;//mealtype piechart
    @FXML private Label cal1;
    @FXML private Label cal2;

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
        name.setText("Welcome, " + User.getUsername());//setting name at the top of the page
        //bmi
        if (User.getHeight()>0 && User.getWeight()>0){//if both the height and weight have been entered
            DecimalFormat df = new DecimalFormat("#.##");//format of decimal of bmi
            df.setRoundingMode(RoundingMode.CEILING);
            double bmi = User.getWeight()/Math.pow((double)(User.getHeight()/100.0),2.0);//works out bmi
            if (bmi > 0) {
                String bmiCategory = "";//essentially a switch case
                if (bmi < 16) {
                    bmiCategory = " - severly underweight";
                } else if(bmi>= 16 && bmi < 19){
                    bmiCategory = " - underweight";
                } else if(bmi>= 19 && bmi < 25){
                    bmiCategory = " - healthy";
                } else if(bmi>= 25 && bmi < 30){
                    bmiCategory = " - overweight";
                }else if(bmi>= 30 && bmi < 40){
                    bmiCategory = " - obese";
                } else {
                    bmiCategory = " - serverly obese";
                }
                BMI.setText("Your BMI is: " + df.format(bmi) + bmiCategory);//sets bmi label and category
            }
        }
        //get rid of competed goals
        ArrayList<WeightGoal> allGoals = WeightGoal.getAll(User);//get all the goals
        for(WeightGoal wg: allGoals){//for all the goals
            if (wg.isMet()){//if the goal is met
                wg.remove();//remove the goal
                GoalDone.setText("Congratulations, You completed your goal!");//message about completed goal
            }
        }

        int calRemaining = 0;
        if (User.getHeight()>0 && User.getWeight()>0){//if the user has entered a weight and a height
            cal1.setVisible(true);
            cal2.setVisible(true);
            long ageMs = new Date().getTime() - User.getDOB().getTime();
            int age = Integer.parseInt(Long.toString(ageMs/31536000000L));//calculate age in years
            int s = 5;//part of the equation
            if (User.getGender()=='F'){
                s=-161;
            }
            int toMaintainCal = (int)(10*User.getWeight() + 6.25*User.getHeight() - (5 * age) + s);//Mifflin-St. Jeor equation
            User.setCal((int)(toMaintainCal * 1.2));//multiplyed by 1.2 as that is roughly how many calories people burn from walking around each day and other exercise that is not going to be added in
            //do a percentage of the calories based upon how far, away thier goal is
            if (!allGoals.isEmpty()){//there is goals
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
            User.update();//update the caloreis on the user in the database
            //set up calories for the day
            int totalCal = User.getCal();
            int calBurned = 0;
            ArrayList<ExerciseSession> exerciseLinks = ExerciseSession.getDays(User,new Date());//get todays exercise sessions
            for (ExerciseSession el:exerciseLinks){//for all exercises session that day
                calBurned += el.getCaloriesBurned();//add up the caliroes burned
            }
            int calConsumed = 0;
            ArrayList<Meal> meals = Meal.getDays(User,new Date());
            for (Meal m:meals){//for all meals that day
                calConsumed += (m.getCalories());
            }
            calRemaining = (totalCal-calConsumed+calBurned);//work out calories remaining for that day
            calLeft.setText(totalCal + " - " + calConsumed + " + " + calBurned + " = " + calRemaining);
        } else {
            cal1.setVisible(false);
            cal2.setVisible(false);
        }
        //suggestion label
        ArrayList<String> suggestions = new ArrayList<>();
        if (User.getHeight()==0){//if the user hasnt entered a height
            suggestion.setText("Try going to Add Measurements and entering your height");//suggest they add a height
        } else if (User.getWeight()==0){//if the user hasnt entered a weight
            suggestion.setText("Try going to Add Measurements and enter your weight");//suggest they do so
        } else {
            Map.Entry<Date,Double> entry = User.getAllWeights().entrySet().iterator().next();//gets all the weights of the user
            if(entry.getKey().getTime()<(((new Date()).getTime())-86400000L)){//weight not entered today
                suggestions.add("Try entering your weight for today");
            }
            if (WeightGoal.getAll(User).isEmpty()){//if the user has no goals
                suggestions.add("Try going to add goal and setting a personal goal");
            }
            if (calRemaining>100){//if the user has calories left to eat
                suggestions.add("Remember you have more calories to eat");
            }
            if (calRemaining<-100){//the user has over eaten
                suggestions.add("Oh dear, You have over eaten, try doing an exercise");
            }
            if (suggestions.isEmpty()){//if all the above conditions are met
                suggestions.add("Go do some exercise, come back and add it");
                suggestions.add("Well Done!!");
                suggestions.add("Keep working towards your goals");
                suggestions.add("Try setting another goal to work towards");
            }
        }
        if (suggestion.getText().equals("")){
            Random random = new Random();
            suggestion.setText(suggestions.get(random.nextInt(suggestions.size())));//pick a random suggestion
        }
        //exercise bar chart
        XYChart.Series exerciseSessionSeries = new XYChart.Series();
        exerciseSessionSeries.setName("Calories");
        //hashmap
        HashMap<String,Integer> exerciseSessionTypeChartData = new HashMap<>();//datahashmap
        for (int i = 0;i<14;i++){//up to 14 days ago
            ArrayList<ExerciseSession> day = ExerciseSession.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            for (ExerciseSession s:day){//for the day
                if(exerciseSessionTypeChartData.containsKey(s.getExerciseName())){//if the map already contains the exercise type
                    exerciseSessionTypeChartData.put(s.getExerciseName(),exerciseSessionTypeChartData.get(s.getExerciseName()) + s.getCaloriesBurned());//add the value back in with the added calories
                } else {
                    exerciseSessionTypeChartData.put(s.getExerciseName(),s.getCaloriesBurned());//add a new item into the hashmap
                }
            }
        }
        for (Map.Entry<String,Integer> entry : exerciseSessionTypeChartData.entrySet()){//for each element of the hashmap
            exerciseSessionSeries.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));//adding the data to the series
        }
        if (exerciseSessionTypeChartData.isEmpty()){//if there is no data
            ExerciseBar.setVisible(false);//hide
        } else {
            ExerciseBar.setVisible(true);//set to visible
            ExerciseBar.getData().addAll(exerciseSessionSeries);//add the series to the bar chart
        }
        //pie chart
        HashMap<String,Integer> dataPie = new HashMap<>();//make hashmap of the meals for the pie chart
        for (int i = 0;i<14;i++){//for all the days up to 13 days ago
            ArrayList<Meal> day = Meal.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));//get i days ago meals meals
            for (Meal m:day){//for all the meals on that day
                if(dataPie.containsKey(m.getType())){//if the data already contains the mealtype
                    dataPie.put(m.getType(),exerciseSessionTypeChartData.get(m.getType()) + m.getCalories());//add to the calories
                } else {
                    dataPie.put(m.getType(),m.getCalories());//make a new mealtype section
                }
            }
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();//data observable list create
        for (Map.Entry<String,Integer> entry : dataPie.entrySet()){//for all the elements in the hashmap
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));//add them to the data
        }
        if (pieChartData.isEmpty()){
            ConsumedCal.setVisible(false);
        } else {
            ConsumedCal.setVisible(true);
            ConsumedCal.getData().addAll(pieChartData);//add the data to the pie chart
        }

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
