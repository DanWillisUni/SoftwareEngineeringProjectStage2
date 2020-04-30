package Controller;

//javafx imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
        import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
        import javafx.util.StringConverter;

        import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddWeightController extends GenericController{
    private Model.User User;
    @FXML private Label name;
    @FXML private TextField weight;
    @FXML private Label errorMsg;
    @FXML private LineChart WeightTracking;
    /**
     * set the user
     * @param User current user that is signed in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * sets up the display for the user
     */
    public void setUpDisplay(){
        name.setText("Hi, " + User.getForename());

        //line chart of weight
        HashMap<Integer,Date> all = User.getAllWeights();
        ArrayList<Integer> weights = new ArrayList<>();//gets all the weights
        ArrayList<java.util.Date> dates = new ArrayList<>();//gets all the dates
        for (Map.Entry<Integer,Date> entry : all.entrySet()){
            weights.add(entry.getKey());
            dates.add(entry.getValue());
        }
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < weights.size(); i++) {
            series.getData().add(new XYChart.Data<Number,Number>(dates.get(i).getTime(), weights.get(i)));//puts the weights and dates into a series
        }
        series.setName("Weight");
        WeightTracking.getData().add(series);//adds the series to the linechart

        NumberAxis xAxis = (NumberAxis) WeightTracking.getXAxis();
        xAxis.setUpperBound(new Date().getTime() + 86400000L);//sets the x axis upperbound to 1 day in the future from now
        xAxis.setLowerBound(new Date().getTime() - 1296000000L);//sets the x axis lower bound to 2 weeks ago
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {//create a tick label formatter
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
        });
    }
    /**
     * go to the dashboard
     * @param event push the back button
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
    /**
     * validation
     * add weight
     * go to dash
     * @param event add weight button pushed
     */
    @FXML
    private void AddWeightAction (ActionEvent event) {
        errorMsg.setText("");
        //validation
        if (weight.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            double i = Double.parseDouble(weight.getText());
            if (i>250){
                errorMsg.setText("Error: weight greater than 250");
                weight.setText("");
            }
        } else {
            errorMsg.setText("Error: weight not positive");
            weight.setText("");
        }
        if(errorMsg.getText().equals("")){
            User.addWeight(Integer.parseInt(weight.getText()));
            GenericController.goToDash(User,event);
        }
    }
}
