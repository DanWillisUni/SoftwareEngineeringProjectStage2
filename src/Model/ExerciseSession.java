package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExerciseSession {
    int id;
    Exercise exercise;
    int duration;
    int caloriesBurned;

    ExerciseSession(int id, Exercise exercise, int duration, int caloriesBurned) {
        this.id = id;
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
    public void remove(){
        GenericDatabaseController db = new GenericDatabaseController();
        db.remove(getId(),"exercisesession","idExerciseSession");
    }
}
