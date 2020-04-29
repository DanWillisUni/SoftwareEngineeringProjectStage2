package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class WeightGoal {
    int id;
    User user;
    int targetWeight;
    Date set;
    Date due;
    boolean toLoose;//true if the goal is to get under targetweight

    WeightGoal(int id, User user, int targetWeight, Date set, Date due, boolean toLoose){
        this.id = id;
        this.user=user;
        this.targetWeight=targetWeight;
        this.set=set;
        this.due=due;
        this.toLoose=toLoose;
    }
    public WeightGoal(User user, int targetWeight, Date due, boolean toLoose){
        GenericDatabaseController db = new GenericDatabaseController();
        this.id = db.genID("GoalWeight","idGoalWeight");
        this.user=user;
        this.targetWeight=targetWeight;
        this.set= new Date();
        this.due=due;
        this.toLoose=toLoose;
    }

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
        if (user.getWeight()>0){
            if (user.getWeight() >= targetWeight){
                return toLoose;
            } else {
                return !toLoose;
            }
        }
        return false;
    }
    public boolean isOverdue(){
        Date today = new Date();
        return (today.getTime()>due.getTime());
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.goalweight Values("+ getId() + ", '" + getUser().getId() + "', '" + getTargetWeight()+ "', '" + new java.sql.Date(getSet().getTime())+ "', '" + new java.sql.Date(getDue().getTime())+  "', '" + (getToLoose() ? 1 : 0) +"' )";
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

    public static ArrayList<WeightGoal> getAll(User u){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<WeightGoal> r =  new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
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

