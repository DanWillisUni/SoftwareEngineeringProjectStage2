package Model;

import java.sql.*;
import java.util.Date;

public class WeeklySummary {
    int id;
    User user;
    int caloriesBurntInTotal;
    int caloriesConsumedInTotal;
    double weight;
    Date commencing;

    /**
     * Constructor
     * This constructor is used in the getWeeklySummary function in User
     * @param id the id of the weekly summary
     * @param user the user obj of the weekly summary
     * @param caloriesBurntIntTotal the calories burnt in the weekly summary
     * @param caloriesConsumedInTotal the calories consumed in total in the weekly summary
     * @param weight the weight of the user in the weekly summary
     * @param commencing the date of the first day of the week
     */
    WeeklySummary(int id, User user, int caloriesBurntIntTotal, int caloriesConsumedInTotal, double weight, Date commencing){
        this.id=id;
        this.user=user;
        this.caloriesBurntInTotal=caloriesBurntIntTotal;
        this.caloriesConsumedInTotal=caloriesConsumedInTotal;
        this.weight=weight;
        this.commencing=commencing;
    }
    /**
     * Constructor
     * @param user the user that the weekly summary is for
     * @param commencing the date commencing
     * @param c connection to the database
     */
    public WeeklySummary(User user, Date commencing, Connection c){
        this.id = -1;
        this.user=user;
        this.caloriesBurntInTotal=0;
        this.caloriesConsumedInTotal=0;
        WeeklySummary p = getPrev(c);
        if (p!=null){
            this.weight=p.getWeight();
        } else {
            this.weight=0;
        }
        this.commencing=commencing;
    }
    /**
     * Constructor
     * @param user the user that the weekly summary applies to
     * @param commencing the date of the week starting
     * @param weight the weight of the user
     */
    public WeeklySummary(User user, Date commencing,double weight){
        this.id = -1;
        this.user=user;
        this.caloriesBurntInTotal=0;
        this.caloriesConsumedInTotal=0;
        this.weight=weight;
        this.commencing=commencing;
    }

    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public int getCaloriesBurntInTotal() {
        return caloriesBurntInTotal;
    }
    public int getCaloriesConsumedInTotal() {
        return caloriesConsumedInTotal;
    }
    public double getWeight() {
        return weight;
    }
    public Date getCommencing() {
        return commencing;
    }

    public void addCalBurnt(int i){
        caloriesBurntInTotal += i;
    }
    public void addCalConsu(int i){
        caloriesConsumedInTotal += i;
    }
    public void setWeight(double weight){
        this.weight=weight;
    }

    /**
     * Gets the previous weeks weekly summary obj, or the most recent one
     * @param c connection to the database
     * @return the previous weekly summary
     */
    private WeeklySummary getPrev(Connection c){
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.weeklySummary where idUser = "+getUser().getId() + " ORDER BY weekCommencing desc");

        ){
            if(rs.next()) {
                return new WeeklySummary(rs.getInt("idWeeklySummary"),User.getFromID(rs.getInt("idUser"),c),rs.getInt("caloriesBurnt"),rs.getInt("caloriesConsumed"),rs.getDouble("weight"),rs.getDate("weekCommencing"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Update the summary info of id
     * To the calories during
     */
    public void updateSummary(Connection c){
        final String query = "UPDATE softwareengineering.WeeklySummary SET caloriesBurnt="+getCaloriesBurntInTotal()+",caloriesConsumed="+getCaloriesConsumedInTotal()+",weight="+getWeight()+" Where idWeeklySummary= "+ getId();
        try (
                PreparedStatement pstmt = c.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Adds the weekly summary to the database
     * @param c connection to the database
     */
    public void add(Connection c){
        try {
            final String query = "Insert Into softwareengineering.WeeklySummary Values("+GenericDatabaseController.genID("WeeklySummary","idWeeklySummary",c)+","+getUser().getId()+",'"+new java.sql.Date(getCommencing().getTime()) +"',"+getCaloriesBurntInTotal()+","+getCaloriesConsumedInTotal()+","+getWeight()+")";
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
