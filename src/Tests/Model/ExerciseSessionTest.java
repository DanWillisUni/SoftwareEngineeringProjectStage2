package Model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ExerciseSessionTest {
    static Connection c;
    static ExerciseSession a;
    static Exercise exercisea;
    static ExerciseSession b;
    static Exercise exerciseb;
    @BeforeClass
    public static void setUp() {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
        exercisea = new Exercise(11,"rock climbing",0);
        a = new ExerciseSession(0,exercisea,49,1200);//is in database
        exerciseb = new Exercise(1,"running",10);
        b = new ExerciseSession(29,exerciseb,30,300);//is not in database
    }

    @Test
    public void getId() {
        int id = a.getId();
        assertThat(id,is(0));
    }
    @Test
    public void getExercise() {
        Exercise act = b.getExercise();
        assertThat(act.getId(),is(exerciseb.getId()));
        assertThat(act.getName(),is(exerciseb.getName()));
        assertThat(act.getCaloriesBurned(),is(exerciseb.getCaloriesBurned()));
    }

    @Test
    public void getExerciseName() {
        String act = a.getExerciseName();
        assertThat(act,is("rock climbing"));
    }
    @Test
    public void getDuration() {
        int act = b.getDuration();
        assertThat(act,is(30));
    }

    @Test
    public void getCaloriesBurned() {
        int act = a.getCaloriesBurned();
        assertThat(act,is(1200));
    }

//    @Test
//    public void add() {
//
//    }
    @Test
    public void checkIfSessionInUseNot() {
        Boolean act = b.checkIfSessionInUse(c);
        assertThat(act,is(false));
    }
    @Test
    public void checkIfSessionInUse() {
        Boolean act = a.checkIfSessionInUse(c);
        assertThat(act,is(true));
    }

//    @Test
//    public void remove() {
//
//    }

    @Test
    public void getExerciseSession() {
        ExerciseSession act = ExerciseSession.getExerciseSession(exercisea,49,1200,c);
        assertThat(act.getId(),is(a.getId()));
        assertThat(act.getExerciseName(),is("rock climbing"));
        assertThat(act.getCaloriesBurned(),is(1200));
    }
    @Test
    public void getExerciseSessionNon() {
        ExerciseSession act = ExerciseSession.getExerciseSession(exerciseb,30,300,c);
        assertNull(act);
    }

    @Test
    public void getFromID() {
        ExerciseSession act = ExerciseSession.getFromID(0,c);
        assertThat(act.getId(),is(a.getId()));
        assertThat(act.getExerciseName(),is("rock climbing"));
        assertThat(act.getCaloriesBurned(),is(1200));
    }
    @Test
    public void getFromIDNon() {
        ExerciseSession act = ExerciseSession.getFromID(30,c);
        assertNull(act);
    }

}