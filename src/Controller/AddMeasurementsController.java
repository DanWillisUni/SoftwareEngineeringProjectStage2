package Controller;

//javafx imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class AddMeasurementsController extends GenericController{
    private Model.User User;//User logged in
    @FXML private Label name;//label with the users name in
    @FXML private TextField weight;//weight textbox
    @FXML private Label errorMsg;//where error messages are displayed
    @FXML private TextField height;//height box
    @FXML private Label currentWeight;//current weight
    @FXML private Label currentHeight;//current height
    @FXML private Label heightConversion;//height conversion to cm from the dropdowns
    @FXML private ComboBox Feet;//feet to convert
    @FXML private ComboBox Inches;//inches to convert

    /**
     * Set the user
     * @param User current user that is signed in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the display for the user
     * Sets up the feet dropdown
     * Sets up the inches dropdown
     * Calculates the cm value of the feet and inches
     * Get current weight
     * Get current height
     * @param feet feet dropdown
     * @param inches inches dropdown
     */
    public void setUpDisplay(int feet,int inches){
        name.setText("Hi, " + User.getForename());
        ArrayList<Integer> feetArr = new ArrayList<Integer>();
        for (int i = 0;i<10;i++){
            feetArr.add(i);//add 0 to 9 to feet
        }
        ObservableList<Integer> observableList = (ObservableList<Integer>) FXCollections.observableList(feetArr);//create feet converter observable list
        Feet.setItems(observableList);//dropdown for feet setup
        ArrayList<Integer> inchesArr = new ArrayList<Integer>();
        inchesArr.addAll(feetArr);//add all the feet to the inches array
        for (int i = 10;i<12;i++){
            inchesArr.add(i);//add 10 and 11 to the array for inches
        }
        ObservableList<Integer> observableListInch = (ObservableList<Integer>) FXCollections.observableList(inchesArr);//set observableList to inches
        Inches.setItems(observableListInch);//set inches dropdown

        if (User.getHeight()>0){//if the user has entered a height
            currentHeight.setText(Integer.toString(User.getHeight()));//set the current height
        }
        if (User.getWeight()>0){//if the user has entered a weight
            currentWeight.setText(Double.toString(User.getWeight()));//set the current weight
        }
        heightConversion.setText(Integer.toString((int)((30.48 * feet) + (2.54 * inches))));//set the height conversion label
    }
    /**
     * Go to the dashboard
     * @param event push the back button
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
    /**
     * Validation
     * Add weight to database
     * Go to dash
     * @param event add weight button pushed
     */
    @FXML
    private void AddWeightAction (ActionEvent event) {
        errorMsg.setText("");
        //weight validation
        if (weight.getText().matches("^([1-9][0-9]*)(.[0-9][0-9]?)?$")){
            double i = Double.parseDouble(weight.getText());
            if (i>250){
                errorMsg.setText("Error: weight greater than 250");
                weight.setText("");
            }
        } else {
            errorMsg.setText("Error: weight not positive number or over 2dp");
            weight.setText("");
        }
        if(errorMsg.getText().equals("")){
            User.addWeight(Double.parseDouble(weight.getText()));//add weight to user
            goToDash(User,event);//go to the dashboard
        }
    }
    /**
     * Validation
     * Update height
     * Go to dash
     * @param event add weight button pushed
     */
    @FXML
    private void AddHeightAction (ActionEvent event) {
        errorMsg.setText("");
        //height validation
        if (height.getText().matches("^[1-9][0-9]*$")){
            int i = Integer.parseInt(height.getText());
            if (i>250){
                errorMsg.setText("Error: height greater than 250");
                height.setText("");
            } else {
                User.setHeight(i);
            }
        } else {
            errorMsg.setText("Error: height not positive integer");
            height.setText("");
        }
        if(errorMsg.getText().equals("")){
            User.setHeight(Integer.parseInt(height.getText()));//set the height of the user
            User.update();//update the user in the database
            goToDash(User,event);//go to the dashboard
        }
    }
    /**
     * Calculate the value of cm
     * Refresh the display
     * @param event action to the feet or inches combobox
     */
    @FXML
    public void Refresh(ActionEvent event) {
        int f = 0;
        if (Feet.getValue()!=null){//if feet selected
            f= Integer.parseInt((String) Feet.getValue());//set the feet value
        }
        int i = 0;
        if (Inches.getValue()!=null){//if inches selected
            i= Integer.parseInt((String)Inches.getValue());//set inches
        }
        setUpDisplay(f,i);//refresh the display with the current values
    }
}
