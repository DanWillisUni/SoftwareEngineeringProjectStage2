package Model;

import Controller.GenericController;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeightGoal {
    int id;//id of the goal
    User user;//user the goal applies to
    double targetWeight;//target weight for the user
    Date set;//date the goal was set
    Date due;//date the goal is due
    boolean toLoose;//true if the goal is to get under targetweight

    /**
     * Constructor
     * @param id weight goal
     * @param user user obj that the goal pertains to
     * @param targetWeight the target weight of the goal
     * @param set the date the goal was set
     * @param due the date the goal is due
     * @param toLoose if the goal is to loose
     */
    WeightGoal(int id, User user, double targetWeight, Date set, Date due, boolean toLoose){
        this.id = id;
        this.user=user;
        this.targetWeight=targetWeight;
        this.set=set;
        this.due=due;
        this.toLoose=toLoose;
    }
    /**
     * Constructor used to make a new goal
     * @param user user obj that the goal pertains to
     * @param targetWeight the target weight of the goal
     * @param due the date the goal is due
     * @param toLoose if the goal is to loose
     */
    public WeightGoal(User user, double targetWeight, Date due, boolean toLoose,Connection c){
        this.id = GenericController.genID("GoalWeight","idGoalWeight",c);
        this.user=user;
        this.targetWeight=targetWeight;
        this.set= new Date();
        this.due=due;
        this.toLoose=toLoose;
    }

    /**
     * Get the id of the goal
     * @return the id of the goal
     */
    public int getId() {
        return id;
    }
    /**
     * Get the user obj
     * @return the user obj of the goal
     */
    public User getUser() {
        return user;
    }
    /**
     * Get the target weight
     * @return the target weight
     */
    public double getTargetWeight() {
        return targetWeight;
    }
    /**
     * Get the date the goal was set on
     * @return the date the goal was set on
     */
    public Date getSet() {
        return set;
    }
    /**
     * Get the date the goal is due on
     * @return the date the goal is due to be completed by
     */
    public Date getDue() {
        return due;
    }
    /**
     * Get the due date
     * Convert it to a string in dd-mm-yyyy format
     * @return the due date as a string
     */
    public String getDueStr(){
        DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        return  newFormat.format(getDue());
    }
    /**
     * Get toloose
     * @return to loose
     */
    public boolean getToLoose() {
        return toLoose;
    }

    /**
     * Determines if the goal is met by the user
     * @return true if the goal is completed
     */
    public boolean isMet(){
        if (user.getWeight()>0){
            if (user.getWeight() >= targetWeight){
                return toLoose;
            } else {
                return !toLoose;
            }
        }
        return false;
    }
    /**
     * Get if the due date of the goal is due in the past
     * @return true if the goal was due in the past
     */
    public boolean isOverdue(){
        Date today = new Date();
        return (today.getTime()>due.getTime());
    }
    /**
     * Works out the number of days till when the goal is due
     * @return the number of days till the goal
     */
    public int daysTillGoal(){
        Date current = new Date();
        long differance = getDue().getTime() - current.getTime();
        return Integer.parseInt(Long.toString(differance/86400000));
    }

    /**
     * Adds the goal to the database
     */
    public void add(Connection c){
        try {
            final String query = "Insert Into softwareengineering.goalweight Values("+ getId() + ", '" + getUser().getId() + "', '" + getTargetWeight()+ "', '" + new java.sql.Date(getSet().getTime())+ "', '" + new java.sql.Date(getDue().getTime())+  "', '" + (getToLoose() ? 1 : 0) +"' )";
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
     * Removes the goal from the database
     */
    public void remove(Connection c){
        GenericController.remove(getId(),"goalweight","idgoalweight",c);
    }

    /**
     * Gets all the goals of a user
     * @param u user
     * @return arraylist of all the users goal
     */
    public static ArrayList<WeightGoal> getAll(User u, Connection c){
        ArrayList<WeightGoal> r =  new ArrayList<>();
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.goalweight where idUser ="+u.getId() + " ORDER BY targetDate ASC");

        ){
            while(rs.next()) {
                r.add(new WeightGoal(rs.getInt("idGoalWeight"),u,rs.getInt("weightGoal"),new java.util.Date(rs.getDate("dateSet").getTime()),new java.util.Date(rs.getDate("targetDate").getTime()),rs.getBoolean("toLoose")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
}

