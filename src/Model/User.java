package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class User {
    int id;
    String forename;
    String surname;
    String username;
    String email;
    String password;
    Date DOB;
    int height;
    char gender;
    int weight;
    int cal;
    /**
     * sets all the parameters to the variables of the person
     * @param id id of the person
     * @param forename forename of the user
     * @param surname surname of the user
     * @param username username of the user
     * @param email email of the user
     * @param password password of the user
     * @param DOB date of birth of the user
     * @param height height of the user
     * @param gender gender of the user
     */
    public User(int id, String forename, String surname, String username, String email, String password, Date DOB, int height, char gender, int weight){
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
        this.gender = gender;
        this.weight=weight;
        cal=1800;
        if (gender=='M'){
            cal=2000;
        }
    }
    public static String passwordHash(String p){
        return "hash" + p;
    }
    /**
     * gets the id
     * @return id of user
     */
    public int getId(){
        return id;
    }
    /**
     * gets the forename
     * @return forename of the user
     */
    public String getForename(){
        return forename;
    }
    /**
     * gets the surname of the user
     * @return surname of the user
     */
    public String getSurname(){
        return surname;
    }
    /**
     * gets the username of the user
     * @return username of the user
     */
    public String getUsername(){
        return username;
    }
    /**
     * gets email
     * @return email of the user
     */
    public String getEmail(){
        return email;
    }
    /**
     * gets the password
     * @return password of the user
     */
    public String getPassword(){
        return password;
    }
    /**
     * get the DOB of the user
     * @return date of birth of the user
     */
    public Date getDOB(){
        return DOB;
    }
    /**
     * get the height of the user
     * @return height of the user
     */
    public int getHeight(){
        return height;
    }
    /**
     * get the gender of the user
     * @return gender of the user
     */
    public char getGender(){return gender;}
    /**
     * get the weight of the user
     * @return weight of the user
     */
    public int getWeight(){
        return weight;
    }
    public int getCal(){
        return cal;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
        GenericDatabaseController db = new GenericDatabaseController();
        final String query = "UPDATE softwareengineering.User SET password = '"+ passwordHash(getPassword())+"' Where idUser= "+ getId();
        try (
                PreparedStatement pstmt = db.getConnection().prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setGender(char gender) {
        this.gender = gender;
    }
    public void setWeight(int weight) {
        this.weight = weight;
        update();
    }
    public void setCal(int cal){
        this.cal=cal;
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.user Values("+ getId() + ", '" + getForename() + "', '" + getSurname()+ "', '" + getEmail()+ "', '" + getUsername()+ "', '" + passwordHash(getPassword())+ "', '"+ new java.sql.Date(getDOB().getTime()) +"' , " + getHeight()+ ", '" + getGender() + "', " + getWeight() + ", "+ getCal()+")";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(){
        GenericDatabaseController db = new GenericDatabaseController();
        final String query = "UPDATE softwareengineering.User SET forename = '"+getForename()+"', surname = '"+ getSurname()+"',email = '"+ getEmail()+"',username = '"+ getUsername()+"',DOB = '"+ new java.sql.Date(getDOB().getTime())+"',height = "+ getHeight()+",gender = '"+ getGender()+"',weight = "+ getWeight()+ ",calories="+cal+" Where idUser= "+ getId();
        try (
                PreparedStatement pstmt = db.getConnection().prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getFromEmail(String email){
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.user where email = '"+email +"'");
        ){
            if(rs.next()){
                return new User(rs.getInt("idUser"),rs.getString("forename"),rs.getString("surname"),rs.getString("username"),rs.getString("email"),rs.getString("password"),rs.getDate("DOB"),rs.getInt("height"),(rs.getString("gender")).charAt(0),rs.getInt("weight"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static User getFromID(int id){
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.user where idUser ="+id);
        ){
            if(rs.next()) {
                return new User(id,rs.getString("forename"),rs.getString("surname"),rs.getString("username"),rs.getString("email"),rs.getString("password"),rs.getDate("DOB"),rs.getInt("height"),(rs.getString("gender")).charAt(0),rs.getInt("weight"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addWeight(int w){
        removeWeight();
        setWeight(w);
        update();
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.weighttracking Values(" + db.genID("WeightTracking","idWeightTracking") +", " + getId() + ", '" + new java.sql.Date(new Date().getTime()) + "', '" + w + "' )";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeWeight(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "DELETE FROM softwareengineering.weighttracking WHERE idUser = "+getId() + " AND date = '" + new java.sql.Date(new Date().getTime()) + "'" ;
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public LinkedHashMap<Integer,Date> getAllWeights(){
        GenericDatabaseController db = new GenericDatabaseController();
        LinkedHashMap<Integer,Date> r = new LinkedHashMap<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.weighttracking where idUser ="+getId() + " order by date asc");

        ){
            while(rs.next()) {
                r.put(rs.getInt("weight"),new java.util.Date(rs.getDate("date").getTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public ArrayList<String> getWeeklySummary(Date commencing){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<String> r = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.weeklySummary where idUser = "+getId() + " and weekCommencing = '"+new java.sql.Date(commencing.getTime()) +"'");

        ){
            if(rs.next()) {
                r.add(rs.getString("idWeeklySummary"));
                r.add(rs.getString("idUser"));
                r.add(rs.getString("weekCommencing"));
                r.add(rs.getString("caloriesBurnt"));
                r.add(rs.getString("caloriesConsumed"));
                r.add(rs.getString("weight"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public void updateSummary(int id,int calDuringExercise,int calDuringEating,int weight){
        GenericDatabaseController db = new GenericDatabaseController();
        final String query = "UPDATE softwareengineering.WeeklySummary SET caloriesBurnt="+calDuringExercise+",caloriesConsumed="+calDuringEating+",weight="+weight+" Where idWeeklySummary= "+ id;
        try (
                PreparedStatement pstmt = db.getConnection().prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void newSummary(Date commencing,int calDuringExercise,int calDuringEating,int weight){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.WeeklySummary Values("+db.genID("WeeklySummary","idWeeklySummary")+","+getId()+",'"+new java.sql.Date(commencing.getTime()) +"',"+calDuringExercise+","+calDuringEating+","+weight+")";
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
