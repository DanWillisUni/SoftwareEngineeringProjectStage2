package Model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserTest {
    static Connection c;
    static User u;
    @BeforeClass
    public static void setUp() {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
    }
    @Before
    public void each(){
        try {
            u = new User(4,"Dan","Willis", "Dw","d@gmail.com","miN+0R4PrCGXGexOhZk4SA==",new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"),193,'M',93.4);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String query = "UPDATE softwareengineering.User SET forename = 'Dan', surname = 'Willis',email = 'd@gmail.com',username = 'Dw',DOB = '2000-08-02',height = 193,gender = 'M',weight = 93.4,calories=2000,password = 'miN+0R4PrCGXGexOhZk4SA==' Where idUser= 4";
        try (
                PreparedStatement pstmt = c.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getId() {
        int act = u.getId();
        assertThat(act,is(4));
    }
    @Test
    public void getForename() {
        String act = u.getForename();
        assertThat(act,is("Dan"));
    }
    @Test
    public void getSurname() {
        String act = u.getSurname();
        assertThat(act,is("Willis"));
    }
    @Test
    public void getUsername() {
        String act = u.getUsername();
        assertThat(act,is("Dw"));
    }
    @Test
    public void getEmail() {
        String act = u.getEmail();
        assertThat(act,is("d@gmail.com"));
    }
    @Test
    public void getPassword() {
        String act = u.getPassword();
        assertThat(act,is("miN+0R4PrCGXGexOhZk4SA=="));
    }
    @Test
    public void getDOB() {
        Date act = u.getDOB();
        try {
            assertThat(act,is(new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getHeight() {
        int act = u.getHeight();
        assertThat(act,is(193));
    }
    @Test
    public void getGender() {
        char act = u.getGender();
        assertThat(act,is('M'));
    }
    @Test
    public void getWeight() {
        double act = u.getWeight();
        assertThat(act,is(93.4));
    }
    @Test
    public void getCal() {
        int act = u.getCal();
        assertThat(act,is(2000));
    }
    @Test
    public void setForename() {
        String act = u.getForename();
        assertThat(act,is("Dan"));
        u.setForename("Daniel");
        act = u.getForename();
        assertThat(act,is("Daniel"));
    }
    @Test
    public void setSurname() {
        String act = u.getSurname();
        assertThat(act,is("Willis"));
        u.setSurname("Lastname");
        act = u.getSurname();
        assertThat(act,is("Lastname"));
    }
    @Test
    public void setUsername() {
        String act = u.getUsername();
        assertThat(act,is("Dw"));
        u.setUsername("DanWillis");
        act = u.getUsername();
        assertThat(act,is("DanWillis"));
    }
    @Test
    public void setEmail() {
        String act = u.getEmail();
        assertThat(act,is("d@gmail.com"));
        u.setEmail("dan@gmail.com");
        act = u.getEmail();
        assertThat(act,is("dan@gmail.com"));
    }
    @Test
    public void setPassword() {
        String act = u.getPassword();
        assertThat(act,is("miN+0R4PrCGXGexOhZk4SA=="));
        u.setPassword("password",c);
        act = u.getPassword();
        assertThat(act,is("VPVvmWAaBxFcAdqxvSYYXQ=="));

        final String query = "UPDATE softwareengineering.User SET password = 'miN+0R4PrCGXGexOhZk4SA==' Where idUser= 4";
        try (
                PreparedStatement pstmt = c.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void setDOB() {
        Date act = u.getDOB();
        try {
            assertThat(act, is(new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000")));
            u.setDOB(new SimpleDateFormat("dd/MM/yyyy").parse("02/07/2000"));
            act = u.getDOB();
            assertThat(act, is(new SimpleDateFormat("dd/MM/yyyy").parse("02/07/2000")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void setHeight() {
        int act = u.getHeight();
        assertThat(act,is(193));
        u.setHeight(190);
        act = u.getHeight();
        assertThat(act,is(190));
    }
    @Test
    public void setGender() {
        char act = u.getGender();
        assertThat(act,is('M'));
        u.setGender('F');
        act = u.getGender();
        assertThat(act,is('F'));
    }
    @Test
    public void setWeight() {
        double act = u.getWeight();
        assertThat(act,is(93.4));
        u.setWeight(92.0,c);
        act = u.getWeight();
        assertThat(act,is(92.0));
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.user where idUser = 4")) {
            assertTrue(rs.next());
            assertThat(rs.getDouble("weight"),is(92.0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    @Test
    public void setCal() {
        int act = u.getCal();
        assertThat(act,is(2000));
    }
    @Test
    public void add() {
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.User where idUser = 10")) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        User n = null;
        try {
            n = new User(10,"Daniel","Willis", "DanWillis","dan@gmail.com","miN+0R4PrCGXGexOhZk4SA==",new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"),193,'M',93.4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        n.add(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.user where idUser = 10")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(10));
            assertThat(rs.getString("forename"),is("Daniel"));
            assertThat(rs.getString("surname"),is("Willis"));
            assertThat(rs.getString("username"),is("DanWillis"));
            assertThat(rs.getString("email"),is("dan@gmail.com"));
            assertThat(rs.getDate("DOB"),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000")));
            assertThat(rs.getInt("height"),is(193));
            assertThat(rs.getDouble("weight"),is(93.4));
            assertThat(rs.getString("gender"),is("M"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`User` WHERE (`idUser` = '10')";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void update() {
        try {
            final String query = "INSERT into `softwareengineering`.`user` VALUES (10,'Dan','Willis','dan.willis@gmail.com','DWillis','password','2000-08-02',193,'M',92.3,2300)";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.user where idUser = 10")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(10));
            assertThat(rs.getString("forename"),is("Dan"));
            assertThat(rs.getString("surname"),is("Willis"));
            assertThat(rs.getString("username"),is("DWillis"));
            assertThat(rs.getString("email"),is("dan.willis@gmail.com"));
            assertThat(rs.getDate("DOB"),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000")));
            assertThat(rs.getInt("height"),is(193));
            assertThat(rs.getDouble("weight"),is(92.3));
            assertThat(rs.getString("gender"),is("M"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User n = null;
        try {
            n = new User(10,"Daniel","Willis", "DanWillis","dan@gmail.com","miN+0R4PrCGXGexOhZk4SA==",new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"),193,'M',93.4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        n.update(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.user where idUser = 10")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(10));
            assertThat(rs.getString("forename"),is("Daniel"));
            assertThat(rs.getString("surname"),is("Willis"));
            assertThat(rs.getString("username"),is("DanWillis"));
            assertThat(rs.getString("email"),is("dan@gmail.com"));
            assertThat(rs.getDate("DOB"),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000")));
            assertThat(rs.getInt("height"),is(193));
            assertThat(rs.getDouble("weight"),is(93.4));
            assertThat(rs.getString("gender"),is("M"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`User` WHERE (`idUser` = '10')";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getFromEmail() {
        User act = User.getFromEmail("d@gmail.com",c);
        assertThat(act.getId(),is(4));
        assertThat(act.getForename(),is("Dan"));
        assertThat(act.getSurname(),is("Willis"));
        assertThat(act.getEmail(),is("d@gmail.com"));
        assertThat(act.getUsername(),is("Dw"));
        assertThat(act.getPassword(),is("miN+0R4PrCGXGexOhZk4SA=="));
        try {
            assertThat(act.getDOB(),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertThat(act.getGender(),is('M'));
        assertThat(act.getHeight(),is(193));
        assertThat(act.getWeight(),is(93.4));
    }
    @Test
    public void getFromID() {
        User act = User.getFromID(4,c);
        assertThat(act.getId(),is(4));
        assertThat(act.getForename(),is("Dan"));
        assertThat(act.getSurname(),is("Willis"));
        assertThat(act.getEmail(),is("d@gmail.com"));
        assertThat(act.getUsername(),is("Dw"));
        assertThat(act.getPassword(),is("miN+0R4PrCGXGexOhZk4SA=="));
        try {
            assertThat(act.getDOB(),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertThat(act.getGender(),is('M'));
        assertThat(act.getHeight(),is(193));
        assertThat(act.getWeight(),is(93.4));
    }

    @Test
    public void addWeight() {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.weightTracking where idUser = 4 and date = " + strDate)) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        u.addWeight(93,c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.weightTracking where idUser = 4 and date = '" + strDate + "'")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getString("date"),is(strDate));
            assertThat(rs.getDouble("weight"),is(93.0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`WeightTracking` WHERE idUser = 4 and date = '" + strDate +"'";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void removeWeight() {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            final String query = "INSERT INTO `softwareengineering`.`WeightTracking` VALUES (1000,4,'" + strDate +"',93.0) ";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.weightTracking where idUser = 4 and date = '" + strDate + "'")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getString("date"),is(strDate));
            assertThat(rs.getDouble("weight"),is(93.0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        u.removeWeight(new Date(),c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.weightTracking where idUser = 4 and date = " + strDate)) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Test
    public void addExerciseSessionLink() {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exerciseLink where idUser = 4 and date = " + strDate)) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Exercise exercisea = new Exercise(11,"rock climbing",0);
        ExerciseSession a = new ExerciseSession(0, exercisea, 49, 1200);
        u.addExerciseSessionLink(a,c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exerciseLink where idUser = 4 and date = '" + strDate + "'")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getString("date"),is(strDate));
            assertThat(rs.getInt("idExerciseSession"),is(0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`exerciseLink` WHERE idUser = 4 and date = '" + strDate +"'";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void removeExerciseSessionLink() {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            final String query = "insert Into `softwareengineering`.`exerciseLink` Values (300,4,0,'" + strDate +"')";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exerciseLink where idUser = 4 and date = '" + strDate + "'")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getString("date"),is(strDate));
            assertThat(rs.getInt("idExerciseSession"),is(0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Exercise exercisea = new Exercise(11,"rock climbing",0);
        ExerciseSession a = new ExerciseSession(0, exercisea, 49, 1200);
        u.removeExerciseSessionLink(new Date(),a,c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exerciseLink where idUser = 4 and date = " + strDate)) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Test
    public void addFoodLink() {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.diet where idUser = 4 and date = " + strDate)) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        FoodItem fooda = new FoodItem(0,"apple",95,"");
        Meal a = new Meal(0,fooda,1,"Dinner");//is in database
        u.addFoodLink(a,c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.diet where idUser = 4 and date = '" + strDate + "'")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getString("date"),is(strDate));
            assertThat(rs.getInt("idMeal"),is(0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`diet` WHERE idUser = 4 and date = '" + strDate +"'";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void removeFoodLink() {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            final String query = "INSERT INTO `softwareengineering`.`diet` VALUES (1000,4,0,'" + strDate +"')";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.diet where idUser = 4 and date = '" + strDate + "'")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getString("date"),is(strDate));
            assertThat(rs.getInt("idMeal"),is(0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        FoodItem fooda = new FoodItem(0,"apple",95,"");
        Meal a = new Meal(0,fooda,1,"Dinner");//is in database
        u.removeFoodLink(new Date(),a,c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.diet where idUser = 4 and date = " + strDate)) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}