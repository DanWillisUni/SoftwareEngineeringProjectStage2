package Controller;

import Model.GenericDatabaseController;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class RegistrationController extends GenericController{
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
    /**
     * validate everything
     * create Person object
     * add the person object to the database
     * @param event button push to register
     */
    @FXML
    protected void RegisterHandleSubmitButtonAction(ActionEvent event) {
        GenericDatabaseController db = new GenericDatabaseController();
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
            }
        } else {
            errorMsg.setText("Error: forename null");
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
                if (username.getText().toString().length()>=45){
                    errorMsg.setText("Error: username too long");
                    username.setText("");
                } else {
                    if(db.isStr(username.getText(),"user","username")){
                        errorMsg.setText("Error: username already in use");
                        username.setText("");
                    }
                }
            }else{
                errorMsg.setText("Error: username null");
            }
        }else{
            errorMsg.setText("Error: username null");
        }
        //email validation
        if (email.getText()!=null){
            if (!email.getText().equals("")){
                if (email.getText().toString().length()<60){
                    if (email.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                        if(db.isStr(email.getText(),"user","email")){
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
            }
        } else {
            errorMsg.setText("Error: email null");
        }
        //password validation
        if (password.getText()!=null){
            if (!password.getText().equals("")){
                if (password.getText().toString().length()>=20){
                    errorMsg.setText("Error: password too long");
                    password.setText("");
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
            User newUser = new User(db.genID("user","idUser"),forename.getText(),surname.getText(),username.getText(),email.getText(),password.getText(), Date.from(Instant.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()))),0, gender.getValue().toString().charAt(0),0);
            newUser.add();
            goToPage("../View/Login.fxml",event);
        }
    }
    /**
     * go to the login page
     * @param event go back to login button pushed
     */
    @FXML
    private void GoToLoginButtonAction (ActionEvent event) {
        goToPage("../View/Login.fxml",event);
    }
}
