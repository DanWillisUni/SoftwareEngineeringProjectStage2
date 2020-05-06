package Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HistoryController extends GenericController{
    private Model.User User;//user obj
    @FXML private Label name;//name label at the top
    @FXML private LineChart WeightTracking;//linechart of weight
    @FXML private LineChart CalorieTracking;//linechart of calories consumed each day
    @FXML private LineChart BurningTracking;//linechart of calories burnt each day
    private int howManyDaysBackBeginning;//how many days back till the begining of the x axis
    private int howManyDaysBackEnd;//how many days back till the right most side of the x axis

    /**
     * Sets the user to the user that is logged in
     * @param User Person object logged in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the display
     * Resets the days back if they are too far back or too far forward
     * Get all the weights
     * If they are within that date add them to the data arraylist
     * For each element in the arraylist add them to the observable array also getting the highest and lowest
     *
     * @param howManyDaysBackBeginning how many days to the begining of the chart
     * @param howManyDaysBackEnd how many days back till the end of the chart x axis
     */
    public void setUpDisplay(int howManyDaysBackBeginning,int howManyDaysBackEnd) {
        if (howManyDaysBackEnd < -1){//if trying to go into the furture
            howManyDaysBackEnd = -1;
            howManyDaysBackBeginning = 7;
        }
        if (howManyDaysBackBeginning > 28){//if trying to go into the past more than 4 weeks
            howManyDaysBackBeginning = 28;
            howManyDaysBackEnd = 20;
        }
        this.howManyDaysBackBeginning=howManyDaysBackBeginning;//set the days
        this.howManyDaysBackEnd=howManyDaysBackEnd;//set the days
        name.setText("Welcome " + User.getForename());
        //line chart of weight
        HashMap<Date, Double> all = User.getAllWeights();
        ArrayList<Double> weights = new ArrayList<>();//gets all the weights
        ArrayList<java.util.Date> dates = new ArrayList<>();//gets all the dates
        double smallestWeight = Integer.MAX_VALUE;
        double highestWeight = Integer.MIN_VALUE;
        for (Map.Entry<Date,Double> entry : all.entrySet()){//for each weight and date in the hashmap
            if (entry.getKey().getTime()>=Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackBeginning).atStartOfDay(ZoneId.systemDefault()))).getTime()&&entry.getKey().getTime()<=Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackEnd).atStartOfDay(ZoneId.systemDefault()))).getTime()){//if it is within the dates selected
                weights.add(entry.getValue());//adds weight to array
                dates.add(entry.getKey());//adds date to the array
                if (entry.getValue()<smallestWeight){//keeps track of the smallest weight
                    smallestWeight = entry.getValue();
                }
                if (entry.getValue()>highestWeight){//keeps track of the largest weight
                    highestWeight = entry.getValue();
                }
            }
        }
        XYChart.Series weightSeries = new XYChart.Series();//create new series for the weight
        for (int i = 0; i < weights.size(); i++) {//for everything in the weight array
            weightSeries.getData().add(new XYChart.Data<Number,Double>(dates.get(i).getTime(), weights.get(i)));//puts the weights and dates into a series
        }
        weightSeries.setName("Weight");//set the series name
        WeightTracking.getData().add(weightSeries);//adds the series to the linechart
        NumberAxis yAxis = (NumberAxis) WeightTracking.getYAxis();
        //sets the smallest and largest value +/- 2.5kg as the upper and lower limits of y axis
        if (smallestWeight!=Integer.MAX_VALUE){
            yAxis.setLowerBound(smallestWeight-2.5);
        } else {
            yAxis.setLowerBound(0);
        }
        if (highestWeight!=Integer.MIN_VALUE){
            yAxis.setUpperBound(highestWeight+2.5);
        } else {
            yAxis.setUpperBound(100);
        }
        NumberAxis xAxis = (NumberAxis) WeightTracking.getXAxis();
        xAxis.setUpperBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackEnd).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis upperbound to 1 day in the future from now
        xAxis.setLowerBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackBeginning).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis lower bound to 2 weeks ago
        xAxis.setTickLabelFormatter(numberToDate);//set the number to be formatted as a date

        ArrayList<Integer> Cal = new ArrayList<>();//create new array for calories
        ArrayList<java.util.Date> datesCal = new ArrayList<>();//gets all the dates
        int smallestCal = Integer.MAX_VALUE;
        int highestCal = Integer.MIN_VALUE;
        for (int i = howManyDaysBackEnd;i<howManyDaysBackBeginning;i++){//for all the days
            ArrayList<Meal> meals = Meal.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));//get meals on this day
            int totalCal = 0;//set the total for that day to 0
            for (Meal meal:meals){//for all meals
               totalCal= meal.getCalories();//add the calories of the meal to the total
            }
            if (totalCal>0){//if the total calories is more than 0
                //if the calories are higher or smaller than the pervious highest or smallest update accordingly
                if (totalCal>highestCal){
                    highestCal = totalCal;
                }
                if (totalCal<smallestCal){
                    smallestCal = totalCal;
                }
                Cal.add(totalCal);//add the total calories to the calories array
                datesCal.add(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));//add the date to the date array
            }
        }
        XYChart.Series ConsumptionSeries = new XYChart.Series();
        for (int i = 0; i < datesCal.size(); i++) {//for all the dates in the arraylist
            ConsumptionSeries.getData().add(new XYChart.Data<Number,Number>(datesCal.get(i).getTime(), Cal.get(i)));//puts the calories and dates into a series
        }
        ConsumptionSeries.setName("Calories Consumed");
        CalorieTracking.getData().add(ConsumptionSeries);//adds the series to the linechart
        NumberAxis yAxisCal = (NumberAxis) CalorieTracking.getYAxis();
        //set the upper and lower bounds accoridingly to the highest and lowest
        if (smallestCal!=Integer.MAX_VALUE){
            yAxisCal.setLowerBound(smallestCal-50.0);
        } else {
            yAxisCal.setLowerBound(0);
        }
        if (highestCal!=Integer.MIN_VALUE){
            yAxisCal.setUpperBound(highestCal+50.0);
        } else {
            yAxisCal.setUpperBound(100);
        }
        NumberAxis xAxisCal = (NumberAxis) CalorieTracking.getXAxis();
        xAxisCal.setUpperBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackEnd).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis upperbound to 1 day in the future from now
        xAxisCal.setLowerBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackBeginning).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis lower bound to 2 weeks ago
        xAxisCal.setTickLabelFormatter(numberToDate);//format the number to be in date format

        ArrayList<Integer> Burn = new ArrayList<>();//gets all the calories
        ArrayList<java.util.Date> datesBurn = new ArrayList<>();//gets all the dates
        int highestBurn = Integer.MIN_VALUE;
        int smallestBurn = Integer.MAX_VALUE;
        for (int i = howManyDaysBackEnd;i<howManyDaysBackBeginning;i++){
            ArrayList<ExerciseSession> sessions = ExerciseSession.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            int totalCal = 0;
            for (ExerciseSession s:sessions){//forall sessions
                totalCal+= s.getCaloriesBurned();//add to the total
            }

            if (totalCal>0){//if there was exercise that day
                //get the highest and lowest comparison for upper and lower bounds
                if (totalCal>highestCal){
                    highestBurn = totalCal;
                }
                if (totalCal < smallestBurn){
                    smallestBurn = totalCal;
                }
                Burn.add(totalCal);//add to the arraylist
                datesBurn.add(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));//add the date to the array list
            }
        }
        XYChart.Series seriesBurn = new XYChart.Series();
        for (int i = 0; i < datesBurn.size(); i++) {//add the data to the
            seriesBurn.getData().add(new XYChart.Data<Number,Number>(datesBurn.get(i).getTime(), Burn.get(i)));//puts the calories and dates into a series
        }
        seriesBurn.setName("Calories Burnt");
        BurningTracking.getData().add(seriesBurn);//adds the series to the linechart
        NumberAxis yAxisBurn = (NumberAxis) BurningTracking.getYAxis();
        //setting upper and lower bound of the y axis
        if (highestBurn!=Integer.MIN_VALUE){
            yAxisBurn.setUpperBound(highestBurn+50.0);
        } else {
            yAxisBurn.setUpperBound(100);
        }
        if (smallestBurn==Integer.MAX_VALUE) {
            yAxisBurn.setLowerBound(0);
        } else if (smallestBurn-50 > 0) {
            yAxisBurn.setLowerBound(smallestBurn - 50);
        } else {
            yAxisBurn.setLowerBound(0);
        }
        NumberAxis xAxisBurn = (NumberAxis) BurningTracking.getXAxis();
        xAxisBurn.setUpperBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackEnd).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis upperbound to 1 day in the future from now
        xAxisBurn.setLowerBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackBeginning).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis lower bound to 2 weeks ago
        xAxisBurn.setTickLabelFormatter(numberToDate);//setting the number to date string formatter
    }
    StringConverter<Number> numberToDate = new StringConverter<Number>() {//create a tick label formatter
        @Override
        public String toString(Number n) {//override number tostring method converts it to a date
            long i = n.longValue();//converts the number to a long
            java.util.Date date = new Date(i);//creates a new Date obj
            DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");//puts the date object into this format
            return  newFormat.format(date);
        }
        @Override
        public Number fromString(String string) {//has to be here however isnt used so i didnt write it properly
            return 0;
        }//not used but required
    };

    /**
     * Sets up display with adjusted days to go a week in the past
     * @param event past button pressed
     */
    @FXML
    private void GoToPastButtonAction (ActionEvent event) {
        setUpDisplay(howManyDaysBackBeginning+7,howManyDaysBackBeginning-1);
    }
    /**
     * Set up the display with adjusted numbers to go one week into the future
     * @param event future button pressed
     */
    @FXML
    private void GoToFutureButtonAction (ActionEvent event) {
        setUpDisplay(howManyDaysBackEnd+1,howManyDaysBackEnd-7);
    }
    /**
     * Go to the dashboard
     * @param event push the back button
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
}
