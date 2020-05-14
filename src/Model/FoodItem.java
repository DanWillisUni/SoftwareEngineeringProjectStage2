package Model;

import Controller.GenericController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
     *
     * @param str
     * @param c
     * @return
     */
    public static ObservableList<String> getAllLike(String str,Connection c) {
        ArrayList<String> results = GenericDatabaseController.getAllLike(str,"foods","foodName",c);//gets all the foodnames
        ObservableList<String> observableList = FXCollections.observableList(results);//puts all foodnames into a observablelist
        return observableList;
    }
    /**
     *
     * @param str
     * @param c
     * @return
     */
    public static boolean isInTable(String str,Connection c){
        return GenericDatabaseController.isInTable(str,"foods","foodName",c);
    }
    /**
     * Get the food obj using the getFoodFromID after getting the food id from the name in the database
     * @param name the name of the desired food
     * @return the food obj
     */
    public static FoodItem getFoodFromName(String name,Connection c){
        FoodItem r = getFoodFromID(GenericDatabaseController.getIDFromName(name,"foods","foodName","idFood",c),c);
        return r;
    }
    /**
     * Get the food obj from the id of the food item
     * @param id the id of the food item
     * @return the food obj
     */
    public static FoodItem getFoodFromID(int id, Connection c){
        try (
                Statement stmnt = c.createStatement();
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
