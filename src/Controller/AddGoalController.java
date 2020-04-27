package Controller;
//my imports
import Model.GenericDatabaseController;
//fx imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//java imports
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class AddGoalController extends GenericController{
    private Model.User User;
    @FXML private TextField TargetWeight;
    @FXML private DatePicker targetDate;
    @FXML private Label errorMsg;
    @FXML private Label name;
    /**
     * sets the user that is signed in
     * @param User person to set to
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * sets up the display
     */
    public void setUpDisplay(){
        name.setText("Hi, " + User.getForename());
    }
    /**
     * go to the dashboard
     * @param event back button pressed
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event){
        goToDash(User,event);
    }
    /**
     * adds goal
     * @param event add goal button pressed
     */
    @FXML
    private void AddWeightGoalButtonAction (ActionEvent event) {
        errorMsg.setText("");
        //validate target weight
        if (TargetWeight.getText().matches("^[1-9][0-9]*$")){
            int i = Integer.parseInt(TargetWeight.getText());
            if (i>250){
                errorMsg.setText("Error: target greater than 250");
                TargetWeight.setText("");
            }
        } else {
            errorMsg.setText("Error: target not positive integer");
            TargetWeight.setText("");
        }
        //validate target date
        if(targetDate.getValue()!=null){
            Long d = Date.from(Instant.from(targetDate.getValue().atStartOfDay(ZoneId.systemDefault()))).getTime();
            Long c = new Date().getTime();
            if (c<=d){
                if (c + 31536000000L < d){
                    errorMsg.setText("Error: target date to far ahead");
                }
            } else {
                errorMsg.setText("Error: target date in the past or today");
            }
        }else {
            errorMsg.setText("Error: Date not selected");
        }
        if (errorMsg.getText().equals("")){
//            GenericDatabaseController db = new GenericDatabaseController();
//            db.addGoal(User.getId(),Integer.parseInt(TargetWeight.getText()), Date.from(Instant.from(targetDate.getValue().atStartOfDay(ZoneId.systemDefault()))));
            goToDash(User,event);
        }
    }
}
