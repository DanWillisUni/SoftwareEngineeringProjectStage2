package Model;

import Controller.GenericController;

import java.sql.*;
import java.util.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
    int id;//id of the user
    String forename;//users first name
    String surname;//users last name
    String username;//users selected username
    String email;//users email address
    String password;//users password
    Date DOB;//users date of birth
    int height;//users height in cm
    char gender;//users gender
    double weight;//user weight in kg
    int cal;//calories the user has to consume

    /**
     * Constructor
     * Sets all the parameters to the variables of the person
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
    public User(int id, String forename, String surname, String username, String email, String password, Date DOB, int height, char gender, double weight){
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
    /**
     * Gets the id
     * @return id of user
     */
    public int getId(){
        return id;
    }
    /**
     * Gets the forename
     * @return forename of the user
     */
    public String getForename(){
        return forename;
    }
    /**
     * Gets the surname of the user
     * @return surname of the user
     */
    public String getSurname(){
        return surname;
    }
    /**
     * Gets the username of the user
     * @return username of the user
     */
    public String getUsername(){
        return username;
    }
    /**
     * Gets email address
     * @return email of the user
     */
    public String getEmail(){
        return email;
    }
    /**
     * Gets the password
     * @return password of the user
     */
    public String getPassword(){
        return password;
    }
    /**
     * Get the DOB of the user
     * @return date of birth of the user
     */
    public Date getDOB(){
        return DOB;
    }
    /**
     * Get the height of the user
     * @return height of the user
     */
    public int getHeight(){
        return height;
    }
    /**
     * Get the gender of the user
     * @return gender of the user
     */
    public char getGender(){return gender;}
    /**
     * Get the weight of the user
     * @return weight of the user
     */
    public double getWeight(){
        return weight;
    }
    /**
     * Gets the calories the user has to consume daily
     * @return the calories
     */
    public int getCal(){
        return cal;
    }

    /**
     * Sets the Users forename to forename
     * @param forename new forename
     */
    public void setForename(String forename) {
        this.forename = forename;
    }
    /**
     * Sets the Users surname to surname
     * @param surname new surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /**
     * Sets the Users username to username
     * @param username new Username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Sets the Users email to email
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Updates the users table in the database to set the password
     * Hashes the password before putting it into the database
     * @param password new password as plain text
     */
    public void setPassword(String password, Connection c) {
        this.password = password;
        final String query = "UPDATE softwareengineering.User SET password = '"+ passwordHash(getPassword())+"' Where idUser= "+ getId();
        try (
                PreparedStatement pstmt = c.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Sets the Users date of birth to DOB
     * @param DOB new date of birth
     */
    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }
    /**
     * Sets the Users height to height
     * @param height new height
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Sets the Users gender to gender
     * @param gender new gender
     */
    public void setGender(char gender) {
        this.gender = gender;
    }
    /**
     * Sets the Users weight to weight
     * @param weight new weight
     */
    public void setWeight(double weight, Connection c) {
        this.weight = weight;
        update(c);
    }
    /**
     * Sets the Users calories to cal
     * @param cal new calories
     */
    public void setCal(int cal){
        this.cal=cal;
    }

    /**
     * Adds a user to the database
     */
    public void add(Connection c){
        try {
            final String query = "Insert Into softwareengineering.user Values("+ getId() + ", '" + getForename() + "', '" + getSurname()+ "', '" + getEmail()+ "', '" + getUsername()+ "', '" + passwordHash(getPassword())+ "', '"+ new java.sql.Date(getDOB().getTime()) +"' , " + getHeight()+ ", '" + getGender() + "', " + getWeight() + ", "+ getCal()+")";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Updates the user with the id the same as this
     * Sets all the columns except the password column
     * To the current attributes of the user
     */
    public void update(Connection c){
        final String query = "UPDATE softwareengineering.User SET forename = '"+getForename()+"', surname = '"+ getSurname()+"',email = '"+ getEmail()+"',username = '"+ getUsername()+"',DOB = '"+ new java.sql.Date(getDOB().getTime())+"',height = "+ getHeight()+",gender = '"+ getGender()+"',weight = "+ getWeight()+ ",calories="+cal+" Where idUser= "+ getId();
        try (
                PreparedStatement pstmt = c.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hashes the password
     * Seeded the random obj that generated the salt by the User id
     * This means for each user it will generate something different for the same password
     * @param p plaintext password
     * @return the hashed password
     */
    public String passwordHash(String p){
        byte[] salt = new byte[16];
        Random random = new Random(getId());
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(p.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        try {
            hash = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(hash);
    }

    /**
     * Gets the user obj of the user that has the email, email
     * If there is no such user
     * Return null
     * @param email email address of the user
     * @return the user obj that has the email
     */
    public static User getFromEmail(String email, Connection c){
        try (
                Statement stmnt = c.createStatement();
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
    /**
     * Gets the user obj of the user that has the id, id
     * If there is no such user
     * Return null
     * @param id id of the user
     * @return the user obj with id as id
     */
    public static User getFromID(int id, Connection c){
        try (
                Statement stmnt = c.createStatement();
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

    /**
     * Removes a weight tracking value if there is one on the current day
     * Adds new weight to the weight tracking
     * @param w new weight
     */
    public void addWeight(double w, Connection c){
        removeWeight(new Date(),c);
        setWeight(w,c);
        update(c);
        try {
            final String query = "Insert Into softwareengineering.weighttracking Values(" + GenericController.genID("WeightTracking","idWeightTracking",c) +", " + getId() + ", '" + new java.sql.Date(new Date().getTime()) + "', '" + w + "' )";
            try (
                    PreparedStatement pstmt =c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Removes the weight tracking column for this user on date d
     * @param d date to remove on
     */
    public void removeWeight(Date d, Connection c){
        GenericController db = new GenericController();
        try {
            final String query = "DELETE FROM softwareengineering.weighttracking WHERE idUser = "+getId() + " AND date = '" + new java.sql.Date(d.getTime()) + "'" ;
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Get all the weight trackings of the user
     * Add the date and weight to the hashmap
     * @return all the weights the user has entered in the last 4 weeks, with corrosponding dates
     */
    public LinkedHashMap<Date,Double> getAllWeights(Connection c){
        LinkedHashMap<Date,Double> r = new LinkedHashMap<>();
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.weighttracking where idUser ="+getId() + " order by date asc");

        ){
            while(rs.next()) {
                r.put(new java.util.Date(rs.getDate("date").getTime()),rs.getDouble("weight"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * Add an exerciseSessionLink between exerciseSession and the user on todays date
     * @param exerciseSession exerciseSession to create link with
     */
    public void addExerciseSessionLink(ExerciseSession exerciseSession, Connection c){
        try {
            final String query = "Insert Into softwareengineering.exerciseLink Values("+ GenericController.genID("exerciseLink","idLink",c) + ", '" + getId() + "', '" + exerciseSession.getId()+ "', '" +new java.sql.Date(new Date().getTime())+ "' )";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Removes the link with an exercise session
     * @param date date
     * @param exerciseSession exercise session to remove
     */
    public void removeExerciseSessionLink(Date date,ExerciseSession exerciseSession, Connection c){
        //select one id of the exerciseSession
        int firstID = -1;
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink WHERE idUser=" + getId() + " and idExerciseSession="+ exerciseSession.getId() + " and date='" + new java.sql.Date(date.getTime()) + "'");

        ){
            if(rs.next()) {
                firstID = rs.getInt("idLink");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //delete the exercise link with that id
        try {
            final String query = "DELETE FROM softwareengineering.exerciselink WHERE idLink=" + firstID;
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!exerciseSession.checkIfSessionInUse(c)){//if the session is not in use
            exerciseSession.remove(c);//remove the exercise session from the database
        }
    }

    /**
     * Adds a link into the table diet
     * Between the user and the meal
     * @param meal
     */
    public void addFoodLink(Meal meal, Connection c){
        try {
            final String query = "Insert Into softwareengineering.diet Values("+ GenericController.genID("diet","idDiet",c) + ", '" + getId() + "', '" + meal.getId()+ "', '" + new java.sql.Date(new Date().getTime())+ "' )";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Gets the id of one of the meals on the date by the user
     * Removes that link in diet table
     * Checks to see if the meal is in use anywhere else
     * If it isnt remove it
     * @param date date to remove meal on
     * @param meal meal to remove link with
     */
    public void removeFoodLink(Date date,Meal meal, Connection c){
        int firstID = -1;
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.diet WHERE idUser=" + getId() + " and idMeal="+ meal.getId() + " and date='" + new java.sql.Date(date.getTime()) + "'");

        ){
            if(rs.next()) {
                firstID = rs.getInt("idDiet");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            final String query = "DELETE FROM softwareengineering.Diet WHERE idDiet=" + firstID;
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!meal.checkIfInUse(c)){
            meal.remove(c);
        }
    }

    /**
     * Get all the attrubuites of a weekly summary
     * @param commencing week commencing in this date
     * @return all the attributes in the weekly summary as an arraylist
     */
    public ArrayList<String> getWeeklySummary(Date commencing, Connection c){
        GenericController db = new GenericController();
        ArrayList<String> r = new ArrayList<>();
        try (
                Statement stmnt = c.createStatement();
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
    /**
     * Update the summary info of id
     * To the calories during
     * @param id id of the weekly summary to update
     * @param calDuringExercise new calories burnt during exercise
     * @param calDuringEating new calories eat
     * @param weight new weight
     */
    public void updateSummary(int id,int calDuringExercise,int calDuringEating,double weight, Connection c){
        final String query = "UPDATE softwareengineering.WeeklySummary SET caloriesBurnt="+calDuringExercise+",caloriesConsumed="+calDuringEating+",weight="+weight+" Where idWeeklySummary= "+ id;
        try (
                PreparedStatement pstmt = c.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param commencing the date of the start of the week
     * @param calDuringExercise calories burnt during exercise
     * @param calDuringEating calories during eating
     * @param weight new weight
     */
    public void newSummary(Date commencing,int calDuringExercise,int calDuringEating,double weight, Connection c){
        try {
            final String query = "Insert Into softwareengineering.WeeklySummary Values("+GenericController.genID("WeeklySummary","idWeeklySummary",c)+","+getId()+",'"+new java.sql.Date(commencing.getTime()) +"',"+calDuringExercise+","+calDuringEating+","+weight+")";
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
