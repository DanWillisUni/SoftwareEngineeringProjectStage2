package Model;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class MealTest {
    static Connection c;
    static Meal a;
    static FoodItem fooda;
    static Meal b;
    static FoodItem foodb;
    @Before
    public void setUp() {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
        fooda = new FoodItem(11,"apple",95,"");
        a = new Meal(35,fooda,1,"Dinner");//is in database
        foodb = new FoodItem(60,"Sandwich",260,"Ham and Cheese");
        b = new Meal(300,foodb,1,"Lunch");//is not in database
    }

    @Test
    public void getId() {
    }

    @Test
    public void getFood() {
    }

    @Test
    public void getQuantity() {
    }

    @Test
    public void getType() {
    }

    @Test
    public void getFoodName() {
    }

    @Test
    public void getCalories() {
    }

//    @Test
//    void add() {
//    }

    @Test
    public void getMeal() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void getFromID() {
    }

    @Test
    public void checkIfInUse() {
    }

    @Test
    public void getDays() {
    }

    @Test
    public void getDateAll() {
    }
}