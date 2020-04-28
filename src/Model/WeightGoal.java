package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class WeightGoal {
    int id;
    User user;
    int targetWeight;
    Date set;
    Date due;
    boolean toLoose;//true if the goal is to get under targetweight

    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public int getTargetWeight() {
        return targetWeight;
    }
    public Date getSet() {
        return set;
    }
    public Date getDue() {
        return due;
    }
    public boolean getToLoose() {
        return toLoose;
    }

    public boolean isMet(){
        if (user.getWeight() >= targetWeight){
            return !toLoose;
        } else {
            return toLoose;
        }
    }
    public boolean isOverdue(){
        return false;
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.goalweight Values("+ getId() + ", '" + getUser().getId() + "', '" + getTargetWeight()+ "', '" + new java.sql.Date(getSet().getTime())+ "', '" + new java.sql.Date(getDue().getTime())+  "', '" + getToLoose()+"' )";
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
        db.remove(getId(),"goalweight","idgoalweight");
    }
}

