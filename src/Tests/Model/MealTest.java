package Model;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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
        fooda = new FoodItem(0,"apple",95,"");
        a = new Meal(35,fooda,1,"Dinner");//is in database
        foodb = new FoodItem(70,"Sandwich",260,"Ham and Cheese");
        b = new Meal(300,foodb,1,"Lunch");//is not in database
    }

    @Test
    public void getId() {
        int act = a.getId();
        assertThat(act,is(35));
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

//    @Test
//    void add() {
//    }

    @Test
    public void getMeal() {
        Meal act = Meal.getMeal(fooda,1,"Dinner",c);
        assertThat(act.getId(),is(35));
        assertThat(act.getFoodName(),is("apple"));
        assertThat(act.getQuantity(),is(1));
        assertThat(act.getCalories(),is(95));
        assertThat(act.getType(),is("Dinner"));
    }

//    @Test
//    public void remove() {
//    }

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
    public void checkIfInUse() {
        Boolean act = a.checkIfInUse(c);
        assertThat(act,is(true));
    }
    @Test
    public void checkIfInUseNot() {
        Boolean act = b.checkIfInUse(c);
        assertThat(act,is(false));
    }

//    @Test
//    public void getDays() {
//
//    }
//
//    @Test
//    public void getDateAll() {
//    }
}