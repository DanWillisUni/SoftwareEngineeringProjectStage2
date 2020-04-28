package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class MealEaten {
    String foodName;
    String quantity;
    String calories;

    MealEaten(String foodName,String quantity, String calories){
        this.foodName=foodName;
        this.quantity = quantity;
        this.calories = calories;
    }

    public String getFoodName() {
        return foodName;
    }
    public String getQuantity() {
        return quantity;
    }
    public String getCalories() {
        return calories;
    }

    public Meal getMeal(){
        FoodItem foodItem = FoodItem.getFoodFromName(foodName);
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.meal where idFood = "+ foodItem.getId() + " and quantity = " + quantity);

        ){
            if(rs.next()) {
                return Meal.getFromID(rs.getInt("idMeal"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
