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
    private Model.User User;
    @FXML private Label name;
    @FXML private TextField weight;
    @FXML private Label errorMsg;
    @FXML private TextField height;
    @FXML private Label currentWeight;
    @FXML private Label currentHeight;
    @FXML private Label heightConversion;
    @FXML private ComboBox Feet;
    @FXML private ComboBox Inches;
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
     * validation
     * add weight
     * go to dash
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
        System.out.println(f + "," + i);
        setUpDisplay(f,i);
    }
}
