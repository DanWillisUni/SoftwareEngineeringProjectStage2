package Model;

import Controller.GenericDatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercise {
    int id;
    String name;
    int caloriesBurned;

    public Exercise(int id, String name, int caloriesBurned){
        this.id=id;
        this.name=name;
        this.caloriesBurned=caloriesBurned;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    static public Exercise getExerciseFromName(String name){
        GenericDatabaseController db = new GenericDatabaseController();
        return getExerciseFromID(db.getIDFromName(name,"exercise","exerciseName","idExerciseType"));
    }
    public static Exercise getExerciseFromID(int id){
        GenericDatabaseController db = new GenericDatabaseController();
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
