package Controller;
//javafx imports
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Callback;
//java imports
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AddExerciseSessionController extends GenericController{
    private Model.User User; //person who is currently logged in
    @FXML private TextField txt_search;//search box
    @FXML private ComboBox Exercise;//exercises drop down
    @FXML private TextField duration;//duration text box
    @FXML private TextField calBurned;//calories burned text box
    @FXML private Label errorMsg;//error message label
    @FXML private Label name;
    @FXML private TableView Burned;
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
        try {
            GenericDatabaseController db = new GenericDatabaseController();
            ArrayList<String> results = db.getAllLike("","exercise","exerciseName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Exercise.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText("Hello, " + User.getForename());

        ArrayList<Model.ExerciseSession> links = ExerciseSession.getDays(User,new Date());
        ObservableList<ExerciseSession> data = FXCollections.observableArrayList();
        for (Model.ExerciseSession exerciseSession:links) {
            data.add(exerciseSession);
        }
        Burned.setEditable(true);
        TableColumn exercise = new TableColumn("Exercise");
        exercise.setMinWidth(200);
        exercise.setCellValueFactory(
                new PropertyValueFactory<ExerciseSession, String>("exerciseName"));
        TableColumn duration = new TableColumn("Duration");
        duration.setMinWidth(100);
        duration.setCellValueFactory(
                new PropertyValueFactory<ExerciseSession, String>("duration"));
        TableColumn calories = new TableColumn("Calories");
        calories.setMinWidth(100);
        calories.setCellValueFactory(
                new PropertyValueFactory<ExerciseSession, String>("caloriesBurned"));
        Burned.setPrefWidth(400);
        if (data.isEmpty()){
            Burned.setVisible(false);
        } else {
            Burned.setItems(data);
            Burned.getColumns().addAll(exercise, duration, calories);
            addButtonToTable();
        }

    }
    /**
     * Adds an end button onto the end column of the table
     * This button is used to remove any food that the user wants
     * When the button is pressed the exercise session is removed
     * The same page is then loaded
     */
    private void addButtonToTable() {
        TableColumn<ExerciseSession, Void> colBtn = new TableColumn("Delete");
        Callback<TableColumn<ExerciseSession, Void>, TableCell<ExerciseSession,Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ExerciseSession, Void> call(final TableColumn<ExerciseSession, Void> param) {
                final TableCell<ExerciseSession, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Remove");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ExerciseSession data = getTableView().getItems().get(getIndex());//get the exercise session obj that is in the table
                            data.removeLink(User,new Date());//remove link between the user and the exercise session
                            //reloads the page
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddExerciseSession.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            AddExerciseSessionController controller = loader.<AddExerciseSessionController>getController();
                            controller.setUser(User);
                            controller.setUpDisplay();
                            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                            stage.setFullScreen(true);
                            stage.show();
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
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
        Burned.getColumns().add(colBtn);
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
            ExerciseSession exerciseSession = ExerciseSession.getExerciseSession(exercise,durationNum,caloriesBurned);
            if (exerciseSession == null){
                exerciseSession = new ExerciseSession(exercise,User,durationNum,caloriesBurned);
                exerciseSession.add();
            }
            exerciseSession.setUser(User);
            exerciseSession.addLink();
            goToDash(User,event);//go to the dashboard
        }
    }
    /**
     * Get all like search box from the exercise table and put it in the drop down
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
