package View;

import Controller.GenericController;
import Controller.LoginController;
import Controller.RegistrationController;
import Model.GenericDatabaseController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import javafx.util.converter.LocalDateStringConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

public class RegistrationTest extends ApplicationTest {
    static String[] incorrectStringInputs;
    static String[] additionalInputsForPasswords;
    static Connection c;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/Registration.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Health Tracker");
        stage.setScene(new Scene(root));
        RegistrationController controller = loader.<RegistrationController>getController();
        controller.setConnection(c);
        stage.show();
    }

    @BeforeClass
    public static void setUp() throws Exception {
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
    @Before
    public void each(){
        ((TextField)lookup("#forename").query()).setText("Dan");
        ((TextField)lookup("#surname").query()).setText("Willis");
        ((TextField)lookup("#username").query()).setText("DanWillis");
        ((TextField)lookup("#email").query()).setText("Dan.Willis@gmail.com");
        ((PasswordField)lookup("#password").query()).setText("ValidPassword1");
        ((PasswordField)lookup("#password2").query()).setText("ValidPassword1");
        ((DatePicker)lookup("#DOB").query()).setValue(LocalDate.parse("2000-08-02"));
        ((ComboBox)lookup("#gender").query()).setValue("Male");
    }
    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void ForeNameRegistration() {
        TextField f = lookup("#forename").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#forename");
            write(in);
            f = lookup("#forename").query();
            verifyThat(f.getText(), is(in));
            clickOn("#registerButton");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void SurnameRegistration() {
        TextField f = lookup("#surname").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#surname");
            write(in);
            f = lookup("#surname").query();
            verifyThat(f.getText(), is(in));
            clickOn("#registerButton");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void UserNameRegistration() {
        TextField f = lookup("#username").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#username");
            write(in);
            f = lookup("#username").query();
            verifyThat(f.getText(), is(in));
            clickOn("#registerButton");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void EmailRegistration() {
        TextField f = lookup("#email").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#email");
            write(in);
            f = lookup("#email").query();
            verifyThat(f.getText(), is(in));
            clickOn("#registerButton");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void PasswordRegistration() {
        PasswordField f = lookup("#password").query();
        f.setText("");
        PasswordField f2 = lookup("#password2").query();
        f2.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#password");
            write(in);
            clickOn("#password2");
            write(in);
            f = lookup("#password").query();
            verifyThat(f.getText(), is(in));
            f2 = lookup("#password2").query();
            verifyThat(f2.getText(), is(in));
            clickOn("#registerButton");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void PasswordRegistrationAdditional() {
        PasswordField f = lookup("#password").query();
        f.setText("");
        PasswordField f2 = lookup("#password2").query();
        f2.setText("");
        for (String in : additionalInputsForPasswords) {
            Label l = lookup("#errorMsg").query();
            clickOn("#password");
            write(in);
            clickOn("#password2");
            write(in);
            f = lookup("#password").query();
            verifyThat(f.getText(), is(in));
            f2 = lookup("#password2").query();
            verifyThat(f2.getText(), is(in));
            clickOn("#registerButton");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void GenderRegistration() {
        ComboBox f = lookup("#gender").query();
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            f.setValue(in);
            f = lookup("#gender").query();
            verifyThat(f.getValue(), is(in));
            clickOn("#registerButton");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void DateRegistration() {
        Label l = lookup("#errorMsg").query();
        ((DatePicker)lookup("#DOB").query()).setValue(LocalDate.parse("2100-08-02"));
        clickOn("#registerButton");
        assertNotEquals(l.getText(), is(""));
        ((DatePicker)lookup("#DOB").query()).setValue(LocalDate.parse("1900-08-02"));
        clickOn("#registerButton");
        assertNotEquals(l.getText(), is(""));
    }
}