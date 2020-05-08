package Model;

import Controller.GenericController;
import Controller.GenericController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
     * Finds the exercise with the name in the database
     * Selects the id
     * Uses the getExerciseFromID function to get the exercise
     * @param name the name of the exercise required
     * @return the exercise obj with the name specified
     */
    public static Exercise getExerciseFromName(String name){
        GenericController db = new GenericController();
        return getExerciseFromID(db.getIDFromName(name,"exercise","exerciseName","idExerciseType"));
    }
    /**
     * Gets the exercise object from the id
     *
     * @param id the id of the exercise
     * @return the exercise obj with that id
     */
    public static Exercise getExerciseFromID(int id){
        GenericController db = new GenericController();
        try (
                Statement stmnt = db.getConnection().createStatement();
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
