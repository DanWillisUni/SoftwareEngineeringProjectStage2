package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExerciseSession {
    int id;
    Exercise exercise;
    int duration;
    int caloriesBurned;

    public ExerciseSession(int id, Exercise exercise, int duration, int caloriesBurned) {
        this.id = id;
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }
    public ExerciseSession(Exercise exercise, int duration, int caloriesBurned) {
        GenericDatabaseController db = new GenericDatabaseController();
        this.id = db.genID("exercisesession","idExerciseSession");
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    public int getId() {
        return id;
    }
    public Exercise getExercise() {
        return exercise;
    }
    public int getDuration() {
        return duration;
    }
    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void add() {
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.exerciseSession Values("+ getId() + ", '" + getDuration() + "', '" + getExercise().getId()+ "', '" +getCaloriesBurned()+ "' )";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ExerciseSession getExerciseSession(Exercise exercise, int duration, int caloriesBurned){
        GenericDatabaseController db = new GenericDatabaseController();
        ExerciseSession r = null;
        try {
            final String query = "SELECT * FROM softwareengineering.meal WHERE idExerciseType = " + exercise.getId() + " AND duration = " + duration + " And caloriesBurned = " + caloriesBurned;
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    return new ExerciseSession(rs.getInt("idExerciseSession"),exercise,duration,caloriesBurned);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public void remove(){
        GenericDatabaseController db = new GenericDatabaseController();
        db.remove(getId(),"exercisesession","idExerciseSession");
    }
}
