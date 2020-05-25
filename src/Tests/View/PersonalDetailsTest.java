package View;

import Controller.GenericController;
import Controller.PersonalDetailsController;
import Model.GenericDatabaseController;

import Model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

public class PersonalDetailsTest extends ApplicationTest {
    static String[] incorrectStringInputs;
    static String[] additionalInputsForPasswords;
    static Connection c;
    static User u;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/PersonalDetails.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            u = new User(4,"Dan","Willis", "Dw","d@gmail.com","miN+0R4PrCGXGexOhZk4SA==",new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"),193,'M',93.4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        stage.setTitle("Health Tracker");
        stage.setScene(new Scene(root));
        PersonalDetailsController controller = loader.<PersonalDetailsController>getController();
        controller.setUser(u,c);
        controller.setUpDisplay();
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

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void ForeNamePersonalDetails() {
        TextField f = lookup("#forename").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#forename");
            write(in);
            f = lookup("#forename").query();
            verifyThat(f.getText(), is(in));
            clickOn("#update");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void SurnamePersonalDetails() {
        TextField f = lookup("#surname").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#surname");
            write(in);
            f = lookup("#surname").query();
            verifyThat(f.getText(), is(in));
            clickOn("#update");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void UserNamePersonalDetails() {
        TextField f = lookup("#username").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#username");
            write(in);
            f = lookup("#username").query();
            verifyThat(f.getText(), is(in));
            clickOn("#update");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void EmailPersonalDetails() {
        TextField f = lookup("#email").query();
        f.setText("");
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            clickOn("#email");
            write(in);
            f = lookup("#email").query();
            verifyThat(f.getText(), is(in));
            clickOn("#update");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void PasswordPersonalDetails() {
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
            clickOn("#update");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void PasswordPersonalDetailsAdditional() {
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
            clickOn("#update");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void GenderPersonalDetails() {
        ComboBox f = lookup("#gender").query();
        for (String in : incorrectStringInputs) {
            Label l = lookup("#errorMsg").query();
            f.setValue(in);
            f = lookup("#gender").query();
            verifyThat(f.getValue(), is(in));
            clickOn("#update");
            assertNotEquals(l.getText(), is(""));
        }
    }
    @Test
    public void DatePersonalDetails() {
        Label l = lookup("#errorMsg").query();
        ((DatePicker)lookup("#DOB").query()).setValue(LocalDate.parse("2100-08-02"));
        clickOn("#update");
        assertNotEquals(l.getText(), is(""));
        ((DatePicker)lookup("#DOB").query()).setValue(LocalDate.parse("1900-08-02"));
        clickOn("#update");
        assertNotEquals(l.getText(), is(""));
    }
}