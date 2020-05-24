package Model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    @Test
    public void add() {
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exercisesession where idExerciseSession = 29")) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        b.add(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exercisesession where idExerciseSession = 29")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idExerciseSession"),is(29));
            assertThat(rs.getInt("durationMinutes"),is(30));
            assertThat(rs.getInt("idExerciseType"),is(1));
            assertThat(rs.getInt("caloriesBurned"),is(300));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            final String query = "DELETE FROM `softwareengineering`.`exercisesession` WHERE (`idExerciseSession` = '29')";
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
    public void checkIfSessionInUseNot() {
        Boolean act = b.checkIfSessionInUse(c);
        assertThat(act,is(false));
    }
    @Test
    public void checkIfSessionInUse() {
        Boolean act = a.checkIfSessionInUse(c);
        assertThat(act,is(true));
    }
    @Test
    public void remove() {
        try {
            final String query = "INSERT INTO `softwareengineering`.`exercisesession` VALUES (29,30,1,300)";
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exercisesession where idExerciseSession = 29")) {
            assertTrue(rs.next());
            assertThat(rs.getInt("idExerciseSession"),is(29));
            assertThat(rs.getInt("durationMinutes"),is(30));
            assertThat(rs.getInt("idExerciseType"),is(1));
            assertThat(rs.getInt("caloriesBurned"),is(300));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        b.remove(c);
        try(ResultSet rs=c.createStatement().executeQuery("SELECT * FROM softwareengineering.exercisesession where idExerciseSession = 29")) {
            assertFalse(rs.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
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