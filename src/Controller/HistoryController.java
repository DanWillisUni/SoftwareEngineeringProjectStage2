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
    private Model.User User;
    @FXML private Label name;
    @FXML private LineChart WeightTracking;
    @FXML private LineChart CalorieTracking;
    @FXML private LineChart BurningTracking;
    private int howManyDaysBackBeginning;
    private int howManyDaysBackEnd;

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
    public void setUpDisplay(int howManyDaysBackBeginning,int howManyDaysBackEnd) {
        this.howManyDaysBackBeginning=howManyDaysBackBeginning;
        this.howManyDaysBackEnd=howManyDaysBackEnd;
        name.setText("Welcome " + User.getForename());
        //line chart of weight
        HashMap<Integer, Date> all = User.getAllWeights();
        ArrayList<Integer> weights = new ArrayList<>();//gets all the weights
        ArrayList<java.util.Date> dates = new ArrayList<>();//gets all the dates
        int smallestWeight = Integer.MAX_VALUE;
        int highestWeight = Integer.MIN_VALUE;
        for (Map.Entry<Integer,Date> entry : all.entrySet()){
            if (entry.getValue().getTime()>=Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackBeginning).atStartOfDay(ZoneId.systemDefault()))).getTime()&&entry.getValue().getTime()<=Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackEnd).atStartOfDay(ZoneId.systemDefault()))).getTime()){
                weights.add(entry.getKey());
                dates.add(entry.getValue());
                if (entry.getKey()<smallestWeight){
                    smallestWeight = entry.getKey();
                }
                if (entry.getKey()>highestWeight){
                    highestWeight = entry.getKey();
                }
            }
        }
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < weights.size(); i++) {
            series.getData().add(new XYChart.Data<Number,Number>(dates.get(i).getTime(), weights.get(i)));//puts the weights and dates into a series
        }
        series.setName("Weight");
        WeightTracking.getData().add(series);//adds the series to the linechart
        NumberAxis yAxis = (NumberAxis) WeightTracking.getYAxis();
        if (smallestWeight!=Integer.MAX_VALUE){
            yAxis.setLowerBound(smallestWeight-5.0);
        } else {
            yAxis.setLowerBound(0);
        }
        if (highestWeight!=Integer.MIN_VALUE){
            yAxis.setUpperBound(highestWeight+5.0);
        } else {
            yAxis.setUpperBound(100);
        }
        NumberAxis xAxis = (NumberAxis) WeightTracking.getXAxis();
        xAxis.setUpperBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackEnd).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis upperbound to 1 day in the future from now
        xAxis.setLowerBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackBeginning).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis lower bound to 2 weeks ago
        xAxis.setTickLabelFormatter(numberToDate);

        ArrayList<Integer> Cal = new ArrayList<>();//gets all the calories
        ArrayList<java.util.Date> datesCal = new ArrayList<>();//gets all the dates
        int smallestCal = Integer.MAX_VALUE;
        int highestCal = Integer.MIN_VALUE;
        for (int i = howManyDaysBackEnd;i<howManyDaysBackBeginning;i++){
            ArrayList<Meal> meals = Meal.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            int totalCal = 0;
            for (Meal meal:meals){
               totalCal= meal.getCalories();
            }
            if (totalCal>0){
                if (totalCal>highestCal){
                    highestCal = totalCal;
                }
                if (totalCal<smallestCal){
                    smallestCal = totalCal;
                }
                Cal.add(totalCal);
                datesCal.add(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            }
        }
        XYChart.Series seriesCal = new XYChart.Series();
        for (int i = 0; i < datesCal.size(); i++) {
            seriesCal.getData().add(new XYChart.Data<Number,Number>(datesCal.get(i).getTime(), Cal.get(i)));//puts the calories and dates into a series
        }
        seriesCal.setName("Calories Consumed");
        CalorieTracking.getData().add(seriesCal);//adds the series to the linechart
        NumberAxis yAxisCal = (NumberAxis) CalorieTracking.getYAxis();
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
        xAxisCal.setTickLabelFormatter(numberToDate);

        ArrayList<Integer> Burn = new ArrayList<>();//gets all the calories
        ArrayList<java.util.Date> datesBurn = new ArrayList<>();//gets all the dates
        int highestBurn = Integer.MIN_VALUE;
        int smallestBurn = Integer.MAX_VALUE;
        for (int i = howManyDaysBackEnd;i<howManyDaysBackBeginning;i++){
            ArrayList<ExerciseSession> sessions = ExerciseSession.getDays(User,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            int totalCal = 0;
            for (ExerciseSession s:sessions){
                totalCal+= s.getCaloriesBurned();
            }
            if (totalCal>0){
                if (totalCal>highestCal){
                    highestBurn = totalCal;
                }
                if (totalCal < smallestBurn){
                    smallestBurn = totalCal;
                }
                Burn.add(totalCal);
                datesBurn.add(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(i).atStartOfDay(ZoneId.systemDefault()))));
            }
        }
        XYChart.Series seriesBurn = new XYChart.Series();
        for (int i = 0; i < datesBurn.size(); i++) {
            seriesBurn.getData().add(new XYChart.Data<Number,Number>(datesBurn.get(i).getTime(), Burn.get(i)));//puts the calories and dates into a series
        }
        seriesBurn.setName("Calories Burnt");
        BurningTracking.getData().add(seriesBurn);//adds the series to the linechart
        NumberAxis yAxisBurn = (NumberAxis) BurningTracking.getYAxis();
        if (highestBurn!=Integer.MIN_VALUE){
            yAxisBurn.setUpperBound(highestBurn+50.0);
        } else {
            yAxisBurn.setUpperBound(100);
        }
        if (smallestBurn-50 > 0) {
            yAxisBurn.setLowerBound(smallestBurn - 50);
        } else {
            yAxisBurn.setLowerBound(0);
        }
        NumberAxis xAxisBurn = (NumberAxis) BurningTracking.getXAxis();
        xAxisBurn.setUpperBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackEnd).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis upperbound to 1 day in the future from now
        xAxisBurn.setLowerBound(Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).minusDays(howManyDaysBackBeginning).atStartOfDay(ZoneId.systemDefault()))).getTime());//sets the x axis lower bound to 2 weeks ago
        xAxisBurn.setTickLabelFormatter(numberToDate);
    }
    StringConverter<Number> numberToDate = new StringConverter<Number>() {//create a tick label formatter
        @Override
        public String toString(Number n) {//overide number tostring method converts it to a date
            long i = n.longValue();
            java.util.Date date = new Date(i);
            DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            return  newFormat.format(date);
        }
        @Override
        public Number fromString(String string) {//has to be here however isnt used so i didnt write it properly
            return 0;
        }
    };

    @FXML
    private void GoToPastButtonAction (ActionEvent event) {
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
        controller.setUpDisplay(howManyDaysBackBeginning+14,howManyDaysBackBeginning-1);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    @FXML
    private void GoToFutureButtonAction (ActionEvent event) {
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
        controller.setUpDisplay(howManyDaysBackEnd+1,howManyDaysBackEnd-14);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * go to the dashboard
     * @param event push the back button
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
}
