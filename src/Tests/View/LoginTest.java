package View;

import Controller.GenericController;
import Controller.LoginController;
import Model.GenericDatabaseController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import java.sql.Connection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

public class LoginTest extends ApplicationTest {
    static String[] incorrectStringInputs;
    static String[] additionalInputsForPasswords;
    static Connection c;
    @Override
    public void start (Stage stage) throws Exception {
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
        controller.setConnection(c);
        stage.show();
    }

    @BeforeClass
    public static void setUp () throws Exception {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c = genericController.getConnection();
        incorrectStringInputs = new String[]{
                ",./;\'[]\\-=",
                "<>?:\"{}|_+",
                "!@#$%^&*()`~",
                "Ω≈ç√∫˜µ≤≥÷",
                "åß∂ƒ©˙∆˚¬…æ",
                "œ∑´®†¥¨ˆøπ“‘",
                "¡™£¢∞§¶•ªº–≠",
                "¸˛Ç◊ı˜Â¯˘¿",
                "ÅÍÎÏ˝ÓÔÒÚÆ☃",
                "Œ„´‰ˇÁ¨ˆØ∏”’",
                "`⁄€‹›ﬁﬂ‡°·‚—±",
                "⅛⅜⅝⅞",
                "ЁЂЃЄЅІЇЈЉЊЋЌЍЎЏАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя",
                "٠١٢٣٤٥٦٧٨٩",
                "⁰⁴⁵",
                "₀₁₂",
                "⁰⁴⁵₀₁₂",
                "ด้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็ ด้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็ ด้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็็้้้้้้้้็็็็็้้้้้็็็็",
                "'",
                "\"",
                "\'\'",
                "\"\"",
                "\'\"\'",
                "\"\'\'\'\'\"\'\"",
                "\"\'\"\'\"\'\'\'\'\"",
                "ThisStringIsTooLong!ThisStringIsTooLong!ThisStringIsTooLong!ThisStringIsTooLong!ThisStringIsTooLong!ThisStringIsTooLong!"
        };
        additionalInputsForPasswords = new String[]{
                "7chrpas",
                "noNumberPassword",
                "1234567890"
        };
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void EmailLogin () {
        for (String in:incorrectStringInputs){
            Label l = lookup("#errorMsg").query();
            clickOn("#email");
            write(in);
            TextField email = lookup("#email").query();
            verifyThat(email.getText(), is(in));
            clickOn("#SignIn");
            assertNotEquals(l.getText(),is(""));
        }
    }
    @Test
    public void PasswordLogin () {
        clickOn("#email");
        write("d@gmail.com");
        for (String in:incorrectStringInputs){
            Label l = lookup("#errorMsg").query();
            clickOn("#password");
            write(in);
            PasswordField password = lookup("#password").query();
            verifyThat(password.getText(), is(in));
            clickOn("#SignIn");
            assertNotEquals(l.getText(),is(""));
        }
    }
    @Test
    public void PasswordLoginAdditional () {
        clickOn("#email");
        write("d@gmail.com");
        for (String in:additionalInputsForPasswords){
            Label l = lookup("#errorMsg").query();
            clickOn("#password");
            write(in);
            PasswordField password = lookup("#password").query();
            verifyThat(password.getText(), is(in));
            clickOn("#SignIn");
            assertNotEquals(l.getText(),is(""));
        }
    }
    @Test
    public void EmailChange () {
        for (String in:incorrectStringInputs){
            Label l = lookup("#errorMsg").query();
            clickOn("#emailReset");
            write(in);
            TextField email = lookup("#emailReset").query();
            verifyThat(email.getText(), is(in));
            clickOn("#Reset");
            assertNotEquals(l.getText(),is(""));
        }
    }
    @Test
    public void PasswordChange () {
        Label l = lookup("#errorMsg").query();
        clickOn("#emailReset");
        write("d@gmail.com");
        for (String in:incorrectStringInputs){
            clickOn("#passwordReset");
            write(in);
            clickOn("#passwordReset2");
            write(in);
            PasswordField password = lookup("#passwordReset").query();
            PasswordField password2 = lookup("#passwordReset2").query();
            verifyThat(password.getText(), is(in));
            verifyThat(password2.getText(), is(in));
            clickOn("#Reset");
            l = lookup("#errorMsg").query();
            assertNotEquals(l.getText(),is("Your password has been reset"));
        }
    }
    @Test
    public void PasswordChangeAdditional () {
        Label l = lookup("#errorMsg").query();
        clickOn("#emailReset");
        write("d@gmail.com");
        for (String in:additionalInputsForPasswords){
            clickOn("#passwordReset");
            write(in);
            clickOn("#passwordReset2");
            write(in);
            PasswordField password = lookup("#passwordReset").query();
            PasswordField password2 = lookup("#passwordReset2").query();
            verifyThat(password.getText(), is(in));
            verifyThat(password2.getText(), is(in));
            clickOn("#Reset");
            l = lookup("#errorMsg").query();
            assertNotEquals(l.getText(),is("Your password has been reset"));
        }
    }
    @Test
    public void PasswordChangeNotMatching () {
        Label l = lookup("#errorMsg").query();
        clickOn("#emailReset");
        write("d@gmail.com");
        clickOn("#passwordReset");
        write("ThisDoesNotMatchSecondPassword");
        clickOn("#passwordReset2");
        write("ThisDoesNotMatchFirstPassword");
        PasswordField password = lookup("#passwordReset").query();
        PasswordField password2 = lookup("#passwordReset2").query();
        verifyThat(password.getText(), is("ThisDoesNotMatchSecondPassword"));
        verifyThat(password2.getText(), is("ThisDoesNotMatchFirstPassword"));
        clickOn("#Reset");
        l = lookup("#errorMsg").query();
        assertNotEquals(l.getText(),is("Your password has been reset"));
    }

}