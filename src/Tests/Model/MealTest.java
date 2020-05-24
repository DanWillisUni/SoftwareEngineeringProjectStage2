package Model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MealTest {
    static Connection c;
    static Meal a;
    static FoodItem fooda;
    static Meal b;
    static FoodItem foodb;
    @BeforeClass
    public static void setUp() {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
        fooda = new FoodItem(0,"apple",95,"");
        a = new Meal(300,fooda,1,"Dinner");//is in database
        foodb = new FoodItem(70,"Sandwich",260,"Ham and Cheese");
        b = new Meal(301,foodb,1,"Lunch");//is not in database
    }

    @Test
    public void getId() {
        int act = a.getId();
        assertThat(act,is(300));
    }
    @Test
    public void getFood() {
        FoodItem act = b.getFood();
        assertThat(act.getId(),is(foodb.getId()));
        assertThat(act.getName(),is(foodb.getName()));
        assertThat(act.getAmountOfCalories(),is(foodb.getAmountOfCalories()));
        assertThat(act.getDescription(),is("Ham and Cheese"));
    }
    @Test
    public void getQuantity() {
        int act = b.getQuantity();
        assertThat(act,is(1));
    }
    @Test
    public void getType() {
        String act = a.getType();
        assertThat(act,is("Dinner"));
    }
    @Test
    public void getFoodName() {
        String act = b.getFoodName();
        assertThat(act,is("Sandwich"));
    }
    @Test
    public void getCalories() {
        int act = a.getCalories();
        assertThat(act,is(95));
    }
    @Test
    public void add() {
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.meal where idMeal = 300")) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        a.add(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.meal where idMeal = 300")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idMeal"),is(300));
            assertThat(rs.getInt("idFood"),is(0));
            assertThat(rs.getInt("quantity"),is(1));
            assertThat(rs.getString("mealCategory"),is("Dinner"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`meal` WHERE (`idmeal` = '300')";
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
    public void getMeal() {
        Meal act = Meal.getMeal(fooda,1,"Dinner",c);
        assertThat(act.getId(),is(35));
        assertThat(act.getFoodName(),is("apple"));
        assertThat(act.getQuantity(),is(1));
        assertThat(act.getCalories(),is(95));
        assertThat(act.getType(),is("Dinner"));
    }
    @Test
    public void remove() {
        try {
            final String query = "INSERT into `softwareengineering`.`meal` VALUES (300,0,1,'Dinner')";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.meal where idMeal = 300")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idMeal"),is(300));
            assertThat(rs.getInt("idFood"),is(0));
            assertThat(rs.getInt("quantity"),is(1));
            assertThat(rs.getString("mealCategory"),is("Dinner"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        a.remove(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.meal where idMeal = 300")) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Test
    public void getFromID() {
        Meal act = Meal.getFromID(35,c);
        assertThat(act.getId(),is(35));
        assertThat(act.getFoodName(),is("apple"));
        assertThat(act.getQuantity(),is(1));
        assertThat(act.getCalories(),is(95));
        assertThat(act.getType(),is("Dinner"));
    }
    @Test
    public void checkIfInUseNot() {
        Boolean act = b.checkIfInUse(c);
        assertThat(act,is(false));
    }
}