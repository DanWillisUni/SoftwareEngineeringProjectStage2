package Model;

//java imports
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class GenericDatabaseController {
    private Connection connection;//connection to the database
    //used everywhere
    /**
     * Creates a connection to the server
     */
    public GenericDatabaseController() {
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
     * closes the connection to the local server
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

    //generic functions
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
        ArrayList<String> things = new ArrayList<>();
        String sql = "SELECT * FROM softwareengineering."+TableName+" WHERE "+ ColumnName+" LIKE ? order by " + ColumnName + " asc";
        try (
                PreparedStatement pst=connection.prepareStatement(sql);
        ){
            pst.setString(1, "%" + s + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                things.add(rs.getString(ColumnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return things;
    }
    /**
     * select all from the column
     * if any of them match str return true
     * if none match str return false
     * @param str element to search for
     * @param TableName the name of the table to look in
     * @param ColumnName the column to look in
     * @return if str is in the column
     */
    public boolean isStr(String str,String TableName,String ColumnName){
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
     * select the id column from TableName where ColumnName is equal to name
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
    public void remove(int id,String TableName,String ColumnName){
        try {
            final String query = "DELETE FROM softwareengineering"+TableName+" WHERE "+ ColumnName + "="+id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //login
    /**
     * Selects all info from personalinfo where the user id is the same as id
     * create new Person object from all the info
     * return new person
     * @param id the id of the user
     * @return User with idUser id
     */
    public User getAllPersonalInfo(int id) {
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.PersonalInfo where idUser = '"+id +"'");
        ){
            if(rs.next()){
                String firstName = rs.getString("forename");
                String lastName = rs.getString("surname");
                String Username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date DOB = rs.getDate("DOB");
                int height = Integer.parseInt(rs.getString("height"));
                char gender = rs.getString("gender").charAt(0);
                int weight = Integer.parseInt(rs.getString("weight"));
                User user = new User(id,firstName, lastName,Username, email,password,DOB,height,gender,weight);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * selects the password where the email matches
     * @param email email to match
     * @return password if there is any
     */
    public String getMatchingPassword(String email){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select password from softwareengineering.personalinfo Where email= '" + email + "'");
        ){
            if (rs.first()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //registration

    //add weight

    public boolean checkGoalMet(int id){
        boolean r = false;
        ArrayList<Integer> goalIDs = getGoalIDFromID(id);
        int cw = getCurrentWeight(id);
        for (int goalID:goalIDs){
            if (getGoalWeight(goalID) >= cw){
                r = true;
                DelGoalLink(id,goalID);
            }
        }
        return r;
    }
    /**
     * delete from goal link where the user and goal id are the same
     * @param id user id to delete
     * @param goalID goal id to delete
     */
    private void DelGoalLink(int id,int goalID){
        final String query = "Delete from softwareengineering.goallink Where idUser= "+ id + " And idGoalWeight = " + goalID;
        try (
                PreparedStatement pstmt = connection.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * get the ids of all the goals of a user
     * @param id user id
     * @return arraylist of all the ids of a user
     */
    private ArrayList<Integer> getGoalIDFromID(int id){
        ArrayList<Integer> r = new ArrayList<>();
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.goallink Where idUser= "+ id);
        ){
            while (rs.next()) {
                r.add(rs.getInt("idGoalWeight"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * get the target weight of a goal with the id
     * @param id the id of the goal
     * @return target weight
     */
    private int getGoalWeight(int id){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.goalweight Where idGoalWeight=" + id);
        ){
            if (rs.first()) {
                return rs.getInt("weightGoal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * gets the current weight of a user
     * select all weights in weight tracking and order by date
     * @param id users id
     * @return current weight of user
     */
    public int getCurrentWeight(int id){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.weighttracking Where idUser=" + id + " order by date desc");
        ){
            if (rs.first()) {
                return rs.getInt("weight");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //add exercise
    /**
     * add into exercise link
     * @param sessionID the session id
     * @param userID the user id
     */
    public void addExerciseLink(int sessionID,int userID){
        int id =genID("exerciselink","idLink");
        try {
            final String query = "Insert Into softwareengineering.exerciselink Values("+ id + ", " + userID  + " ,"+ sessionID + " ,?)";
            try (
                    PreparedStatement pstmt1 = connection.prepareStatement(query)
            ){
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                pstmt1.setDate(1, currentDate);
                pstmt1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Check if there is already a session
     * if it wasnt add it
     * @param duration duration of exercise session
     * @param sportID sport of the session
     * @param calburned calories burned during the session
     * @return the session id
     */
    public int addExerciseSession(BigDecimal duration,int sportID,int calburned){
        int id = getExerciseSessionID(duration,sportID,calburned);
        if (id == -1){
            id =genID("exercisesession","idExerciseSession");
            try {
                final String query = "Insert Into softwareengineering.exercisesession Values("+ id + ", '" + duration + "' ,"+ sportID+ " ,"+ calburned + ")";
                try (
                        PreparedStatement pstmt1 = connection.prepareStatement(query)
                ){
                    pstmt1.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
    /**
     * select the exercise session id
     * @param duration the duration to look for
     * @param sportID the sport id to look for
     * @param calburned the number of calories burned to look for
     * @return -1 if doesnt exist
     */
    private int getExerciseSessionID(BigDecimal duration,int sportID,int calburned){
        int r = -1;
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.exercisesession where idExerciseType = " + sportID+" AND durationMinutes = " +duration +" and caloriesBurned = "+calburned);
        ){
            if(rs.next()){
                r = Integer.parseInt(rs.getString("idExerciseSession"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * gets the number of calories burned per minute for an exercise
     * @param id exercise id
     * @return the number of calories burned per min
     */
    public int getCalsBurnedFromID(int id){
        int r = 0;
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.exercise where idExerciseType = '"+id+"'");
        ){
            if(rs.next()){
                r = Integer.parseInt(rs.getString("calsPerMinute"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    //add goal
    /**
     * checks to see if the goal exists
     * if not make it
     * create a goal link with the id and the goal id
     * @param id  user id
     * @param targetWeight the target for the goal
     * @param targetDate the target date
     */
    public void addGoal(int id,int targetWeight, Date targetDate){
        int idGoalWeight = selectGoal(targetWeight,new Date(),targetDate);
        if (idGoalWeight == -1){
            idGoalWeight = genID("goalweight","idGoalWeight");
            createGoal(idGoalWeight,targetWeight,new Date(),targetDate);
        }
        addGoalLink(id,idGoalWeight);
    }
    /**
     * select goal id where criteria is the same
     * @param targetWeight target weight
     * @param setDate the date the goal was set
     * @param targetDate the date to compete by
     * @return goal id if it exists
     */
    private int selectGoal(int targetWeight,Date setDate, Date targetDate){
        int r = -1;
        try {
            final String query = "SELECT * FROM softwareengineering.goalweight WHERE dateSet = ? AND targetDate = ? AND weightGoal = " + targetWeight;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(setDate.getTime()));
                pstmt.setDate(2, new java.sql.Date(targetDate.getTime()));
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    r = rs.getInt("idGoalWeight");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * add a goal link between the goal id and the user id
     * @param id user id
     * @param idGoalWeight goal id
     */
    private void addGoalLink(int id,int idGoalWeight){
        try {
            final String query = "Insert Into softwareengineering.goallink Values(" + id + ", " + idGoalWeight+")";
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * inserts the values into the goal table
     * @param id the goal id
     * @param targetWeight the target weight
     * @param set the set date
     * @param targetDate the target date
     */
    private void createGoal(int id,int targetWeight,Date set, Date targetDate){
        try {
            final String query = "Insert Into softwareengineering.goalweight Values("+ id +", " + targetWeight +", ?,?)";
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(set.getTime()));
                pstmt.setDate(2, new java.sql.Date(targetDate.getTime()));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //dashboard
    //cal consumed
    /**
     * get all the meals consumed by that user on that date
     * get the quantity of the meal
     * get the calorie count of each portion
     * mutiply them
     * sum them all
     * @param id user id
     * @param d date to get the calories consumed
     * @return the number of calories consumed by the user of the date
     */
    public int getCalConsumed(int id,Date d){
        int r = 0;
        ArrayList<Integer> idMeals = getMealsFromID(id,d);
        for (Integer idMeal:idMeals){
            int q = getQuantityFromMeal(idMeal);
            int cal = getCalCountFromFoodID(getFoodIDFromMeal(idMeal));
            r+=(q*cal);
        }
        return r;
    }
    /**
     * select from diet where date is d and userid is id
     * @param id user id
     * @param d date
     * @return the meal ids of that day by that user
     */
    private ArrayList<Integer> getMealsFromID(int id,Date d){
        ArrayList<Integer> i = new ArrayList<>();
        try {
            final String query = "SELECT * FROM softwareengineering.diet WHERE date = ? AND idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(d.getTime()));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    i.add(rs.getInt("idMeal"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }
    /**
     * get the quantity from the meal id
     * @param id meal id
     * @return quantity
     */
    private int getQuantityFromMeal(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.meal WHERE idMeal = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r = (rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * get the food id from the meal id
     * @param id meal id
     * @return food id
     */
    private int getFoodIDFromMeal(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.meal WHERE idMeal = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r = (rs.getInt("idFood"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * get the calorie count of 1 portion of food
     * @param id food id
     * @return cal count
     */
    private int getCalCountFromFoodID(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.foods WHERE idFood = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r = (rs.getInt("amountOfCalories"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    //get cal burned
    /**
     * calories burned on that date by that user
     * @param id user id
     * @param d date
     * @return calories burned
     */
    public int getCalBurned(int id,Date d){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.exerciselink WHERE date = ? AND idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(d.getTime()));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    r+=getCalBurnedFromSessionID(rs.getInt("idExerciseSession"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    /**
     * select calories burned where the exercise id is the id
     * @param id session id
     * @return calories burned
     */
    private int getCalBurnedFromSessionID(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.exercisesession WHERE idExerciseSession = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r=(rs.getInt("caloriesBurned"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    //get weight tracking
    /**
     * select weight from weighttracking where id is the id
     * @param id user id
     * @return arraylist of the weights of the user
     */
    public ArrayList<Integer> getWeightTrackingWeight(int id){
        ArrayList<Integer> w = new ArrayList<>();
        try {
            final String query = "SELECT * FROM softwareengineering.weighttracking WHERE idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    w.add(rs.getInt("weight"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return w;
    }
    /**
     * select date from weighttracking for the user
     * @param id user id
     * @return arraylist of dates
     */
    public ArrayList<java.util.Date> getWeightTrackingDate(int id){
        ArrayList<java.util.Date> d = new ArrayList<>();
        try {
            final String query = "SELECT * FROM softwareengineering.weighttracking WHERE idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    d.add(new java.util.Date(rs.getDate("date").getTime()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
    /**
     * gets the goal closest to the current date
     * @param userid id if the user
     * @return
     */
    public int getClosestGoal(int userid){
        ArrayList<Integer> goalIDs = getAllGoals(userid);
        if (goalIDs.size()>0){
            String str = "select * from softwareengineering.goalweight where";
            boolean first = true;
            for(int i:goalIDs){
                if (first == true){
                    str += " idGoalWeight= " + i;
                    first = false;
                } else {
                    str += " OR idGoalWeight= " + i;
                }
            }
            str += " order by targetDate asc";
            try (
                    PreparedStatement pstmt = connection.prepareStatement(str)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.first()){
                    return (rs.getInt("weightGoal"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }
    /**
     * gets all the goal ids that are linked to the user
     * @param userid user id
     * @return arraylist of al the id of the goal
     */
    private ArrayList<Integer> getAllGoals(int userid){
        ArrayList<Integer> w = new ArrayList<>();
        try {
            final String query = "SELECT * FROM softwareengineering.goallink WHERE idUser = " + userid;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    w.add(rs.getInt("idGoalWeight"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return w;
    }
    /**
     * removes all the overdue goals
     * gets all the goals of that user
     * checks the date to see if it is in the past
     * @param idUser user id
     * @return if one was removed
     */
    public boolean removeOverdueGoals(int idUser){
        boolean r = false;
        ArrayList<Integer> allGoals = getAllGoals(idUser);
        for(int goalid:allGoals){
            Date d = getDateOfGoal(goalid);
            if (d.getTime() < new Date().getTime()){
                DelGoalLink(idUser,goalid);
                r=true;
            }
        }
        return r;
    }
    /**
     * gets the target date of the goal
     * @param id goal id
     * @return target date
     */
    private Date getDateOfGoal(int id){
        try {
            final String query = "SELECT * FROM softwareengineering.goalweight WHERE idGoalWeight = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    return new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("targetDate"));
                }
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}