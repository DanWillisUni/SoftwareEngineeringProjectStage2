package Model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
            u = new User(4,"Dan","Willis", "Dw","d@gmail.com","rNYNurbvE3aFespgQ1+WLg==",new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"),193,'M',93.4);
        } catch (ParseException e) {
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
        assertThat(act,is("rNYNurbvE3aFespgQ1+WLg=="));
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
        assertThat(act,is("rNYNurbvE3aFespgQ1+WLg=="));
        u.setPassword("password",c);
        act = u.getPassword();
        assertThat(act,is("VPVvmWAaBxFcAdqxvSYYXQ=="));
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

//    @Test
//    public void setWeight() {
//        double act = u.getWeight();
//        assertThat(act,is(93.4));
//        u.setWeight();
//        act = u.getWeight();
//        assertThat(act,is(93.4));
//    }

//    @Test
//    public void setCal() {
//        int act = u.getCal();
//        assertThat(act,is(2000));
//    }
//
//    @Test
//    public void add() {
//    }
//
//    @Test
//    public void update() {
//    }

    @Test
    public void getFromEmail() {
        User act = User.getFromEmail("d@gmail.com",c);
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
    public void getFromID() {
        User act = User.getFromID(4,c);
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

//    @Test
//    public void addWeight() {
//    }
//
//    @Test
//    public void removeWeight() {
//    }
//
//    @Test
//    public void getAllWeights() {
//    }
//
//    @Test
//    public void addExerciseSessionLink() {
//    }
//
//    @Test
//    public void removeExerciseSessionLink() {
//    }
//
//    @Test
//    public void addFoodLink() {
//    }
//
//    @Test
//    public void removeFoodLink() {
//    }
//
//    @Test
//    public void getWeeklySummary() {
//    }
}