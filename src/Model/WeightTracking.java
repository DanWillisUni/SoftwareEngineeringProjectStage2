package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class WeightTracking {
    User user;
    int weight;
    Date date;

    WeightTracking(User user,int weight){
        this.user=user;
        this.weight=weight;
        this.date = new Date();
    }
    WeightTracking(User user,int weight,Date date){
        this.user=user;
        this.weight=weight;
        this.date = date;
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
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.weighttracking Values("+ getUser().getId() + ", '" + new java.sql.Date(getDate().getTime()) + "', '" + getWeight() + "' )";
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
            final String query = "DELETE FROM softwareengineering.weighttracking WHERE idUser = "+user.getId() + " AND date = " + new java.sql.Date(getDate().getTime()) ;
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
