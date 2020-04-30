package Controller;
//my imports
//fx imports
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AddGoalController extends GenericController{
    private Model.User User;
    @FXML private TextField TargetWeight;
    @FXML private DatePicker targetDate;
    @FXML private Label errorMsg;
    @FXML private Label name;
    @FXML private ComboBox GoalType;
    @FXML private TableView Goals;
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

        ArrayList<Model.WeightGoal> goals = Model.WeightGoal.getAll(User);
        ObservableList<WeightGoal> data = FXCollections.observableArrayList();
        for (Model.WeightGoal wg:goals) {
            data.add(wg);
        }
        Goals.setEditable(true);
        TableColumn target = new TableColumn("Target Weight");
        target.setMinWidth(100);
        target.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("targetWeight"));
        TableColumn DateDue = new TableColumn("Due Date");
        DateDue.setMinWidth(150);
        DateDue.setCellValueFactory(
                new PropertyValueFactory<WeightGoal, String>("due"));

        Goals.setItems(data);
        Goals.getColumns().addAll(target, DateDue);
        addButtonToTable();
    }
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
        //validate goal type
        if (GoalType.getValue()==null) {
            errorMsg.setText("Error: goal type not selected");
        }else if(GoalType.getValue().toString().equals("")){
            errorMsg.setText("Error: goal type not typed in");
        } else {
            if(!(GoalType.getValue().toString().equals("Above")||GoalType.getValue().toString().equals("Below"))){
                errorMsg.setText("Error: not valid goal type");
                GoalType.setValue("");
            }
        }
        if (errorMsg.getText().equals("")){
            WeightGoal g = new WeightGoal(User,Integer.parseInt(TargetWeight.getText()),Date.from(Instant.from(targetDate.getValue().atStartOfDay(ZoneId.systemDefault()))),(GoalType.getValue().toString().equals("Above")));
            g.add();
            goToDash(User,event);
        }
    }
}
