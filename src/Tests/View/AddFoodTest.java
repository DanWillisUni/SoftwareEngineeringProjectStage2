package View;

import Controller.AddExerciseSessionController;
import Controller.AddFoodController;
import Controller.GenericController;
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

public class AddFoodTest extends ApplicationTest {
    static String[] incorrectNumberInputs;
    static String[] incorrectStringInputs;
    static Connection c;
    static User u;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/AddFood.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            u = new User(4, "Dan", "Willis", "Dw", "d@gmail.com", "miN+0R4PrCGXGexOhZk4SA==", new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2000"), 193, 'M', 93.4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        stage.setTitle("Health Tracker");
        stage.setScene(new Scene(root));
        AddFoodController controller = loader.<AddFoodController>getController();
        controller.setUser(u, c);
        controller.setUpDisplay();
        stage.show();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        GenericDatabaseController genericController = new GenericDatabaseController();
        c = genericController.getConnection();
        incorrectNumberInputs = new String[]{
                "0",
                "1/2",
                "1E2",
                "1E02",
                "1E+02",
                "-1",
                "-1.00",
                "-1/2",
                "-1E2",
                "-1E02",
                "-1E+02",
                "1/0",
                "0/0",
                "-2147483648/-1",
                "-9223372036854775808/-1",
                "-0",
                "-0.0",
                "+0",
                "+0.0",
                "0.00",
                "0..0",
                ".",
                "0.0.0",
                "0,00",
                "0,,0",
                ",",
                "0,0,0",
                "0.0/0",
                "1.0/0.0",
                "0.0/0.0",
                "1,0/0,0",
                "0,0/0,0",
                "--1",
                "-",
                "-.",
                "-,",
                "999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999",
                "NaN",
                "Infinity",
                "-Infinity",
                "INF",
                "1#INF",
                "-1#IND",
                "1#QNAN",
                "1#SNAN",
                "1#IND",
                "0x0",
                "0xffffffff",
                "0xffffffffffffffff",
                "0xabad1dea",
                "123456789012345678901234567890123456789",
                "1,000.00",
                "1 000.00",
                "1'000.00",
                "1,000,000.00",
                "1 000 000.00",
                "1'000'000.00",
                "1.000,00",
                "1 000,00",
                "1'000,00",
                "1.000.000,00",
                "1 000 000,00",
                "1'000'000,00",
                "01000",
                "08",
                "09",
                "2.2250738585072011e-308"
        };
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
    }

    @Before
    public void each() {
        ((ComboBox) lookup("#Foods").query()).setValue("apple");
        ((TextField) lookup("#quantity").query()).setText("2");
        ((ComboBox) lookup("#MealType").query()).setValue("Snack");
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
    @Test
    public void FoodAddFood() {
        for (String in:incorrectStringInputs){
            Label l = lookup("#errorMsg").query();
            ((ComboBox)lookup("#Foods").query()).setValue(in);
            clickOn("#add");
            assertNotEquals(l.getText(),is(""));
        }
    }
    @Test
    public void MealTypeAddFood() {
        for (String in:incorrectStringInputs){
            Label l = lookup("#errorMsg").query();
            ((ComboBox)lookup("#MealType").query()).setValue(in);
            clickOn("#add");
            assertNotEquals(l.getText(),is(""));
        }
    }
    @Test
    public void QuantityAddFood() {
        ((TextField)lookup("#quantity").query()).setText("");
        for (String in:incorrectNumberInputs){
            Label l = lookup("#errorMsg").query();
            clickOn("#quantity");
            write(in);
            clickOn("#add");
            assertNotEquals(l.getText(),is(""));
        }
    }
}
