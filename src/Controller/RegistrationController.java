package Controller;

import Model.GenericDatabaseController;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class RegistrationController extends GenericDatabaseController {
    private Connection c;
    @FXML private TextField forename;
    @FXML private TextField surname;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField password2;
    @FXML private DatePicker DOB;
    @FXML private ComboBox gender;
    @FXML private Label errorMsg;

    public void setConnection(Connection c){
        this.c=c;
    }
    /**
     * Validate everything as all the fields are required
     * If any of the fields are wrong display
     * Make a new User obj if all the fields are valid
     * Add the user to the database
     * Go to the login page
     * @param event button push to register
     */
    @FXML
    protected void RegisterHandleSubmitButtonAction(ActionEvent event) {
        errorMsg.setText("");
        //validation forename
        if (forename.getText()!=null){
            if (!forename.getText().equals("")){
                if(forename.getText().matches("^([a-z]|[A-Z])+$")){
                    if (forename.getText().toString().length()>=45){
                        errorMsg.setText("Error: forename too long");
                        forename.setText("");
                    }
                } else {
                    errorMsg.setText("Error: forename non alphabetical");
                    forename.setText("");
                }
            } else {
                errorMsg.setText("Error: forename null");
                forename.setText("");
            }
        } else {
            errorMsg.setText("Error: forename null");
            forename.setText("");
        }
        //surename validation
        if (surname.getText()!=null){
            if (!surname.getText().equals("")){
                if(surname.getText().matches("^([a-z]|[A-Z])+$")){
                    if (surname.getText().toString().length()>=45){
                        errorMsg.setText("Error: surname too long");
                        surname.setText("");
                    }
                } else {
                    errorMsg.setText("Error: surname non alphabetical");
                    surname.setText("");
                }
            } else {
                errorMsg.setText("Error: surname null");
            }
        } else {
            errorMsg.setText("Error: surname null");
        }
        //username validation
        if (username.getText()!=null){
            if (!username.getText().equals("")){
                if (username.getText().matches("^([a-z]|[A-Z]|[d])+$")){
                    if (username.getText().toString().length()>=45){
                        errorMsg.setText("Error: username too long");
                        username.setText("");
                    } else {
                        if(GenericDatabaseController.isInTable(username.getText(),"user","username",c)){
                            errorMsg.setText("Error: username already in use");
                            username.setText("");
                        }
                    }
                } else {
                    errorMsg.setText("Error: username contains invalid character");
                    username.setText("");
                }
            }else{
                errorMsg.setText("Error: username null");
                username.setText("");
            }
        }else{
            errorMsg.setText("Error: username null");
            username.setText("");
        }
        //email validation
        if (email.getText()!=null){
            if (!email.getText().equals("")){
                if (email.getText().toString().length()<60){
                    if (email.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                        if(GenericDatabaseController.isInTable(email.getText(),"user","email",c)){
                            errorMsg.setText("Error: email already in use");
                            email.setText("");
                        }
                    } else {
                        errorMsg.setText("Error: Not a valid email");
                        email.setText("");
                    }
                } else {
                    errorMsg.setText("Error: email too long");
                    email.setText("");
                }
            }else {
                errorMsg.setText("Error: email null");
                email.setText("");
            }
        } else {
            errorMsg.setText("Error: email null");
            email.setText("");
        }
        //password validation
        if (password.getText()!=null){
            if (!password.getText().equals("")){
                if (password.getText().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
                    if (password.getText().toString().length()>=20){
                        errorMsg.setText("Error: password too long");
                        password.setText("");
                        password2.setText("");
                    }
                } else {
                    errorMsg.setText("Minimum eight characters, at least one letter and one number");
                    password.setText("");
                    password2.setText("");
                }
            } else {
                errorMsg.setText("Error: password null");
            }
        }else{
            errorMsg.setText("Error: password null");
        }
        //Date of birth validation
        if(DOB.getValue()!=null){
            Long d = Date.from(Instant.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()))).getTime();
            Long c = new Date().getTime();
            if (c>d){
                if (c - 3153600000000L > d){
                    errorMsg.setText("Error: date to long ago");
                }
            } else {
                errorMsg.setText("Error: date in the future");
            }
        }else {
            errorMsg.setText("Error: Date not selected");
        }
        //validation of gender
        if (gender.getValue()==null) {
            errorMsg.setText("Error: gender not selected");
        }else if(gender.getValue().toString().equals("")){
            errorMsg.setText("Error: gender not typed in");
        } else {
            if(!gender.getValue().toString().equals("Male")&&!gender.getValue().toString().equals("Female")&&!gender.getValue().toString().equals("Other")){
                errorMsg.setText("Error: not valid gender");
                gender.setValue("");
            }
        }
        //password matching validation
        if (!password.getText().equals(password2.getText())) {
            errorMsg.setText("Passwords do not match");
            password2.setText("");
        }
        if (errorMsg.getText().equals("")){
            User newUser = new User(GenericDatabaseController.genID("user","idUser",c),forename.getText(),surname.getText(),username.getText(),email.getText(),password.getText(), Date.from(Instant.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()))),0, gender.getValue().toString().charAt(0),0);//create a new user
            newUser.add(c);//put the user in the database
            GenericController.goToLogin(c,event);
        }
    }
    /**
     * Go to the login page
     * @param event go back to login button pushed
     */
    @FXML
    private void GoToLoginButtonAction (ActionEvent event) {
        GenericController.goToLogin(c,event);
    }
}
