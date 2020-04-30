package Controller;

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

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DashboardController extends GenericController{
    private Model.User User;
    @FXML private Label name;
    @FXML private Label calLeft;
    @FXML Label GoalDone;
    @FXML private Label nextGoal;
    @FXML private Label BMI;
    @FXML private TableView Burned;
    @FXML private TableView Consumed;
    /**
     * sets the user to the user that is logged in
     * @param User Person object logged in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the display
     * gets any goals that have expired or been competed and removes them with a message
     * gets bmi
     * gets the calories of that day both consumed and burned and works out calories left
     * gets the weight of the upcoming goal
     * gets the weights and dates
     * only displays the chart of the last 2 weeks to track weight
     */
    public void setUpDisplay() {
        name.setText("Welcome " + User.getForename());
        int totalCal = 1800;
        if (User.getGender() == 'M') {//men eat more calories so start with 2000 calories whereas women start with 1800
            totalCal = 2000;
        }

        if (User.getHeight()>0 && User.getWeight()>0){
            DecimalFormat df = new DecimalFormat("#.##");//format of decimal of bmi
            df.setRoundingMode(RoundingMode.CEILING);
            double bmi = User.getWeight()/Math.pow((double)(User.getHeight()/100.0),2.0);//works out bmi
            if (bmi > 0){
                BMI.setText("Your BMI is: " + df.format(bmi));//sets bmi label
            }
        }

        ArrayList<WeightGoal> allGoals = WeightGoal.getAll(User);
        for(WeightGoal wg: allGoals){
            if (wg.isMet()){
                wg.remove();
                GoalDone.setText("Congratulations, You completed your goal!");
            }
        }
        if (!allGoals.isEmpty()){
            nextGoal.setText("The next goal is: " + Integer.toString(allGoals.get(0).getTargetWeight()) + "kg, on " + allGoals.get(0).getDue());
        }

        int calBurned = 0;
        ArrayList<ExerciseLink> exerciseLinks = ExerciseLink.getTodays(User);
        for (ExerciseLink el:exerciseLinks){
            calBurned += el.getSession().getCaloriesBurned();
        }
        int calConsumed = 0;
        ArrayList<Diet> diets = Diet.getTodays(User);
        for (Diet d:diets){
            calConsumed += (d.getMeal().getQuantity()) * (d.getMeal().getFood().getAmountOfCalories());
        }
        calLeft.setText(totalCal + " - " + calConsumed + " + " + calBurned + " = " + (totalCal-calConsumed+calBurned));

        ArrayList<Model.ExerciseLink> links = ExerciseLink.getTodays(User);
        ObservableList<ExerciseDisplay> data = FXCollections.observableArrayList();
        for (Model.ExerciseLink l:links) {
            data.add(new ExerciseDisplay(l.getSession()));
        }
        Burned.setEditable(true);
        TableColumn exercise = new TableColumn("Exercise");
        exercise.setMinWidth(200);
        exercise.setCellValueFactory(
                new PropertyValueFactory<ExerciseDisplay, String>("name"));
        TableColumn duration = new TableColumn("Duration");
        duration.setMinWidth(100);
        duration.setCellValueFactory(
                new PropertyValueFactory<ExerciseDisplay, String>("duration"));
        TableColumn calories = new TableColumn("Calories");
        calories.setMinWidth(100);
        calories.setCellValueFactory(
                new PropertyValueFactory<ExerciseDisplay, String>("cal"));
        Burned.setItems(data);
        Burned.getColumns().addAll(exercise, duration, calories);
        addButtonToExerciseTable();

        ArrayList<Model.Diet> todaysFood = Model.Diet.getTodays(User);
        ObservableList<MealEaten> dataFood = FXCollections.observableArrayList();
        for (Model.Diet d:todaysFood) {
            dataFood.add(d.getMeal().getMealEaten());
        }
        Consumed.setEditable(true);
        TableColumn name = new TableColumn("Name");
        name.setMinWidth(200);
        name.setCellValueFactory(
                new PropertyValueFactory<MealEaten, String>("foodName"));
        TableColumn quantity = new TableColumn("Quantity");
        quantity.setMinWidth(100);
        quantity.setCellValueFactory(
                new PropertyValueFactory<MealEaten, String>("quantity"));
        TableColumn caloriesCol = new TableColumn("Calories");
        caloriesCol.setMinWidth(100);
        caloriesCol.setCellValueFactory(
                new PropertyValueFactory<MealEaten, String>("calories"));
        Consumed.setItems(dataFood);
        Consumed.getColumns().addAll(name, quantity, caloriesCol);
        addButtonToFoodTable();
    }
    private void addButtonToExerciseTable() {
        TableColumn<ExerciseDisplay, Void> colBtn = new TableColumn("Delete");
        Callback<TableColumn<ExerciseDisplay, Void>, TableCell<ExerciseDisplay, Void>> cellFactory = new Callback<TableColumn<ExerciseDisplay, Void>, TableCell<ExerciseDisplay, Void>>() {
            @Override
            public TableCell<ExerciseDisplay, Void> call(final TableColumn<ExerciseDisplay, Void> param) {
                final TableCell<ExerciseDisplay, Void> cell = new TableCell<ExerciseDisplay, Void>() {
                    private final Button btn = new Button("Remove");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ExerciseDisplay data = getTableView().getItems().get(getIndex());
                            ExerciseSession s = data.getSession();
                            ExerciseLink l = ExerciseLink.getLink(User,s);
                            l.remove();
                            GenericController.goToDash(User,event);
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
    private void addButtonToFoodTable() {
        TableColumn<MealEaten, Void> colBtn = new TableColumn("Delete");
        Callback<TableColumn<MealEaten, Void>, TableCell<MealEaten, Void>> cellFactory = new Callback<TableColumn<MealEaten, Void>, TableCell<MealEaten, Void>>() {
            @Override
            public TableCell<MealEaten, Void> call(final TableColumn<MealEaten, Void> param) {
                final TableCell<MealEaten, Void> cell = new TableCell<MealEaten, Void>() {
                    private final Button btn = new Button("Remove");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            MealEaten data = getTableView().getItems().get(getIndex());
                            Meal m = data.getMeal();
                            Diet d = Diet.getDiet(User,m);
                            d.remove();
                            GenericController.goToDash(User,event);
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
        Consumed.getColumns().add(colBtn);
    }

    /**
     * take the user to the add weight button
     * @param event button pushed to add weight
     */
    @FXML
    private void GoToAddWeightButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddWeight.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddWeightController controller = loader.<AddWeightController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * goes to the add exercise page
     * @param event add exercise button pressed
     */
    @FXML
    private void GoToAddExerciseSessionButtonAction (ActionEvent event) {
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
    }
    /**
     * goes to the add food page
     * @param event add food button pushed
     */
    @FXML
    private void GoToAddFoodButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddFood.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddFoodController controller = loader.<AddFoodController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * goes to the add goal page
     * @param event add goal button pressed
     */
    @FXML
    private void GoToAddGoalButtonAction (ActionEvent event) {
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
    }
    @FXML
    private void GoToPersonalDetailsButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/PersonalDetails.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        PersonalDetailsController controller = loader.<PersonalDetailsController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * signs out the user by not passing the user and going to login page
     * @param event sign out button pushed
     */
    @FXML
    private void GoToSignOutButtonAction (ActionEvent event) {
        goToPage("../View/Login.fxml",event);
    }
}
