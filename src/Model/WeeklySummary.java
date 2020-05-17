package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class WeeklySummary {
    int id;
    User user;
    int caloriesBurntInTotal;
    int caloriesConsumedInTotal;
    double weight;
    Date commencing;

    WeeklySummary(int id, User user, int caloriesBurntIntTotal, int caloriesConsumedInTotal, double weight, Date commencing){
        this.id=id;
        this.user=user;
        this.caloriesBurntInTotal=caloriesBurntIntTotal;
        this.caloriesConsumedInTotal=caloriesConsumedInTotal;
        this.weight=weight;
        this.commencing=commencing;
    }
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
    public void updateWeight(double weight){
        this.weight=weight;
    }

    private WeeklySummary getPrev(Connection c){
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.weeklySummary where idUser = "+getUser().getId() + "ORDER BY weekCommencing desc");

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
     *
     * @param c
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
