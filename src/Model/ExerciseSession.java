package Model;

import Controller.GenericController;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ExerciseSession {
    int id;//exercise session id
    Exercise exercise; //exercise obj
    int duration;//duration of the session
    int caloriesBurned;//calories burned during the session

    /**
     * Constructor
     * Used to construct exerciseSession that is already in the database
     * @param id id of the session
     * @param exercise exercise obj
     * @param duration duration of the exercise
     * @param caloriesBurned calories burnt during the exercise
     */
    public ExerciseSession(int id, Exercise exercise, int duration, int caloriesBurned) {
        this.id = id;
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }
    /**
     * Constructor
     * This is used to create a new exercise session that doesnt exist
     * @param exercise exercise obj
     * @param duration duration of the session
     * @param caloriesBurned calories burnt
     */
    public ExerciseSession(Exercise exercise, int duration, int caloriesBurned,Connection c) {
        this.id = GenericController.genID("exercisesession","idExerciseSession",c);
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }
    /**
     * Get id
     * @return the id of the exercise session
     */
    public int getId() {
        return id;
    }
    /**
     * Get the exercise obj of the session
     * @return exercise obj
     */
    public Exercise getExercise() {
        return exercise;
    }
    /**
     * Get the exercise then get the name from that obj
     * @return exerciseName
     */
    public String getExerciseName() {
        return getExercise().getName();
    }
    /**
     * Get the duration of the session
     * @return get the duration of the session
     */
    public int getDuration() {
        return duration;
    }
    /**
     * Get the number of calories burnt
     * @return the number of calories burnt during the exercise
     */
    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    /**
     * Adds the exercise session to the database
     */
    public void add(Connection c) {
        ExerciseSession s = ExerciseSession.getExerciseSession(getExercise(),getDuration(),getCaloriesBurned(),c);
        if(s==null){
            try {
                final String query = "Insert Into softwareengineering.exerciseSession Values("+ getId() + ", '" + getDuration() + "', '" + getExercise().getId()+ "', '" +getCaloriesBurned()+ "' )";
                try (
                        PreparedStatement pstmt = c.prepareStatement(query)
                ){
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Checks to see if the session is still in use
     * Used for removing the link to see if the session can be removed too
     * @return true if the session is linked to
     */
    public boolean checkIfSessionInUse(Connection c){
        try {
            final String query = "SELECT * FROM softwareengineering.exerciselink WHERE idExerciseSession = " + getId();
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
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
     * Removes the exercise session from the database
     */
    public void remove(Connection c){
        GenericController.remove(getId(),"exercisesession","idExerciseSession",c);
    }

    /**
     * Get the exercise session that corrosponds to the the one searched for
     * Or null if there is no such session
     * @param exercise exercise done during the session
     * @param duration duration of the session
     * @param caloriesBurned calories burnt during the session
     * @return exercise session obj of a session with the right inputs
     */
    public static ExerciseSession getExerciseSession(Exercise exercise, int duration, int caloriesBurned, Connection c){
        ExerciseSession r = null;
        try {
            final String query = "SELECT * FROM softwareengineering.exercisesession WHERE idExerciseType = " + exercise.getId() + " AND durationMinutes = " + duration + " And caloriesBurned = " + caloriesBurned;
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
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
    /**
     * Get the session from the id
     * @param id id of session
     * @return exercise session obj with id, id
     */
    public static ExerciseSession getFromID(int id, Connection c){
        ExerciseSession r = null;
        try {
            final String query = "SELECT * FROM softwareengineering.exercisesession WHERE idExerciseSession = " + id;
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    Exercise exercise = Exercise.getExerciseFromID(rs.getInt("idExerciseType"),c);
                    return new ExerciseSession(id,exercise,rs.getInt("durationMinutes"),rs.getInt("caloriesBurned"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * Gets all the exercise session done by the user on the date
     * @param u user
     * @param date date in question
     * @return list of all the exercise session done by user u on date date
     */
    public static ArrayList<ExerciseSession> getDays(User u,Date date, Connection c){
        ArrayList<ExerciseSession> r = new ArrayList<>();
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink where idUser = "+u.getId() +" And date = '" + new java.sql.Date(date.getTime()) + "'");

        ){
            while(rs.next()) {
                r.add(ExerciseSession.getFromID(rs.getInt("idExerciseSession"),c));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * Get all the dates and exercise sessions
     * @param u user
     * @return all the exercise session done and the date in arraylists in a hashmap
     */
    public static HashMap<ArrayList<Date>,ArrayList<ExerciseSession>> getDateAll(User u, Connection c){
        HashMap<ArrayList<Date>,ArrayList<ExerciseSession>> r = new HashMap<>();
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<ExerciseSession> sessions = new ArrayList<>();
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink where idUser = "+u.getId());

        ){
            while(rs.next()) {
                dates.add(rs.getDate("date"));
                sessions.add(ExerciseSession.getFromID(rs.getInt("idExerciseSession"),c));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        r.put(dates,sessions);
        return r;
    }
}
