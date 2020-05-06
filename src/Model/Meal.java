package Model;

import Controller.GenericDatabaseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Meal {
    int id;//id of the meal
    FoodItem food;//food object that was eaten
    int quantity;//quantity of the meal
    String type;//type of the meal

    /**
     * Constructor
     * Make a meal obj that is in the database already
     * @param id id of the meal obj
     * @param food food obj eaten
     * @param quantity quantity eaten
     * @param type type of meal
     */
    Meal(int id, FoodItem food, int quantity, String type){
        this.id=id;
        this.food = food;
        this.quantity = quantity;
        this.type = type;
    }
    /**
     * Constructor
     * Used to generate a new meal by a user
     * @param food food obj that was eaten
     * @param quantity quantity of food eaten
     * @param type type of meal
     */
    public Meal(FoodItem food, int quantity, String type){
        GenericDatabaseController db = new GenericDatabaseController();
        this.id=db.genID("meal","idMeal");
        this.food = food;
        this.quantity = quantity;
        this.type = type;
    }

    /**
     * Get the id of the meal
     * @return the id of the meal
     */
    public int getId() {
        return id;
    }
    /**
     * Get the food obj of the meal
     * @return food obj
     */
    public FoodItem getFood() {
        return food;
    }
    /**
     * Get the quantity
     * @return the quantity of the meal
     */
    public int getQuantity() {
        return quantity;
    }
    /**
     * Get the type of meal
     * @return the type of meal
     */
    public String getType() {
        return type;
    }
    /**
     * Get the food objects name
     * @return the food objects name
     */
    public String getFoodName() {
        return getFood().getName();
    }
    /**
     * Get the amount of calories consumed in the meal
     * @return quantity mutiplies by the amount of caloires in the food
     */
    public int getCalories() {
        return getQuantity() * getFood().getAmountOfCalories();
    }

    /**
     * Adds the meal to the database
     */
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
    /**
     * Get the meal item that was already in the database
     * And return null if none exists
     * @param food the food object in the meal
     * @param quantity the quantity in the meal
     * @param type the type of meal
     * @return the meal item from the database
     */
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
    /**
     * Remove the Meal from the database
     */
    public void remove(){
        GenericDatabaseController db = new GenericDatabaseController();
        db.remove(getId(),"meal","idMeal");
    }

    /**
     * Get the meal obj with the id id
     * @param id id of the meal
     * @return meal obj with id id
     */
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
    /**
     * Checks to see if the meal is in use
     * @return true if it is in use by someone in the database
     */
    public boolean checkIfInUse(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "SELECT * FROM softwareengineering.diet WHERE idMeal = " + getId();
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets a list of meals eaten on date by the user
     * @param user user
     * @param date date to get from
     * @return list of meals eaten by user on date
     */
    public static ArrayList<Meal> getDays(User user,Date date){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<Meal> r = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.diet where idUser ="+user.getId() +" And date = '" + new java.sql.Date(date.getTime()) + "'");

        ){
            while(rs.next()) {
                r.add(Meal.getFromID(rs.getInt("idMeal")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * Needed it to return both arraylists hence a hashmap of arraylists
     * @param u user
     * @return hashmap of arraylist of all the dates and arraylist of all the meals
     */
    public static HashMap<ArrayList<Date>,ArrayList<Meal>> getDateAll(User u){
        GenericDatabaseController db = new GenericDatabaseController();
        HashMap<ArrayList<Date>,ArrayList<Meal>> r = new HashMap<>();
        ArrayList<Date> d = new ArrayList<>();
        ArrayList<Meal> m = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.diet where idUser = "+u.getId());

        ){
            while(rs.next()) {
                d.add(rs.getDate("date"));
                m.add(Meal.getFromID(rs.getInt("idMeal")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        r.put(d,m);
        return r;
    }
}
