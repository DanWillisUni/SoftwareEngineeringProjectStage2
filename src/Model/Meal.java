package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Meal {
    private enum MealType {
        Breakfast,
        Lunch,
        Dinner,
        Snack
    }
    int id;
    FoodItem food;
    int quantity;
    MealType type;

    Meal(int id, FoodItem food, int quantity, MealType type){
        this.id=id;
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
    public MealType getType() {
        return type;
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.meal Values("+ getId() + ", '" + getFood().getId() + "', '" + getQuantity()+ "', '" + getType().toString()+ "' )";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void remove(){
        GenericDatabaseController db = new GenericDatabaseController();
        db.remove(getId(),"meal","idMeal");
    }
}
