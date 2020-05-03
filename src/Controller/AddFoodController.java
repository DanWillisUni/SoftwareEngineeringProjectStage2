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

public class AddFoodController extends GenericController{
    private Model.User User;//the user that the food is being added to
    @FXML private TextField txt_search;
    @FXML private ComboBox Foods;
    @FXML private TextField quantity;
    @FXML private ComboBox MealType;
    @FXML private Label errorMsg;
    @FXML private Label name;
    @FXML private TableView Consumed;
    /**
     * sets the user to the user signed in
     * @param User logged in user
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * sets the drop down of food to all the food
     */
    public void setUpDisplay(){
        name.setText("Hello, " + User.getForename());
        try {
            GenericDatabaseController db = new GenericDatabaseController();
            ArrayList<String> results = db.getAllLike("","foods","foodName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Foods.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Model.Meal> todaysFood = Model.Meal.getDays(User,new Date());
        ObservableList<Meal> data = FXCollections.observableArrayList();
        for (Model.Meal m:todaysFood) {
            data.add(m);
        }
        Consumed.setEditable(true);
        TableColumn name = new TableColumn("Name");
        name.setMinWidth(200);
        name.setCellValueFactory(
                new PropertyValueFactory<Meal, String>("foodName"));
        TableColumn quantity = new TableColumn("Quantity");
        quantity.setMinWidth(100);
        quantity.setCellValueFactory(
                new PropertyValueFactory<Meal, String>("quantity"));
        TableColumn calories = new TableColumn("Calories");
        calories.setMinWidth(100);
        calories.setCellValueFactory(
                new PropertyValueFactory<Meal, String>("calories"));
        if (data.isEmpty()){
            Consumed.setVisible(false);
        } else {
            Consumed.setItems(data);
            Consumed.getColumns().addAll(name, quantity, calories);
            addButtonToTable();
        }
    }
    private void addButtonToTable() {
        TableColumn<Meal, Void> colBtn = new TableColumn("Delete");
        Callback<TableColumn<Meal, Void>, TableCell<Meal, Void>> cellFactory = new Callback<TableColumn<Meal, Void>, TableCell<Meal, Void>>() {
            @Override
            public TableCell<Meal, Void> call(final TableColumn<Meal, Void> param) {
                final TableCell<Meal, Void> cell = new TableCell<Meal, Void>() {
                    private final Button btn = new Button("Remove");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Meal data = getTableView().getItems().get(getIndex());
                            data.removeLink(User,new Date());
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
     * go to the dashboard
     * @param event button pushed
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
    /**
     * adds meal
     * adds diet
     * go to dashboard
     * @param event add food button pressed
     */
    @FXML
    private void AddFoodsAction (ActionEvent event){
        errorMsg.setText("");
        GenericDatabaseController db = new GenericDatabaseController();
        //validation for quantity
        if (quantity.getText().matches("^[1-9][0-9]*$")){
            int i = Integer.parseInt(quantity.getText());
            if (i>10){
                errorMsg.setText("Error: quantity greater than 10");
                quantity.setText("");
            }
        } else {
            errorMsg.setText("Error: quantity not positive");
            quantity.setText("");
        }
        //validation of dropdown
        if (Foods.getValue()==null) {
            errorMsg.setText("Error: food not selected");
        }else if(Foods.getValue().toString().equals("")){
            errorMsg.setText("Error: food not typed in");
        } else {
            if(!db.isInTable(Foods.getValue().toString(),"foods","foodName")){
                errorMsg.setText("Error: not valid food");
                Foods.setValue("");
            }
        }
        //validation of the meal type
        if (MealType.getValue()==null) {
            errorMsg.setText("Error: meal type not selected");
        }else if(MealType.getValue().toString().equals("")){
            errorMsg.setText("Error: meal type not typed in");
        } else {
            if(!(MealType.getValue().toString().equals("Breakfast")||MealType.getValue().toString().equals("Lunch")||MealType.getValue().toString().equals("Dinner")||MealType.getValue().toString().equals("Snack"))){
                errorMsg.setText("Error: not valid meal type");
                MealType.setValue("");
            }
        }

        if (errorMsg.getText().equals("")){
            FoodItem foodItem = FoodItem.getFoodFromName(Foods.getValue().toString());
            Meal meal = Meal.getMeal(foodItem,Integer.parseInt(quantity.getText()),MealType.getValue().toString());
            if (meal==null){
                meal = new Meal(User,foodItem,Integer.parseInt(quantity.getText()),MealType.getValue().toString());
                meal.add();
            }
            meal.setUser(User);
            meal.addLink();
            goToDash(User,event);
        }
    }
    /**
     * adjusts the result in the drop down of food
     * @param event search button pushed
     */
    @FXML
    private void goSearch(ActionEvent event) {
        try {
            String toSearch = txt_search.getText();
            GenericDatabaseController db = new GenericDatabaseController();
            ArrayList<String> results = db.getAllLike(toSearch,"foods","foodName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Foods.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void Remove(ActionEvent event) {}

}