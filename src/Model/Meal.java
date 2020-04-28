package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Meal {
    int id;
    FoodItem food;
    int quantity;
    String type;

    Meal(int id, FoodItem food, int quantity, String type){
        this.id=id;
        this.food = food;
        this.quantity = quantity;
        this.type = type;
    }
    public Meal(FoodItem food, int quantity, String type){
        GenericDatabaseController db = new GenericDatabaseController();
        this.id=db.genID("meal","idMeal");
        this.food = food;
        this.quantity = quantity;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public FoodItem getFood() {
        return food;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getType() {
        return type;
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.meal Values("+ getId() + ", '" + getFood().getId() + "', '" + getQuantity()+ "', '" + getType()+ "' )";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Meal getMeal(FoodItem food, int quantity, String type){
        GenericDatabaseController db = new GenericDatabaseController();
        Meal r = null;
        try {
            final String query = "SELECT * FROM softwareengineering.meal WHERE idFood = " + food.getId() + " AND quantity = " + quantity + " And mealCategory = '" + type + "'";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    return new Meal(rs.getInt("idMeal"),food,quantity,type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public void remove(){
        GenericDatabaseController db = new GenericDatabaseController();
        db.remove(getId(),"meal","idMeal");
    }

    public static Meal getFromID(int id){
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.meal where idMeal ="+id);
        ){
            if(rs.next()) {
                FoodItem foodItem = FoodItem.getFoodFromID(rs.getInt("idFood"));
                return new Meal(id,foodItem,rs.getInt("quantity"),rs.getString("mealCategory"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public MealEaten getMealEaten(){
        return new MealEaten(getFood().getName(), Integer.toString(getQuantity()), Integer.toString((getQuantity())*(getFood().getAmountOfCalories())));
    }
}
