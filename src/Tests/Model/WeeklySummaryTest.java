package Model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
            s=new WeeklySummary(0,u,2500,10000,94.3,new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getId() {
    }

    @Test
    public void getUser() {
    }

    @Test
    public void getCaloriesBurntInTotal() {
    }

    @Test
    public void getCaloriesConsumedInTotal() {
    }

    @Test
    public void getWeight() {
    }

    @Test
    public void getCommencing() {
    }

    @Test
    public void addCalBurnt() {
    }

    @Test
    public void addCalConsu() {
    }

    @Test
    public void setWeight() {
    }

    @Test
    public void updateSummary() {
    }

    @Test
    public void add() {
    }
}