package Model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GenericDatabaseControllerTest {
    static Connection c;
    @BeforeClass
    public static void setUp() {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c=genericController.getConnection();
    }

    @Test
    public void genID() {
        int act = GenericDatabaseController.genID("Foods","IdFood",c);
        assertThat(act,is(51));
    }
}