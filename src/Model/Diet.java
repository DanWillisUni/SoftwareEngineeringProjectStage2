package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Diet {
    int id;
    User user;
    Meal meal;
    Date date;

    public Diet(int id, User user, Meal meal,Date date){
        this.id=id;
        this.user = user;
        this.meal=meal;
        this.date=date;
    }
    public Diet(User user, Meal meal){
        GenericDatabaseController db = new GenericDatabaseController();
        this.id=db.genID("diet","idDiet");
        this.user = user;
        this.meal=meal;
        this.date=new Date();
    }

    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public Meal getMeal() {
        return meal;
    }
    public Date getDate() {
        return date;
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.diet Values("+ getId() + ", '" + getUser().getId() + "', '" + getMeal().getId()+ "', '" + new java.sql.Date(getDate().getTime())+ "' )";
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
        db.remove(getId(),"diet","idDiet");
    }
    public static ArrayList<Diet> getTodays(User user){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<Diet> r = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.diet where idUser ="+user.getId() +" And date = '" + new java.sql.Date(new Date().getTime()) + "'");

        ){
            while(rs.next()) {
                Meal m = Meal.getFromID(rs.getInt("idMeal"));
                r.add(new Diet(rs.getInt("idDiet"),user,m,new java.util.Date(rs.getDate("date").getTime())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static Diet getDiet(User user,Meal meal){
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.diet where idUser = "+user.getId() +" and idMeal = "+meal.getId()+" And date = '" + new java.sql.Date(new Date().getTime()) + "'");

        ){
            if(rs.next()) {
                return new Diet(rs.getInt("idDiet"),user,meal,new Date());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
