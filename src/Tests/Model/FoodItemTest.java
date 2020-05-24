package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FoodItemTest {
    static FoodItem f;
    static Connection c;
    @BeforeClass
    public static void setUp() {
        f= new FoodItem(0,"apple",95,"A red apple");
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
    }

    @Test
    public void getId() {
        int act = f.getId();
        assertThat(act,is(0));
    }
    @Test
    public void getName() {
        String act = f.getName();
        assertThat(act,is("apple"));
    }
    @Test
    public void getAmountOfCalories() {
        int act = f.getAmountOfCalories();
        assertThat(act,is(95));
    }
    @Test
    public void getDescription() {
        String act = f.getDescription();
        assertThat(act,is("A red apple"));
    }
    @Test
    public void getAllLikeBig() {
        ObservableList<String> res = FoodItem.getAllLike("big",c);
        ArrayList<String> expect = new ArrayList<>();
        expect.add("big mac");
        ObservableList<String> exp = FXCollections.observableList(expect);
        assertThat(res,is(exp));
    }
    @Test
    public void getAllLikeChicken() {
        ObservableList<String> res = FoodItem.getAllLike("chicken",c);
        ArrayList<String> expect = new ArrayList<>();
        expect.add("chicken 1 breast");
        expect.add("chicken fajita wrap 1 pack");
        ObservableList<String> exp = FXCollections.observableList(expect);
        assertThat(res,is(exp));
    }
    @Test
    public void isInTableNon() {
        ObservableList<String> res = FoodItem.getAllLike("abcd",c);
        ArrayList<String> expect = new ArrayList<>();
        ObservableList<String> exp = FXCollections.observableList(expect);
        assertThat(res,is(exp));
    }
    @Test
    public void getFoodFromName() {
        FoodItem act = FoodItem.getFoodFromName("apple",c);
        assertThat(act.getId(),is(f.getId()));
        assertThat(act.getName(),is(f.getName()));
        assertThat(act.getAmountOfCalories(),is(f.getAmountOfCalories()));
        assertThat(act.getDescription(),is(""));
    }
    @Test
    public void getFoodFromID() {
        FoodItem act = FoodItem.getFoodFromID(0,c);
        assertThat(act.getId(),is(f.getId()));
        assertThat(act.getName(),is(f.getName()));
        assertThat(act.getAmountOfCalories(),is(f.getAmountOfCalories()));
        assertThat(act.getDescription(),is(""));
    }
}