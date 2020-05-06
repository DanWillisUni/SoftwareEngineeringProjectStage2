package Model;

import Controller.GenericDatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FoodItem {
    int id;// food id
    String name;//the name of the food
    int amountOfCalories;// the amount of calories in the food
    String description;//food desciption, for extension, not actually used anywhere

    /**
     * Constructor
     * @param id food id
     * @param name the name of the food
     * @param amountOfCalories amount of calories
     * @param description food description
     */
    public FoodItem(int id, String name, int amountOfCalories, String description){
        this.id = id;
        this.name = name;
        this.amountOfCalories = amountOfCalories;
        this.description = description;
    }
    /**
     * Get the id of the food
     * @return the id of the food
     */
    public int getId() {
        return id;
    }
    /**
     * Get the name of the food
     * @return the name of the food
     */
    public String getName() {
        return name;
    }
    /**
     * Get the amount of calories in the food
     * @return amount of calories
     */
    public int getAmountOfCalories() {
        return amountOfCalories;
    }
    /**
     * Get the food description
     * @return the food description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the food obj using the getFoodFromID after getting the food id from the name in the database
     * @param name the name of the desired food
     * @return the food obj
     */
    public static FoodItem getFoodFromName(String name){
        GenericDatabaseController db = new GenericDatabaseController();
        return getFoodFromID(db.getIDFromName(name,"foods","foodName","idFood"));
    }
    /**
     * Get the food obj from the id of the food item
     * @param id the id of the food item
     * @return the food obj
     */
    public static FoodItem getFoodFromID(int id){
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
