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
     */
    public void setUpDisplay(int feet,int inches){
        name.setText("Hi, " + User.getForename());
        ArrayList<Integer> feetArr = new ArrayList<Integer>();
        for (int i = 0;i<10;i++){
            feetArr.add(i);
        }
        ObservableList<Integer> observableList = (ObservableList<Integer>) FXCollections.observableList(feetArr);
        Feet.setItems(observableList);
        ArrayList<Integer> inchesArr = new ArrayList<Integer>();
        inchesArr.addAll(feetArr);
        for (int i = 10;i<13;i++){
            inchesArr.add(i);
        }
        ObservableList<Integer> observableListInch = (ObservableList<Integer>) FXCollections.observableList(inchesArr);
        Inches.setItems(observableListInch);

        if (User.getHeight()>0){
            currentHeight.setText(Integer.toString(User.getHeight()));
        }
        if (User.getWeight()>0){
            currentWeight.setText(Integer.toString(User.getWeight()));
        }
        heightConversion.setText(Integer.toString((int)((30.48 * feet) + (2.54 * inches))));
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
        if (weight.getText().matches("^[1-9][0-9]$")){
            double i = Double.parseDouble(weight.getText());
            if (i>250){
                errorMsg.setText("Error: weight greater than 250");
                weight.setText("");
            }
        } else {
            errorMsg.setText("Error: weight not positive integer");
            weight.setText("");
        }
        if(errorMsg.getText().equals("")){
            User.addWeight(Integer.parseInt(weight.getText()));
            GenericController.goToDash(User,event);
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
            User.setHeight(Integer.parseInt(height.getText()));
            User.update();
            GenericController.goToDash(User,event);
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
        if (Feet.getValue()!=null){
            f= Integer.parseInt((String) Feet.getValue());
        }
        int i = 0;
        if (Inches.getValue()!=null){
            i= (int) Inches.getValue();
        }
        setUpDisplay(f,i);
    }
}
