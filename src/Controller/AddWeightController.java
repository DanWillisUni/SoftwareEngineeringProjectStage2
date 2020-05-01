package Controller;

//javafx imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddWeightController extends GenericController{
    private Model.User User;
    @FXML private Label name;
    @FXML private TextField weight;
    @FXML private Label errorMsg;

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
