package Controller;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class GenericController {
    private Connection connection;//connection to the database
    /**
     * Creates a connection to the server
     */
    public GenericController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/softwareengineering", "root","rootroot");//my username and password
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Get the connection
     * @return the controller
     */
    public Connection getConnection(){
        return connection;
    }

    /**
     * Generates an id that hasnt been used before
     * Selects all the ids in that column
     * Sorts them
     * gets the largest one and adds one to it
     * @param TableName the name of the table to generate the id in
     * @param ColumName the column name of the id
     * @return a unique id
     */
    public static int genID(String TableName, String ColumName, Connection c){
        ArrayList<Integer> ids = new ArrayList<>();
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering." + TableName);
        ){
            while (rs.next()) {
                ids.add(rs.getInt(ColumName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ids.size()>0){
            Collections.sort(ids);
            return ids.get(ids.size()-1) +1 ;
        } else {
            return 0;
        }
    }
    /**
     * Select all ColumnName where they are like %s%
     * @param s string to search
     * @param TableName the name of the table to search in
     * @param ColumnName the name of the column to search in
     * @return an array list of all the elements in ColumnName like s
     */
    public static ArrayList<String> getAllLike(String s,String TableName,String ColumnName, Connection c){
        ArrayList<String> r = new ArrayList<>();
        String sql = "SELECT * FROM softwareengineering."+TableName+" WHERE "+ ColumnName+" LIKE ? order by " + ColumnName + " asc";
        try (
                PreparedStatement pst=c.prepareStatement(sql);
        ){
            pst.setString(1, "%" + s + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                r.add(rs.getString(ColumnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * Select all from the column
     * If any of them match str return true
     * If none match str return false
     * @param str element to search for
     * @param TableName the name of the table to look in
     * @param ColumnName the column to look in
     * @return if str is in the column
     */
    public static boolean isInTable(String str, String TableName, String ColumnName,Connection c){
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering." + TableName);
        ){
            while(rs.next()){
                String s = rs.getString(ColumnName);
                if(s.equals(str)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Select the id column from TableName where ColumnName is equal to name
     * @param name the name to match
     * @param TableName the table name
     * @param ColumnName the name column
     * @param ColumnIDName the id column
     * @return
     */
    public static int getIDFromName(String name, String TableName, String ColumnName, String ColumnIDName,Connection c){
        int r = -1;
        try (
                Statement stmnt = c.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering."+ TableName +" where " + ColumnName + " = '"+name +"'");
        ){
            if(rs.next()){
                r = Integer.parseInt(rs.getString(ColumnIDName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * Remove the row in the table where the column = id
     * @param id to match id
     * @param TableName table to remove from
     * @param ColumnName column of the id
     */
    public static void remove(int id, String TableName, String ColumnName,Connection c){
        try {
            final String query = "DELETE FROM softwareengineering."+TableName+" WHERE "+ ColumnName + "="+id;
            try (
                    PreparedStatement pstmt = c.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //non database methods

    /**
     * Goes to the dashboard and sets the user
     * @param User user to set it to
     * @param event go to dashboard button pushed
     */
    public static void goToDash(User User, Connection c, ActionEvent event){
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/Dashboard.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        DashboardController controller = loader.<DashboardController>getController();
        controller.setUser(User,c);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void goToLogin(Connection c, ActionEvent event){
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/Login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        LoginController controller = loader.<LoginController>getController();
        controller.setConnection(c);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void goToRegistration(Connection c, ActionEvent event){
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource("../View/Registration.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        RegistrationController controller = loader.<RegistrationController>getController();
        controller.setConnection(c);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
}
