package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ExerciseTest {
    static Exercise exercise;
    static Connection c;
    @BeforeClass
    public static void setUpClass() {
        exercise = new Exercise(0,"other",0);
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
    }

    @Test
    public void getId() {
        int id = exercise.getId();
        assertThat(id,is(0));
    }

    @Test
    public void getName() {
        String name = exercise.getName();
        assertThat(name,is("other"));
    }

    @Test
    public void getCaloriesBurned() {
        int cal = exercise.getCaloriesBurned();
        assertThat(cal,is(0));
    }

    @Test
    public void getAllLikeCyc() {
        ObservableList<String> res = Exercise.getAllLike("cyc",c);
        ArrayList<String> expect = new ArrayList<>();
        expect.add("cycling (quick)");
        expect.add("spin cycle");
        expect.add("stationary cycling");
        ObservableList<String> exp = FXCollections.observableList(expect);
        assertThat(res,is(exp));
    }
    @Test
    public void getAllLikeRun() {
        ObservableList<String> res = Exercise.getAllLike("run",c);
        ArrayList<String> expect = new ArrayList<>();
        expect.add("running");
        ObservableList<String> exp = FXCollections.observableList(expect);
        assertThat(res,is(exp));
    }
    @Test
    public void getAllLikeNon() {
        ObservableList<String> res = Exercise.getAllLike("abc",c);
        ArrayList<String> expect = new ArrayList<>();
        ObservableList<String> exp = FXCollections.observableList(expect);
        assertThat(res,is(exp));
    }
    @Test
    public void isInTableOther() {
        Boolean inTab = Exercise.isInTable("other",c);
        assertThat(inTab,is(true));
    }
    @Test
    public void isInTableNon() {
        Boolean inTab = Exercise.isInTable("abc",c);
        assertThat(inTab,is(false));
    }
    @Test
    public void getExerciseFromNameOther() {
        Exercise act = Exercise.getExerciseFromName("other",c);
        assertThat(act.getId(),is(exercise.getId()));
        assertThat(act.getName(),is(exercise.getName()));
        assertThat(act.getCaloriesBurned(),is(exercise.getCaloriesBurned()));
    }
    @Test
    public void getExerciseFromNameNon() {
        Exercise act = Exercise.getExerciseFromName("abc",c);
        assertNull(act);
    }
}