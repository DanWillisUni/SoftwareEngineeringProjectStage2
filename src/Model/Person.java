package Model;

import java.math.BigDecimal;
import java.util.Date;

public class Person {
    int ID;
    String forename;
    String surname;
    String username;
    String email;
    String password;
    Date DOB;
    BigDecimal height;
    char gender;
    /**
     * sets all the parameters to the variables of the person
     * @param ID id of the person
     * @param forename forename of the user
     * @param surname surname of the user
     * @param username username of the user
     * @param email email of the user
     * @param password password of the user
     * @param DOB date of birth of the user
     * @param height height of the user
     * @param gender gender of the user
     */
    public Person(int ID, String forename, String surname, String username, String email, String password, Date DOB, BigDecimal height,char gender){
        this.ID = ID;
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
        this.gender = gender;
    }
    /**
     * gets the id
     * @return id of user
     */
    public int getID(){
        return ID;
    }
    /**
     * gets the forename
     * @return forename of the user
     */
    public String getForename(){
        return forename;
    }
    /**
     * gets the surname of the user
     * @return surname of the user
     */
    public String getSurname(){
        return surname;
    }
    /**
     * gets the username of the user
     * @return username of the user
     */
    public String getUsername(){
        return username;
    }
    /**
     * gets email
     * @return email of the user
     */
    public String getEmail(){
        return email;
    }
    /**
     * gets the password
     * @return password of the user
     */
    public String getPassword(){
        return password;
    }
    /**
     * get the DOB of the user
     * @return date of birth of the user
     */
    public Date getDOB(){
        return DOB;
    }
    /**
     * get the height of the user
     * @return height of the user
     */
    public BigDecimal getHeight(){
        return height;
    }
    /**
     * get the gender of the user
     * @return gender of the user
     */
    public char getGender(){return gender;}
}
