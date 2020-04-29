package Controller;

import Model.GenericDatabaseController;
import Model.User;
import Model.WeightGoal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import static Controller.GenericController.goToDash;

public class PersonalDetailsController {
    private Model.User User;
    @FXML private TextField forename;
    @FXML private TextField surname;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField password2;
    @FXML private DatePicker DOB;
    @FXML private ComboBox gender;
    @FXML private TextField height;
    @FXML private Label errorMsg;
    @FXML private Label currentForename;
    @FXML private Label currentSurname;
    @FXML private Label currentUsername;
    @FXML private Label currentEmail;
    @FXML private Label currentDOB;
    @FXML private Label currentGender;
    @FXML private Label currentHeight;
    /**
     * sets the user to the user that is logged in
     * @param User Person object logged in
     */
    public void setUser(Model.User User){
        this.User = User;
    }
    /**
     * Sets up the display
     * gets any goals that have expired or been competed and removes them with a message
     * gets bmi
     * gets the calories of that day both consumed and burned and works out calories left
     * gets the weight of the upcoming goal
     * gets the weights and dates
     * only displays the chart of the last 2 weeks to track weight
     */
    public void setUpDisplay() {
        currentForename.setText(User.getForename());
        currentSurname.setText(User.getSurname());
        currentUsername.setText(User.getUsername());
        currentEmail.setText(User.getEmail());
        currentDOB.setText(User.getDOB().toString());
        currentGender.setText(Character.toString(User.getGender()));
        if (User.getHeight()>0){
            currentHeight.setText(Integer.toString(User.getHeight()));
        }
    }
    @FXML
    private void SaveUser (ActionEvent event) {
        User copy = User;
        GenericDatabaseController db = new GenericDatabaseController();
        errorMsg.setText("");
        //validation forename
        if (forename.getText()!=null){
            if (!forename.getText().equals("")){
                if(forename.getText().matches("^([a-z]|[A-Z])+$")){
                    if (forename.getText().toString().length()>=45){
                        errorMsg.setText("Error: forename too long");
                        forename.setText("");
                    } else {
                        User.setForename(forename.getText());
                    }
                } else {
                    errorMsg.setText("Error: forename non alphabetical");
                    forename.setText("");
                }
            }
        }
        //surename validation
        if (surname.getText()!=null){
            if (!surname.getText().equals("")){
                if(surname.getText().matches("^([a-z]|[A-Z])+$")){
                    if (surname.getText().toString().length()>=45){
                        errorMsg.setText("Error: surname too long");
                        surname.setText("");
                    } else {
                        User.setSurname(surname.getText());
                    }
                } else {
                    errorMsg.setText("Error: surname non alphabetical");
                    surname.setText("");
                }
            }
        }
        //username validation
        if (username.getText()!=null){
            if (!username.getText().equals("")){
                if (username.getText().toString().length()>=45){
                    errorMsg.setText("Error: username too long");
                    username.setText("");
                } else {
                    if(db.isInTable(username.getText(),"user","username")){
                        errorMsg.setText("Error: username already in use");
                        username.setText("");
                    } else {
                        User.setUsername(username.getText());
                    }
                }
            }
        }
        //email validation
        if (email.getText()!=null){
            if (!email.getText().equals("")){
                if (email.getText().toString().length()<60){
                    if (email.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                        if(db.isInTable(email.getText(),"user","email")){
                            errorMsg.setText("Error: email already in use");
                            email.setText("");
                        } else {
                            User.setEmail(email.getText());
                        }
                    } else {
                        errorMsg.setText("Error: Not a valid email");
                        email.setText("");
                    }
                } else {
                    errorMsg.setText("Error: email too long");
                    email.setText("");
                }
            }
        }
        //password validation
        if (password.getText()!=null){
            if (!password.getText().equals("")){
                if (password.getText().equals(password2.getText())) {
                    if (password.getText().toString().length() >= 20) {
                        errorMsg.setText("Error: password too long");
                        password.setText("");
                    } else {
                        User.setPassword(password.getText());
                    }
                }  else {
                    errorMsg.setText("Error: passwords not equal");
                    password.setText("");
                }
            }
        }
        //Date of birth validation
        if(DOB.getValue()!=null){
            Long d = Date.from(Instant.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()))).getTime();
            Long c = new Date().getTime();
            if (c>d){
                if (c - 3153600000000L > d){
                    errorMsg.setText("Error: date to long ago");
                } else {
                    User.setDOB(new Date(d));
                }
            } else {
                errorMsg.setText("Error: date in the future");
            }
        }
        //validation of gender
        if (gender.getValue()!=null) {
            if(!gender.getValue().toString().equals("")) {
                if(!gender.getValue().toString().equals("Male")&&!gender.getValue().toString().equals("Female")&&!gender.getValue().toString().equals("Other")){
                    errorMsg.setText("Error: not valid gender");
                    gender.setValue("");
                } else {
                    User.setGender(gender.getValue().toString().charAt(0));
                }
            }
        }
        //height validation
        if (height.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            int i = Integer.parseInt(height.getText());
            if (i>250){
                errorMsg.setText("Error: height greater than 250");
                height.setText("");
            } else {
                User.setHeight(i);
            }
        } else {
            errorMsg.setText("Error: height not positive");
            height.setText("");
        }



        if (errorMsg.getText().equals("")){
            User.update();
            goToDash(User,event);
        } else {
            User = copy;
        }
    }
    /**
     * go to the dashboard
     * @param event back button pressed
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event){
        goToDash(User,event);
    }

}