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
        name.setText("Hi, " + User.getForename());

        int perfectWeight = (int)(22.5 * Math.pow((double)(User.getHeight()/100.0),2.0));
        int multiplyer = 1;
        if (perfectWeight < User.getWeight()){
            multiplyer = -1;
        }
        ArrayList<Model.WeightGoal> goalsSuggestion = new ArrayList<>();
        if (User.getWeight()==perfectWeight){
            goalsSuggestion.add(new WeightGoal(User,perfectWeight,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays(7).atStartOfDay(ZoneId.systemDefault()))),true));
        } else {
            boolean toLoose = multiplyer==1;
            goalsSuggestion.add(new WeightGoal(User,User.getWeight()+multiplyer,Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays(28).atStartOfDay(ZoneId.systemDefault()))),toLoose));
            if (User.getWeight()+multiplyer!=perfectWeight){
                goalsSuggestion.add(new WeightGoal(User,User.getWeight()+(3*multiplyer),Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays(28*3).atStartOfDay(ZoneId.systemDefault()))),toLoose));
                if (User.getWeight()+(2*multiplyer)!=perfectWeight&&User.getWeight()+(3*multiplyer)!=perfectWeight){
                    goalsSuggestion.add(new WeightGoal(User,User.getWeight()+(5*multiplyer),Date.from(Instant.from(LocalDate.now(ZoneId.systemDefault()).plusDays(5*28).atStartOfDay(ZoneId.systemDefault()))),toLoose));
                }
            }
        }

        ObservableList<WeightGoal> dataSuggestion = FXCollections.observableArrayList();
        for (Model.WeightGoal wg:goalsSuggestion) {
            dataSuggestion.add(wg);
        }
        Goals.setEditable(true);
        TableColumn targetSuggestion = new TableColumn("Target");
        targetSuggestion.setMinWidth(100);
        targetSuggestion.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("targetWeight"));
        TableColumn DateDueSuggestion = new TableColumn("Due Date");
        DateDueSuggestion.setMinWidth(200);
        DateDueSuggestion.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("dueStr"));

        SuggestedGoals.setItems(dataSuggestion);
        SuggestedGoals.getColumns().addAll(targetSuggestion, DateDueSuggestion);
        addButtonToTableSuggestion();

        ArrayList<Model.WeightGoal> goals = Model.WeightGoal.getAll(User);
        ObservableList<WeightGoal> data = FXCollections.observableArrayList();
        for (Model.WeightGoal wg:goals) {
            data.add(wg);
        }
        Goals.setEditable(true);
        TableColumn target = new TableColumn("Target");
        target.setMinWidth(100);
        target.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("targetWeight"));
        TableColumn DateDue = new TableColumn("Due Date");
        DateDue.setMinWidth(200);
        DateDue.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("dueStr"));
        if (data.isEmpty()){
            Goals.setVisible(false);
        } else {
            Goals.setItems(data);
            Goals.getColumns().addAll(target, DateDue);
            addButtonToTable();
        }
    }
    /**
     * Adds a button to the table of the users goals
     * The button will remove the goal if the user pressed is
     */
    private void addButtonToTable() {
        TableColumn<WeightGoal, Void> colBtn = new TableColumn("Delete");
        Callback<TableColumn<WeightGoal, Void>, TableCell<WeightGoal, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<WeightGoal, Void> call(final TableColumn<WeightGoal, Void> param) {
                final TableCell<WeightGoal, Void> cell = new TableCell<WeightGoal, Void>() {
                    private final Button btn = new Button("Remove");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            WeightGoal data = getTableView().getItems().get(getIndex());
                            data.remove();

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddGoal.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            AddGoalController controller = loader.<AddGoalController>getController();
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
        Goals.getColumns().add(colBtn);
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
                final TableCell<WeightGoal, Void> cell = new TableCell<WeightGoal, Void>() {
                    private final Button btn = new Button("Add");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            WeightGoal data = getTableView().getItems().get(getIndex());
                            data.add();
                            goToDash(User,event);
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
        SuggestedGoals.getColumns().add(colBtn);
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
            boolean toLoose = true;
            if (User.getWeight()<Integer.parseInt(TargetWeight.getText())){
                toLoose=false;
            }
            WeightGoal g = new WeightGoal(User,Integer.parseInt(TargetWeight.getText()),Date.from(Instant.from(targetDate.getValue().atStartOfDay(ZoneId.systemDefault()))),toLoose);
            g.add();
            goToDash(User,event);
        }
    }
}
