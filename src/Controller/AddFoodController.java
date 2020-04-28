package Controller;
//javafx imports
import Model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
//java imports
import java.util.ArrayList;

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

        ArrayList<Model.Diet> todaysFood = Model.Diet.getTodays(User);
        ObservableList<MealEaten> data = FXCollections.observableArrayList();
        for (Model.Diet d:todaysFood) {
            data.add(d.getMeal().getMealEaten());
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
        TableColumn calories = new TableColumn("Calories");
        calories.setMinWidth(100);
        calories.setCellValueFactory(
                new PropertyValueFactory<MealEaten, String>("calories"));
        Consumed.setItems(data);
        Consumed.getColumns().addAll(name, quantity, calories);
        addButtonToTable();
    }
    private void addButtonToTable() {
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
                            setUpDisplay();
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
                meal = new Meal(foodItem,Integer.parseInt(quantity.getText()),MealType.getValue().toString());
                meal.add();
            }
            Diet diet = new Diet(User,meal);
            diet.add();
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