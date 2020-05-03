package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ExerciseSession {
    int id;
    User user;
    Exercise exercise;
    int duration;
    int caloriesBurned;

    public ExerciseSession(int id,User user, Exercise exercise, int duration, int caloriesBurned) {
        this.id = id;
        this.user = user;
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }
    public ExerciseSession(Exercise exercise,User user, int duration, int caloriesBurned) {
        GenericDatabaseController db = new GenericDatabaseController();
        this.id = db.genID("exercisesession","idExerciseSession");
        this.user=user;
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }
    public ExerciseSession(int id,Exercise exercise, int duration, int caloriesBurned) {
        this.id = id;
        this.user=null;
        this.exercise = exercise;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    public int getId() {
        return id;
    }
    public User getUser(){return user;}
    public Exercise getExercise() {
        return exercise;
    }
    public String getExerciseName() {
        return getExercise().getName();
    }
    public int getDuration() {
        return duration;
    }
    public int getCaloriesBurned() {
        return caloriesBurned;
    }
    public void setUser(User user){
        this.user=user;
    }

    public void add() {
        ExerciseSession s = ExerciseSession.getExerciseSession(getExercise(),getDuration(),getCaloriesBurned());
        if(s==null){
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
    }
    public void addLink(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.exerciseLink Values("+ db.genID("exerciseLink","idLink") + ", '" + getUser().getId() + "', '" + getId()+ "', '" +new java.sql.Date(new Date().getTime())+ "' )";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfSessionInUse(){
        return true;
    }
    public static ExerciseSession getExerciseSession(Exercise exercise, int duration, int caloriesBurned){
        GenericDatabaseController db = new GenericDatabaseController();
        ExerciseSession r = null;
        try {
            final String query = "SELECT * FROM softwareengineering.exercisesession WHERE idExerciseType = " + exercise.getId() + " AND durationMinutes = " + duration + " And caloriesBurned = " + caloriesBurned;
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

    public void removeLink(User user,Date date){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "DELETE FROM softwareengineering.exerciselink WHERE idUser=" + user.getId() + " and idExerciseSession="+ getId() + " and date='" + new java.sql.Date(date.getTime()) + "'";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!checkIfSessionInUse()){
            remove();
        }
    }
    public static ExerciseSession getFromID(int id){
        GenericDatabaseController db = new GenericDatabaseController();
        ExerciseSession r = null;
        try {
            final String query = "SELECT * FROM softwareengineering.exercisesession WHERE idExerciseSession = " + id;
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    Exercise exercise = Exercise.getExerciseFromID(rs.getInt("idExerciseType"));
                    return new ExerciseSession(id,exercise,rs.getInt("durationMinutes"),rs.getInt("caloriesBurned"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public static ArrayList<ExerciseSession> getDays(User u,Date date){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<ExerciseSession> r = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink where idUser = "+u.getId() +" And date = '" + new java.sql.Date(date.getTime()) + "'");

        ){
            while(rs.next()) {
                r.add(ExerciseSession.getFromID(rs.getInt("idExerciseSession")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public static ArrayList<ExerciseSession> getAll(User u){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<ExerciseSession> r = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink where idUser = "+u.getId());

        ){
            while(rs.next()) {
                r.add(ExerciseSession.getFromID(rs.getInt("idExerciseSession")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public static HashMap<Date,ExerciseSession> getDateAll(User u){
        GenericDatabaseController db = new GenericDatabaseController();
        HashMap<Date,ExerciseSession> r = new HashMap<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink where idUser = "+u.getId());

        ){
            while(rs.next()) {
                r.put(rs.getDate("date"),ExerciseSession.getFromID(rs.getInt("idExerciseSession")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
}
