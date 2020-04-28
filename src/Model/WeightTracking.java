package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class WeightTracking {
    int id;
    User user;
    int weight;
    Date date;

    public WeightTracking(User user, int weight){
        GenericDatabaseController db = new GenericDatabaseController();
        this.id = db.genID("WeightTracking","idWeightTracking");
        this.user=user;
        this.weight=weight;
        this.date = new Date();
    }
    public WeightTracking(int id,User user,int weight,Date date){
        this.id=id;
        this.user=user;
        this.weight=weight;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public int getWeight() {
        return weight;
    }
    public Date getDate() {
        return date;
    }

    public void add(){
        remove();
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.weighttracking Values(" + getId()+", " + getUser().getId() + ", '" + new java.sql.Date(getDate().getTime()) + "', '" + getWeight() + "' )";
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
        try {
            final String query = "DELETE FROM softwareengineering.weighttracking WHERE idUser = "+user.getId() + " AND date = '" + new java.sql.Date(getDate().getTime()) + "'" ;
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<WeightTracking> getAll(User user){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<WeightTracking> r = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.weighttracking where idUser ="+user.getId());

        ){
            while(rs.next()) {
                r.add(new WeightTracking(rs.getInt("idWeightTracking"),user,rs.getInt("weight"),new java.util.Date(rs.getDate("date").getTime())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
}
