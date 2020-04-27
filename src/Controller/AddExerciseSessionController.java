package Controller;
//javafx imports
import Model.Exercise;
import Model.ExerciseLink;
import Model.ExerciseSession;
import Model.GenericDatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
//java imports
import java.util.ArrayList;

public class AddExerciseSessionController extends GenericController{
    private Model.User User; //person who is currently logged in
    @FXML private TextField txt_search;//search box
    @FXML private ComboBox Exercise;//exercises drop down
    @FXML private TextField duration;//duration text box
    @FXML private TextField calBurned;//calories burned text box
    @FXML private Label errorMsg;//error message label
    @FXML private Label name;
    /**
     * Sets the current user that is signed into the health tracker
     * @param User Person that is signed in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the drop down for all the exercises
     */
    public void setUpDisplay(){
        try {
            GenericDatabaseController db = new GenericDatabaseController();
            ArrayList<String> results = db.getAllLike("","exercise","exerciseName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Exercise.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText("Hello, " + User.getForename());
    }
    /**
     * Loads the dashboard on the push of the back button
     * @param event button push
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
     /**
     * Adds exercise when button pushed if it is valid otherwise it displays an error message
     * @param event button push
     */
    @FXML
    private void AddExerciseSessionAction (ActionEvent event) {
        errorMsg.setText("");
        GenericDatabaseController db = new GenericDatabaseController();
        boolean validCal = false;
        Boolean validSport = false;

        //validation of calories burned
        if (!calBurned.getText().equals("")){
            if (calBurned.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
                int i = Integer.parseInt(calBurned.getText());
                if (i>2500){
                    errorMsg.setText("Error: calories > 2500");
                    calBurned.setText("");
                } else {
                    validCal = true;
                }
            } else {
                errorMsg.setText("Error: calories not positive number");
                calBurned.setText("");
            }
        }
        //validate sport
        if (validCal){
            if (Exercise.getValue()!=null&&!Exercise.getValue().toString().equals("")){
                if(!db.isInTable(Exercise.getValue().toString(),"exercise","exerciseName")){
                    errorMsg.setText("Error: No such sport found, try other");
                    Exercise.setValue("");
                } else {
                    validSport = true;
                }
            }
        } else {
            if (Exercise.getValue()==null) {
                errorMsg.setText("Error: Sport not selected");
            }else if(Exercise.getValue().toString().equals("")){
                errorMsg.setText("Error: Sport not typed in");
            } else {
                if(!db.isInTable(Exercise.getValue().toString(),"exercise","exerciseName")){
                    errorMsg.setText("Error: No such sport found, try other");
                    Exercise.setValue("");
                } else {
                    validSport = true;
                }
            }
        }

        //validate the duration
        if (duration.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            int i = Integer.parseInt(duration.getText());
            if (i>240){
                errorMsg.setText("Error: duration greater than 4 hours");
                duration.setText("");
            }
        } else {
            errorMsg.setText("Error: duration not a positive number");
            duration.setText("");
        }
        //if no errors in validation
        if (errorMsg.getText()==""){
            int caloriesBurned = 0;
            int durationNum = Integer.parseInt(duration.getText());//set the duration
            Exercise exercise = new Exercise(0,"Other",0);
            //set the sport if it is valid
            if (validSport){
                exercise = Model.Exercise.getExerciseFromName(Exercise.getValue().toString());
                if (!validCal){
                    caloriesBurned = durationNum*(exercise.getCaloriesBurned());//if the calories are not valid, calculate them
                }
            }
            //set the calories if they are valid
            if (validCal){
                caloriesBurned = Integer.parseInt(calBurned.getText());
            }
            ExerciseSession exerciseSession = ExerciseSession.getExerciseSession(exercise,durationNum,caloriesBurned);
            if (exerciseSession==null){
                exerciseSession = new ExerciseSession(exercise,durationNum,caloriesBurned);
                exerciseSession.add();
            }
            ExerciseLink link = new ExerciseLink(exerciseSession,User);
            link.add();
            goToDash(User,event);//go to the dashboard
        }
    }
    /**
     * get all like search box from the exercise table and put it in the drop down
     * @param event button pushed
     */
    @FXML
    private void goSearch(ActionEvent event) {
        try {
            String toSearch = txt_search.getText();
            GenericDatabaseController db = new GenericDatabaseController();
            ArrayList<String> results = db.getAllLike(toSearch,"exercise","exerciseName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Exercise.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
