package Model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WeightGoalTest {
    static Connection c;
    static User u;
    static WeightGoal g;
    @BeforeClass
    public static void setUp() {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
        try {
            u = new User(4,"Dan","Willis", "Dw","d@gmail.com","rNYNurbvE3aFespgQ1+WLg==",new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"),193,'M',93.4);
            g=new WeightGoal(0,u,93,new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020"),new SimpleDateFormat("dd/MM/yyyy").parse("02/06/2020"),true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getId() {
        int act = g.getId();
        assertThat(act,is(0));
    }

    @Test
    public void getUser() {
        User act = g.getUser();
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
    public void getTargetWeight() {
        double act = g.getTargetWeight();
        assertThat(act,is(93.0));
    }

    @Test
    public void getSet() {
        Date act = g.getSet();
        try {
            assertThat(act,is(new SimpleDateFormat("dd/MM/yyyy").parse("02/05/2020")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDue() {
        Date act = g.getSet();
        try {
            assertThat(act,is(new SimpleDateFormat("dd/MM/yyyy").parse("02/06/2020")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDueStr() {
        String act = g.getDueStr();
        assertThat(act,is("02-06-2020"));
    }

    @Test
    public void getToLoose() {
        boolean act = g.getToLoose();
        assertThat(act,is(true));
    }

    @Test
    public void isMet() {
        boolean act = g.isMet();
        assertThat(act,is(false));
    }

    @Test
    public void isOverdue() {
        boolean act = g.isOverdue();
        assertThat(act,is(false));
    }

    @Test
    public void daysTillGoal() {
        long diff = 0;
        try {
            diff = new SimpleDateFormat("dd/MM/yyyy").parse("02/06/2020").getTime() - new Date().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days = (int) (diff/86400000L);
        int act = g.daysTillGoal();
        assertThat(act,is(days));
    }

//    @Test
//    void add() {
//    }
//
//    @Test
//    void remove() {
//    }
//
//    @Test
//    void getAll() {
//    }
}