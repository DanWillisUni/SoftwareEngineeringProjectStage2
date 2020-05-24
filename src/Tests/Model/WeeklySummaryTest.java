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

public class WeeklySummaryTest {
    static Connection c;
    static User u;
    static WeeklySummary s;
    @BeforeClass
    public static void setUp() {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
        try {
            u = new User(4,"Dan","Willis", "Dw","d@gmail.com","rNYNurbvE3aFespgQ1+WLg==",new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"),193,'M',93.4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Before
    public void each(){
        try {
            s=new WeeklySummary(20,u,2500,10000,94.3,new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getId() {
        int act = s.getId();
        assertThat(act,is(20));
    }
    @Test
    public void getUser() {
        User act = s.getUser();
        assertThat(act.getId(),is(4));
        assertThat(act.getForename(),is("Dan"));
        assertThat(act.getSurname(),is("Willis"));
        assertThat(act.getEmail(),is("d@gmail.com"));
        assertThat(act.getUsername(),is("Dw"));
        assertThat(act.getPassword(),is("rNYNurbvE3aFespgQ1+WLg=="));
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
    public void getCaloriesBurntInTotal() {
        int act = s.getCaloriesBurntInTotal();
        assertThat(act,is(2500));
    }
    @Test
    public void getCaloriesConsumedInTotal() {
        int act = s.getCaloriesConsumedInTotal();
        assertThat(act,is(10000));
    }
    @Test
    public void getWeight() {
        double act = s.getWeight();
        assertThat(act,is(94.3));
    }
    @Test
    public void getCommencing() {
        Date act = s.getCommencing();
        try {
            assertThat(act,is(new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void addCalBurnt() {
        s.addCalBurnt(500);
        assertThat(s.getCaloriesBurntInTotal(),is(3000));
    }
    @Test
    public void addCalConsu() {
        s.addCalConsu(1000);
        assertThat(s.getCaloriesConsumedInTotal(),is(11000));
    }
    @Test
    public void setWeight() {
        s.setWeight(93.2);
        assertThat(s.getWeight(),is(93.2));
    }
    @Test
    public void updateSummary() {
        try {
            final String query = "INSERT into `softwareengineering`.`weeklySummary` VALUES (20,4,'2020-05-02',2000,9000,92.3)";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.WeeklySummary where idWeeklySummary = 20")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idWeeklySummary"),is(20));
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getDate("weekCommencing"),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020")));
            assertThat(rs.getInt("caloriesBurnt"),is(2000));
            assertThat(rs.getInt("caloriesConsumed"),is(9000));
            assertThat(rs.getDouble("weight"),is(92.3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        s.updateSummary(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.WeeklySummary where idWeeklySummary = 20")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idWeeklySummary"),is(20));
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getDate("weekCommencing"),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020")));
            assertThat(rs.getInt("caloriesBurnt"),is(2500));
            assertThat(rs.getInt("caloriesConsumed"),is(10000));
            assertThat(rs.getDouble("weight"),is(94.3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`WeeklySummary` WHERE (`idWeeklySummary` = '20')";
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
    public void add() {
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.weeklySummary where idWeeklySummary = 20")) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        s.add(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.WeeklySummary where idWeeklySummary = 20")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idWeeklySummary"),is(20));
            assertThat(rs.getInt("idUser"),is(4));
            assertThat(rs.getDate("weekCommencing"),is(new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020")));
            assertThat(rs.getInt("caloriesBurnt"),is(2500));
            assertThat(rs.getInt("caloriesConsumed"),is(10000));
            assertThat(rs.getDouble("weight"),is(94.3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`WeeklySummary` WHERE (`idWeeklySummary` = '20')";
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