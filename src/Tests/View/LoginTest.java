package View;

import Controller.GenericController;
import Controller.LoginController;
import Model.GenericDatabaseController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextMatchers.hasText;

public class LoginTest extends ApplicationTest {
    static String[] incorrectInputs = new String[]{"123", "456"};
    @Override
    public void start (Stage stage) throws Exception {
        GenericDatabaseController genericController = new GenericDatabaseController();
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/Login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Health Tracker");
        stage.setScene(new Scene(root));
        LoginController controller = loader.<LoginController>getController();
        controller.setConnection(genericController.getConnection());
        stage.show();
    }

    @BeforeClass
    public static void setUp () throws Exception {

    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void wrongEmail () {
        for (String in:incorrectInputs){
            Label l = lookup("#errorMsg").query();
            clickOn("#email");
            write(in);
            TextField email = lookup("#email").query();
            verifyThat(email.getText(), is(in));
            clickOn("#SignIn");
            assertNotEquals(l.getText(),is(""));
        }

    }
}