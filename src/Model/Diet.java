package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Diet {
    int id;
    User user;
    Meal meal;
    Date date;

    public Diet(int id, User user, Meal meal){
        this.id=id;
        this.user = user;
        this.meal=meal;
        this.date=new Date();
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
}
