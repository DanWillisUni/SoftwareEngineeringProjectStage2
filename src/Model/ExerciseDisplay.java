package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExerciseDisplay {
    String name;
    String duration;
    String cal;
    public ExerciseDisplay(ExerciseSession s){
        name = s.getExercise().getName();
        duration = Integer.toString(s.getDuration());
        cal = Integer.toString(s.getCaloriesBurned());
    }
    public String getName() {
        return name;
    }
    public String getDuration() {
        return duration;
    }
    public String getCal() {
        return cal;
    }

    public ExerciseSession getSession() {
        Exercise exercise = Exercise.getExerciseFromName(name);
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exercisesession where idExerciseType = "+ exercise.getId() + " and durationMinutes = " + duration + " and caloriesBurned = " + cal);

        ){
            if(rs.next()) {
                return ExerciseSession.getFromID(rs.getInt("idExerciseSession"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
