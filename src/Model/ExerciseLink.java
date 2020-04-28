package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class ExerciseLink {
    int id;
    ExerciseSession session;
    User user;
    Date date;

    ExerciseLink(int id,ExerciseSession session, User user, Date date){
        this.id = id;
        this.session = session;
        this.user = user;
        this.date = date;
    }
    public ExerciseLink(ExerciseSession session, User user){
        GenericDatabaseController db = new GenericDatabaseController();
        this.id = db.genID("exerciseLink","idLink");
        this.session = session;
        this.user = user;
        this.date = new Date();
    }

    public int getId() {
        return id;
    }
    public ExerciseSession getSession() {
        return session;
    }
    public User getUser() {
        return user;
    }
    public Date getDate() {
        return date;
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.exerciseLink Values("+ getId() + ", '" + getUser().getId() + "', '" + getSession().getId()+ "', '" +new java.sql.Date(getDate().getTime())+ "' )";
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
        db.remove(getId(),"exerciseLink","idLink");
    }

    public static ArrayList<ExerciseLink> getTodays(User u){
        GenericDatabaseController db = new GenericDatabaseController();
        ArrayList<ExerciseLink> r = new ArrayList<>();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink where idUser = "+u.getId() +" And date = '" + new java.sql.Date(new Date().getTime()) + "'");

        ){
            while(rs.next()) {
                ExerciseSession s = ExerciseSession.getFromID(rs.getInt("idExerciseSession"));
                r.add(new ExerciseLink(rs.getInt("idLink"),s,u,new Date()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public static ExerciseLink getLink(User user,ExerciseSession session){
        GenericDatabaseController db = new GenericDatabaseController();
        try (
                Statement stmnt = db.getConnection().createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.exerciselink where idUser = "+user.getId() +" and idexercisesession = "+session.getId()+" And date = '" + new java.sql.Date(new Date().getTime()) + "'");

        ){
            if(rs.next()) {
                return new ExerciseLink(rs.getInt("idLink"),session,user,new Date());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
