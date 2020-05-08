package Controller;
//javafx imports
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
//java imports
import java.util.ArrayList;
import java.util.Date;

public class AddExerciseSessionController extends GenericController{
    private Model.User User; //person who is currently logged in
    @FXML private TextField txt_search;//search box
    @FXML private ComboBox Exercise;//exercises drop down
    @FXML private TextField duration;//duration text box
    @FXML private TextField calBurned;//calories burned text box
    @FXML private Label errorMsg;//error message label
    @FXML private Label name;//name of the user
    @FXML private TableView Burned;//table of all exercise session that day

    /**
     * Sets the current user that is signed into the health tracker
     * @param User Person that is signed in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the drop down for all the exercises
     * Fills the table with all the exercise that has been done that day
     * If there is none then it hides the table
     */
    public void setUpDisplay(){
        //setting up the search box
        try {
            GenericController db = new GenericController();
            ArrayList<String> results = db.getAllLike("","exercise","exerciseName");//get all exercises with a name like "", this gets all of them
            ObservableList<String> observableList = FXCollections.observableList(results);//put the results into an observable list
            Exercise.setItems(observableList);//set the drop down to the observable list
        } catch (Exception e) {
            e.printStackTrace();
        }
        //clears all
        errorMsg.setText("");
        calBurned.setText("");
        duration.setText("");
        Exercise.setValue("");
        name.setText("Hello, " + User.getUsername());//setting name at the top of the page
        //setting up the tableview
        ArrayList<Model.ExerciseSession> links = ExerciseSession.getDays(User,new Date());//get all of todays exercise sessions
        ObservableList<ExerciseSession> data = FXCollections.observableArrayList();
        for (Model.ExerciseSession exerciseSession:links) {//for each of the exercise sessions
            data.add(exerciseSession);//add them to the data
        }
        if (data.isEmpty()){//if there is no exercise sessions to display
            Burned.setVisible(false);//hide the tableview
        } else {
            Burned.setVisible(true);//show the tableview
            Burned.setEditable(true);//enable editing on the tableview
            TableColumn exercise = new TableColumn("Exercise");//create a new column
            exercise.setMinWidth(200);
            exercise.setCellValueFactory(
                    new PropertyValueFactory<ExerciseSession, String>("exerciseName"));//set the column to ExerciseSession.getExerciseName()
            TableColumn duration = new TableColumn("Duration");//creates a new column called Duration
            duration.setMinWidth(100);
            duration.setCellValueFactory(
                    new PropertyValueFactory<ExerciseSession, String>("duration"));//sets all the values to ExerciseSession.getDuration()
            TableColumn calories = new TableColumn("Calories");//creates a new column
            calories.setMinWidth(100);
            calories.setCellValueFactory(
                    new PropertyValueFactory<ExerciseSession, String>("caloriesBurned"));//sets the column to ExerciseSession.getCaloriesBurned()
            Burned.setPrefWidth(400);
            Burned.setItems(data);//actually setting the items of the tableview
            Burned.getColumns().setAll(exercise, duration, calories);//setting the columns
            addButtonToTable();//adds the remove button onto the end of the table
        }
    }
    /**
     * Adds an end button onto the end column of the table
     * This button is used to remove any food that the user wants
     * When the button is pressed the exercise session is removed
     * The same page is then loaded
     */
    private void addButtonToTable() {
        TableColumn<ExerciseSession, Void> colBtn = new TableColumn("Delete");//creates a column
        Callback<TableColumn<ExerciseSession, Void>, TableCell<ExerciseSession,Void>> cellFactory = new Callback<>() {//creates a callback
            @Override
            public TableCell<ExerciseSession, Void> call(final TableColumn<ExerciseSession, Void> param) {//impliment methods of what to do with the call
                final TableCell<ExerciseSession, Void> cell = new TableCell<>() {//made a cell
                    private final Button btn = new Button("Remove"); //put a button in the cell
                    {
                        btn.setOnAction((ActionEvent event) -> {//on action of the button
                            ExerciseSession data = getTableView().getItems().get(getIndex());//get the exercise session obj that is in the table
                            User.removeExerciseSessionLink(new Date(),data);//remove link between the user and the exercise session
                            setUpDisplay();//refresh the page
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {//implement other method
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        Burned.getColumns().add(colBtn);//adds the column to the table
    }

    /**
     * Get all like search box from the exercise table and put it in the drop down
     * @param event button pushed
     */
    @FXML
    private void goSearch(ActionEvent event) {
        try {
            String toSearch = txt_search.getText();//get what is in the search box
            GenericController db = new GenericController();
            ArrayList<String> results = db.getAllLike(toSearch,"exercise","exerciseName");//search the database for any LIKE
            ObservableList<String> observableList = FXCollections.observableList(results);//put the results in an observable list
            Exercise.setItems(observableList);//reset the dropdown to the list of exercises found
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     /**
     * Adds exercise when button pushed if it is valid otherwise it displays an error message
      * If it is valid add the exercise session if it isnt already in the table
      * Then add a link between the session and the user
      * Refreshes the page
     * @param event button push
     */
    @FXML
    private void AddExerciseSessionAction (ActionEvent event) {
        errorMsg.setText("");
        GenericController db = new GenericController();
        boolean validCal = false;
        Boolean validSport = false;

        //validation of calories burned
        if (!calBurned.getText().equals("")){
            if (calBurned.getText().matches("^[1-9][0-9]*$")){
                int i = Integer.parseInt(calBurned.getText());
                if (i>2500){
                    errorMsg.setText("Error: calories > 2500");
                    calBurned.setText("");
                } else {
                    validCal = true;
                }
            } else {
                errorMsg.setText("Error: calories not positive integer number");
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
        if (duration.getText().matches("^[1-9][0-9]*$")){
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
                    if (User.getWeight()>0){
                        caloriesBurned = (int)((User.getWeight()/62.0) * caloriesBurned);//burns more calories the more you weight
                    }
                }
            }
            //set the calories if they are valid
            if (validCal){
                caloriesBurned = Integer.parseInt(calBurned.getText());
            }
            ExerciseSession exerciseSession = ExerciseSession.getExerciseSession(exercise,durationNum,caloriesBurned);//get the exercise session
            if (exerciseSession == null){//if it doesnt exist
                exerciseSession = new ExerciseSession(exercise,durationNum,caloriesBurned);//make a new session
                exerciseSession.add();//add it to the database
            }
            User.addExerciseSessionLink(exerciseSession);//add the link to the session to the database
            setUpDisplay();//refresh the page
        }
    }
    /**
     * Loads the dashboard on the push of the back button
     * @param event button push
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
}
