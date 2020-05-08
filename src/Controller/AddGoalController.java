package Controller;

import Model.WeightGoal;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AddGoalController extends GenericController{
    private Model.User User;
    @FXML private TextField TargetWeight;
    @FXML private DatePicker targetDate;
    @FXML private Label errorMsg;
    @FXML private Label name;
    @FXML private TableView Goals;
    @FXML private TableView SuggestedGoals;

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
        name.setText("Hello, " + User.getUsername());//setting name at the top of the page

        double perfectWeight = (22.5 * Math.pow((double)(User.getHeight()/100.0),2.0));//works out the users perfect weight using bmi and height
        int multiplyer = 1;
        if (perfectWeight < User.getWeight()){//determines if the user is above or below thier perfect weight
            multiplyer = -1;
        }
        ArrayList<Model.WeightGoal> goalsSuggestion = new ArrayList<>();//create new arraylist to put all the weight goals obj in
        if (User.getWeight()==perfectWeight){//if the user is thier perfect weight
            goalsSuggestion.add(new WeightGoal(User,perfectWeight,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays(7).atStartOfDay(ZoneId.systemDefault()))),true));//set a suggestion goal to maintain for a week
        } else {
            boolean toLoose = multiplyer==1;
            for (double i = 0.25;i<=1.5;i=i+0.25){//for 1.5 kg towards thier ideal weight at 0.25 increments
                if (!closerThan(User.getWeight(),User.getWeight()+(i*multiplyer),perfectWeight)){//if it is closer
                    //add 3 different suggestions with 3 different timesscales
                    goalsSuggestion.add(new WeightGoal(User,User.getWeight()+(i*multiplyer),Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays((long)(7*i/0.1)).atStartOfDay(ZoneId.systemDefault()))),toLoose));//slow
                    goalsSuggestion.add(new WeightGoal(User,User.getWeight()+(i*multiplyer),Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays((long)(7*i/0.25)).atStartOfDay(ZoneId.systemDefault()))),toLoose));//medium
                    goalsSuggestion.add(new WeightGoal(User,User.getWeight()+(i*multiplyer),Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays((long)(7*i/0.5)).atStartOfDay(ZoneId.systemDefault()))),toLoose));//fast
                }
            }
        }

        ObservableList<WeightGoal> dataSuggestion = FXCollections.observableArrayList();
        for (Model.WeightGoal wg:goalsSuggestion) {//for all the suggestions
            dataSuggestion.add(wg);//add them to the data
        }
        SuggestedGoals.setEditable(true);
        TableColumn targetSuggestion = new TableColumn("Target");//make new columns
        targetSuggestion.setMinWidth(100);
        targetSuggestion.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("targetWeight"));//use getTargetWeight()
        TableColumn DateDueSuggestion = new TableColumn("Due Date");//create new column
        DateDueSuggestion.setMinWidth(200);
        DateDueSuggestion.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("dueStr"));//use getDueStr()

        SuggestedGoals.setItems(dataSuggestion);//set the data
        SuggestedGoals.getColumns().setAll(targetSuggestion, DateDueSuggestion);//add the columns
        addButtonToTableSuggestion();//add the add button onto the rightmost column

        ArrayList<Model.WeightGoal> goals = Model.WeightGoal.getAll(User);//get all the weight goals of the user
        ObservableList<WeightGoal> data = FXCollections.observableArrayList();
        for (Model.WeightGoal wg:goals) {//for each goal
            data.add(wg);//add the goal to the data
        }
        if (data.isEmpty()){//if there is no data
            Goals.setVisible(false);//hide it
        } else {
            Goals.setVisible(true);//show the table view
            Goals.setEditable(true);
            TableColumn target = new TableColumn("Target");//create column called target
            target.setMinWidth(100);
            target.setCellValueFactory(
                    new PropertyValueFactory<WeightGoal, String>("targetWeight"));//use GetTargetWeight()
            TableColumn DateDue = new TableColumn("Due Date");//create a new due column
            DateDue.setMinWidth(200);
            DateDue.setCellValueFactory(
                    new PropertyValueFactory<WeightGoal, String>("dueStr"));//use getDueStr()
            Goals.setItems(data);//set all the data
            Goals.getColumns().setAll(target, DateDue);//add the columns
            addButtonToTable();//add the remove button column
        }
    }
    /**
     * Calculate if a is closer to t than b is closer to t
     * @param a first double
     * @param b second double
     * @param t target double
     * @return if a closer to t than b
     */
    private boolean closerThan(double a,double b,double t){
        if (Math.abs(t-a)>Math.abs(t-b)){//if the difference of t,a is greater than the difference between t and b
            return false;
        }
        return true;
    }
    /**
     * Adds a button to the table of the users goals
     * The button will remove the goal if the user pressed is
     */
    private void addButtonToTable() {
        TableColumn<WeightGoal, Void> colBtn = new TableColumn("Delete");//create a new column
        Callback<TableColumn<WeightGoal, Void>, TableCell<WeightGoal, Void>> cellFactory = new Callback<>() {//make a new callback
            @Override
            public TableCell<WeightGoal, Void> call(final TableColumn<WeightGoal, Void> param) {//impliment method
                final TableCell<WeightGoal, Void> cell = new TableCell<WeightGoal, Void>() {
                    private final Button btn = new Button("Remove");//put a button in the column
                    {
                        btn.setOnAction((ActionEvent event) -> {//on action
                            WeightGoal data = getTableView().getItems().get(getIndex());//get the weight goal obj that needs to be removed
                            data.remove();//remove the goal
                            setUpDisplay();//refresh
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
        Goals.getColumns().add(colBtn);//add the button column to the tableview
    }
    /**
     * Adds a button to the table of the suggested goals
     * This will add the goal to the database if it is pressed
     */
    private void addButtonToTableSuggestion() {
        TableColumn<WeightGoal, Void> colBtn = new TableColumn("Add");
        Callback<TableColumn<WeightGoal, Void>, TableCell<WeightGoal, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<WeightGoal, Void> call(final TableColumn<WeightGoal, Void> param) {
                final TableCell<WeightGoal, Void> cell = new TableCell<WeightGoal, Void>() {//create a ne wtable cell
                    private final Button btn = new Button("Add");//create a new button
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            WeightGoal data = getTableView().getItems().get(getIndex());//get the data in the row
                            data.add();//add the data
                            setUpDisplay();//refresh
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
        SuggestedGoals.getColumns().add(colBtn);//add the column onto the suggested goals
    }

    /**
     * Go to the dashboard
     * @param event back button pressed
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event){
        goToDash(User,event);
    }
    /**
     * Adds goal
     * Validates everything
     * Make a new weightGoal obj and add it to the database
     * @param event add goal button pressed
     */
    @FXML
    private void AddWeightGoalButtonAction (ActionEvent event) {
        errorMsg.setText("");
        //validate target weight
        if (TargetWeight.getText().matches("^([1-9][0-9]*)(.[0-9][0-9]?)?$")){
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
                if (c + 31536000000L < d){//a year ago
                    errorMsg.setText("Error: target date to far ahead");
                }
            } else {
                errorMsg.setText("Error: target date in the past or today");
            }
        }else {
            errorMsg.setText("Error: Date not selected");
        }
        if (errorMsg.getText().equals("")){
            boolean toLoose = true;
            if (User.getWeight()<Integer.parseInt(TargetWeight.getText())){//if current weight is less than target weight they want to gain weight
                toLoose=false;
            }
            WeightGoal g = new WeightGoal(User,Integer.parseInt(TargetWeight.getText()),Date.from(Instant.from(targetDate.getValue().atStartOfDay(ZoneId.systemDefault()))),toLoose);//new weight goal
            g.add();//add new weight goal to database
            goToDash(User,event);//goes to dashboard
        }
    }
}
