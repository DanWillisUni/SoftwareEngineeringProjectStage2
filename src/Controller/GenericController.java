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
     * Closes the connection to the local server
     */
    public void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
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
    public int genID(String TableName,String ColumName){
        ArrayList<Integer> ids = new ArrayList<>();
        try (
                Statement stmnt = connection.createStatement();
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
    public ArrayList<String> getAllLike(String s,String TableName,String ColumnName){
        ArrayList<String> r = new ArrayList<>();
        String sql = "SELECT * FROM softwareengineering."+TableName+" WHERE "+ ColumnName+" LIKE ? order by " + ColumnName + " asc";
        try (
                PreparedStatement pst=connection.prepareStatement(sql);
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
    public boolean isInTable(String str, String TableName, String ColumnName){
        try (
                Statement stmnt = connection.createStatement();
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
    public int getIDFromName(String name,String TableName,String ColumnName,String ColumnIDName){
        int r = -1;
        try (
                Statement stmnt = connection.createStatement();
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
    public void remove(int id,String TableName,String ColumnName){
        try {
            final String query = "DELETE FROM softwareengineering."+TableName+" WHERE "+ ColumnName + "="+id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //non database methods
    /**
     * Goes to a page with no setting the user
     * @param page page to load path
     * @param event button pushed
     */
    public static void goToPage(String page, ActionEvent event){
        FXMLLoader loader = new FXMLLoader(GenericController.class.getResource(page));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * Goes to the dashboard and sets the user
     * @param User user to set it to
     * @param event go to dashboard button pushed
     */
    public static void goToDash(User User, ActionEvent event){
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
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
}
