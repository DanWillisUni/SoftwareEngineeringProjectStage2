package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

}
