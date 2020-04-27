package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FoodItem {
    int id;
    String name;
    int amountOfCalories;
    String description;
    FoodItem(int id, String name, int amountOfCalories, String description){
        this.id = id;
        this.name = name;
        this.amountOfCalories = amountOfCalories;
        this.description = description;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAmountOfCalories() {
        return amountOfCalories;
    }
    public String getDescription() {
        return description;
    }

    static public FoodItem getFoodFromName(String name){
        GenericDatabaseController db = new GenericDatabaseController();
        return getFoodFromID(db.getIDFromName(name,"foods","foodName","idFood"));
    }
    private static FoodItem getFoodFromID(int id){
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.foods where idFood ="+id);

        ){
            if(rs.next()) {
                return new FoodItem(id,rs.getString("foodName"),rs.getInt("amountOfCalories"),rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
