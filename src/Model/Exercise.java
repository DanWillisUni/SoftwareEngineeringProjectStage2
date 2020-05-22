package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Exercise {
    int id;//the exercise id
    String name;//the name of the exercise
    int caloriesBurned;//the calories burned in 1 min

    /**
     * Constructor
     * @param id the exercise id
     * @param name the exercise name
     * @param caloriesBurned the calories burned in 1 min
     */
    public Exercise(int id, String name, int caloriesBurned){
        this.id=id;
        this.name=name;
        this.caloriesBurned=caloriesBurned;
    }
    /**
     * Gets the id of the object
     * @return the exercise id
     */
    public int getId() {
        return id;
    }
    /**
     * Gets the name of the exercise
     * @return the name of the exercise
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the calories burnt in one min of the exercise
     * @return the calories burnt in one min
     */
    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    /**
     * Gets all the exercises in the database that are like str
     * @param str string to search for
     * @param c connection
     * @return all the exercise names like str
     */
    public static ObservableList<String> getAllLike(String str, Connection c){
        ArrayList<String> results = GenericDatabaseController.getAllLike(str,"exercise","exerciseName",c);//get all exercises with a name like "", this gets all of them
        ObservableList<String> observableList = FXCollections.observableList(results);//put the results into an observable list
        return observableList;
    }
    /**
     * Checks if the name of the exercise is in the exercise table
     * @param str name of the exercise
     * @param c connection
     * @return true if it is
     */
    public static boolean isInTable(String str,Connection c){
        return GenericDatabaseController.isInTable(str,"exercise","exerciseName",c);
    }
    /**
     * Finds the exercise with the name in the database
     * Selects the id
     * Uses the getExerciseFromID function to get the exercise
     * @param name the name of the exercise required
     * @return the exercise obj with the name specified
     */
    public static Exercise getExerciseFromName(String name, Connection c){
        Exercise r = getExerciseFromID(GenericDatabaseController.getIDFromName(name,"exercise","exerciseName","idExerciseType",c),c);
        return r;

    }
    /**
     * Gets the exercise object from the id
     *
     * @param id the id of the exercise
     * @return the exercise obj with that id
     */
    public static Exercise getExerciseFromID(int id, Connection c){
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exercise where idExerciseType ="+id);

        ){
            if(rs.next()) {
                return new Exercise(id,rs.getString("exerciseName"),rs.getInt("calsPerMinute"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
