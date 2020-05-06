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
    @FXML private TextField txt_search;//search box
    @FXML private ComboBox Foods;//food dropdown
    @FXML private TextField quantity;//quantity of food box
    @FXML private ComboBox MealType;//mealType drop down
    @FXML private Label errorMsg;//error message label to inform the user of any errors
    @FXML private Label name;//name of the user at the top of the page
    @FXML private TableView Consumed;//table of all the food eaten that day

    /**
     * Sets the user to the user signed in
     * @param User logged in user
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets the drop down of food to all the food
     * Gets all the food eaten that day by the user
     * Displays each meal eaten by the user in a table
     * If nothing has been eaten that day, hides the table
     */
    public void setUpDisplay(){
        name.setText("Hello, " + User.getForename());
        try {
            GenericDatabaseController db = new GenericDatabaseController();
            ArrayList<String> results = db.getAllLike("","foods","foodName");//gets all the foodnames
            ObservableList<String> observableList = FXCollections.observableList(results);//puts all foodnames into a observablelist
            Foods.setItems(observableList);//set the food dropdown to all the foods
        } catch (Exception e) {
            e.printStackTrace();
        }
        errorMsg.setText("");
        quantity.setText("");
        Foods.setValue("");
        MealType.setValue("");

        ArrayList<Model.Meal> todaysFood = Model.Meal.getDays(User,new Date());//get all todays food
        ObservableList<Meal> data = FXCollections.observableArrayList();
        for (Model.Meal m:todaysFood) {
            data.add(m);//add the Meal to the tableview data
        }

        if (data.isEmpty()){//if no meals have been eaten today
            Consumed.setVisible(false);//hide the table
        } else {
            Consumed.setVisible(true);//show the table
            Consumed.setEditable(true);
            TableColumn name = new TableColumn("Name");//create a new column for the food name
            name.setMinWidth(200);
            name.setCellValueFactory(
                    new PropertyValueFactory<Meal, String>("foodName"));//use Meal.getFoodName()
            TableColumn quantity = new TableColumn("Quantity");//create a new column for the quantity
            quantity.setMinWidth(100);
            quantity.setCellValueFactory(
                    new PropertyValueFactory<Meal, String>("quantity"));//use Meal.getQuantity()
            TableColumn calories = new TableColumn("Calories");//create a new column for the calories
            calories.setMinWidth(100);
            calories.setCellValueFactory(
                    new PropertyValueFactory<Meal, String>("calories"));//use Meal.getCalories()
            Consumed.setItems(data);//set all the data
            Consumed.getColumns().setAll(name, quantity, calories);//put in the columns
            addButtonToTable();//add the remove button column
        }
    }
    /**
     * Adds a column onto the tableview to have a button in
     * The button when pressed removes the food from that days worth of food that the user has eaten that day
     */
    private void addButtonToTable() {
        TableColumn<Meal, Void> colBtn = new TableColumn("Delete");
        Callback<TableColumn<Meal, Void>, TableCell<Meal, Void>> cellFactory = new Callback<TableColumn<Meal, Void>, TableCell<Meal, Void>>() {//new call back
            @Override
            public TableCell<Meal, Void> call(final TableColumn<Meal, Void> param) {
                final TableCell<Meal, Void> cell = new TableCell<Meal, Void>() {
                    private final Button btn = new Button("Remove");//add remove button
                    {
                        btn.setOnAction((ActionEvent event) -> {//on button press
                            Meal data = getTableView().getItems().get(getIndex());//get the meal
                            data.removeLink(User,new Date());//remove the link in diet
                            setUpDisplay();//refresh the display
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {//this is required as it is implenting the callback
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
        colBtn.setCellFactory(cellFactory);//generates the buttons
        Consumed.getColumns().add(colBtn);//adds the buttons
    }

    /**
     * Adjusts the result in the drop down of food
     * @param event search button pushed
     */
    @FXML
    private void goSearch(ActionEvent event) {
        try {
            String toSearch = txt_search.getText();
            GenericDatabaseController db = new GenericDatabaseController();
            ArrayList<String> results = db.getAllLike(toSearch,"foods","foodName");//get all like
            ObservableList<String> observableList = FXCollections.observableList(results);//put results in a observable list
            Foods.setItems(observableList);//set the dropdown
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Goes to the dashboard
     * @param event button pushed
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        goToDash(User,event);
    }
    /**
     * Validates everything
     * Attempts to find the meal in the database
     * If it exists just links it
     * Else add the meal to the database then link it
     * @param event add food button pressed
     */
    @FXML
    private void AddFoodsAction (ActionEvent event){
        errorMsg.setText("");
        GenericDatabaseController db = new GenericDatabaseController();
        //validation for quantity
        if (quantity.getText().matches("^[1-9]$")){
            int i = Integer.parseInt(quantity.getText());
            if (i>9){
                errorMsg.setText("Error: quantity greater than 9");
                quantity.setText("");
            }
        } else {
            errorMsg.setText("Error: quantity not positive integer");
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
            FoodItem foodItem = FoodItem.getFoodFromName(Foods.getValue().toString());//get the food item obj
            Meal meal = Meal.getMeal(foodItem,Integer.parseInt(quantity.getText()),MealType.getValue().toString());//attempt to get the meal
            if (meal==null){//if the meal is not in the database
                meal = new Meal(User,foodItem,Integer.parseInt(quantity.getText()),MealType.getValue().toString());//make a new meal obj
                meal.add();//add the meal to the database
            }
            meal.setUser(User);//set the user of the meal
            meal.addLink();//add a link between the user and the meal
            setUpDisplay();//refresh the page
        }
    }
}