package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class User {
    int id;
    String forename;
    String surname;
    String username;
    String email;
    String password;
    Date DOB;
    int height;
    char gender;
    int weight;
    /**
     * sets all the parameters to the variables of the person
     * @param id id of the person
     * @param forename forename of the user
     * @param surname surname of the user
     * @param username username of the user
     * @param email email of the user
     * @param password password of the user
     * @param DOB date of birth of the user
     * @param height height of the user
     * @param gender gender of the user
     */
    public User(int id, String forename, String surname, String username, String email, String password, Date DOB, int height, char gender, int weight){
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
        this.gender = gender;
        this.weight=weight;
    }
    /**
     * gets the id
     * @return id of user
     */
    public int getId(){
        return id;
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
    public int getHeight(){
        return height;
    }
    /**
     * get the gender of the user
     * @return gender of the user
     */
    public char getGender(){return gender;}
    /**
     * get the weight of the user
     * @return weight of the user
     */
    public int getWeight(){
        return weight;
    }

    public void add(){
        GenericDatabaseController db = new GenericDatabaseController();
        try {
            final String query = "Insert Into softwareengineering.user Values("+ getId() + ", '" + getForename() + "', '" + getSurname()+ "', '" + getEmail()+ "', '" + getUsername()+ "', '" + getPassword()+ "', '"+ new java.sql.Date(getDOB().getTime()) +"' , " + getHeight()+ ", '" + getGender() + "', " + getWeight() + " )";
            try (
                    PreparedStatement pstmt = db.getConnection().prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(){
        GenericDatabaseController db = new GenericDatabaseController();
        final String query = "UPDATE softwareengineering.User SET forename = "+getForename()+" surname = "+ getSurname()+" email = "+ getEmail()+" username = "+ getUsername()+" password = "+ getPassword()+" DOB = "+ new java.sql.Date(getDOB().getTime())+" height = "+ getHeight()+" gender = "+ getGender()+" weight = "+ getWeight()+" Where idUser= "+ getId();
        try (
                PreparedStatement pstmt = db.getConnection().prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
