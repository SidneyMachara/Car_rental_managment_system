package carRentalSystem;

import carRentalSystem.model.Car;
import carRentalSystem.model.RegisteredUser;
import carRentalSystem.model.AdminUser;
import carRentalSystem.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import carRentalSystem.model.Car;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javafx.scene.control.Alert;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Logger;

//import org.mindrot.jbcrypt.BCrypt;

/**
 * Class that connects to the database.
 */
public class Database_Connect {
    private Connection conn;
    private Statement stmt;
    private MessageDigest md;



    /**
     * Class constructer specifying which database service to connect to.
     */
    public Database_Connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db?autoReconnect=true","root", "");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * Method executes an sql statment to insert something into the dtabase
     * @param sql the insert sql statement to be executed
     */
    private void insertStmt(String sql){
        stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method exectutes sql statements to return some result.
     * Takes a statement and queries the database.
     * @param sql   the sql statement to be exececuted
     * @return  Returns a resultset containing the rows and columns specified in the sql query
     */
    private ResultSet executeStmt(String sql){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean test = rs.first();
            return rs;
            //return rs.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * The method used to log a registered user in.
     * If the User exists on the database his/her information will be retrieved
     * and returned as a User object.

     * @param password  password of the user entered by the user
     * @return  returns a user of type RegisteredUser containing all information
     *  of that user
     * @throws SQLException throws an exception if the sql statement is incorrect,
     * or if the user or table does not exist
     */
    public RegisteredUser loginCustomer(String email, String password) throws SQLException, NoSuchAlgorithmException {
        String password_hashed = cryptWithMD5(password);

        String sql = "SELECT * FROM customer_table WHERE email=\""+email+"\" AND password=\""+password_hashed+"\"";

        RegisteredUser user= null;
        ResultSet rs =  executeStmt(sql);
        rs.beforeFirst();
        if(rs.next()){
            if (password_hashed.equals(rs.getString("password")) ) {
                user = new RegisteredUser(rs.getString("name"), rs.getString("email"), rs.getString("omang"), password_hashed);
                Main.currentUser = user;
                return user;
            }
            else
                return null;

        }
        return null;

    }


    /**
     * The method used to log a registered user in.
     * If the User exists on the database his/her information will be retrieved
     * and returned as a User object.

     * @param password  password of the user entered by the user
     * @return  returns a user of type RegisteredUser containing all information
     *  of that user
     * @throws SQLException throws an exception if the sql statement is incorrect,
     * or if the user or table does not exist
     */
    public AdminUser loginAdmin(String email, String password) throws SQLException, NoSuchAlgorithmException {

        String password_hashed = cryptWithMD5(password);
        String sql = "SELECT * FROM admin_table WHERE email=\""+email+"\" AND password=\""+password_hashed+"\"";

        AdminUser user= null;
        ResultSet rs =  executeStmt(sql);
        rs.beforeFirst();
        if(rs.next()){
            if (password_hashed.equals(rs.getString("password")) ) {
                user = new AdminUser("admin", rs.getString("email"), password_hashed);
                Main.currentUser = user;
                return user;
            }
            else
                return null;

        }
        return null;

    }




    /**
     * The method enters a users details into the database.

     * @param email  the users email address
     * @param password  the users chosen password
     */
    public void register(String name, String email, String surname, String omang, String password) throws NoSuchAlgorithmException {
        String password_hash = cryptWithMD5(password);


        String sql = "INSERT INTO customer_table ( name, surname, omang , email, password) " +
                "VALUES (\""+name+"\", \""+surname+"\", \""+omang+"\", \""+email+"\",\""+password_hash+"\")";

        insertStmt(sql);
    }





    /**
     * Queries the database and returns a list of all buses from the database.
     * The list is used to populate tables in other classes and contains all bus
     * information.
     * @return an observable list of buses of type Bus
     */
    public ObservableList<Car> getCarsFromDB(){
        String sql = "SELECT * FROM car_table";
        ObservableList<Car> cars = FXCollections.observableArrayList();
        ResultSet rs = executeStmt(sql);
        try{
            rs.beforeFirst();
            while(rs.next()){
                cars.add(new Car(rs.getString("car_name"),rs.getDouble("price"),rs.getString("status"),rs.getInt("car_id"),rs.getString("image")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    /**
     * Queries the database and returns a list of all buses from the database.
     * The list is used to populate tables in other classes and contains all bus
     * information.
     * @return an observable list of buses of type Bus
     */
    public ObservableList<RegisteredUser> getClientsFromDB(){
        String sql = "SELECT * FROM customer_table";
        ObservableList<RegisteredUser> clients = FXCollections.observableArrayList();
        ResultSet rs = executeStmt(sql);
        try{
            rs.beforeFirst();
            while(rs.next()){
                clients.add(new RegisteredUser(rs.getString("name"), rs.getString("email"), rs.getString("omang"), rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    /**
     * Queries the database and returns a list of all buses from the database.
     * The list is used to populate tables in other classes and contains all bus
     * information.
     * @return an observable list of buses of type Bus
     */
    public ResultSet getBookedCarsFromDB(){
        String sql = "SELECT * FROM booked WHERE customer = \""+Main.currentUser.getEmail()+"\"";
        ResultSet rs = executeStmt(sql);
        return rs;
    }

    /**
     * Queries the database and returns a list of all buses from the database.
     * The list is used to populate tables in other classes and contains all bus
     * information.
     * @return an observable list of buses of type Bus
     */
    public void bookCar(int car_id ,String car_name,double price, LocalDate start_date, LocalDate return_date,String image){
        String sql = "INSERT INTO booked ( car_id , car_name, price, customer,book_from, book_until, image) " +
                "VALUES (\""+car_id+"\", \""+car_name+"\", \""+price+"\", \""+Main.currentUser.getEmail()+"\" , \""+start_date+"\" , \""+return_date+"\", \""+image+"\")";

        insertStmt(sql);
//        update car status in database to booked
         sql = "UPDATE car_table SET status = 'Booked' WHERE car_id = \""+car_id+"\"";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db","root", "");


        } catch (Exception e) {
            e.printStackTrace();
        }


        insertStmt(sql);

    }

    public void returnCar(String car_id){
        String sql = "UPDATE car_table SET status = 'available' WHERE car_id = \""+car_id+"\"";
        insertStmt(sql);

        sql = "DELETE FROM booked WHERE car_id = \""+car_id+"\"";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_db","root", "");


        } catch (Exception e) {
            e.printStackTrace();
        }

        insertStmt(sql);
    }


    /**
     * Queries the database and returns a list of all buses from the database.
     * The list is used to populate tables in other classes and contains all bus
     * information.
     * @return an observable list of buses of type Bus
     */
    public void addCartoDB(String car_name ,String car_model,double price, String image){
        String sql = "INSERT INTO car_table ( car_name, car_model,status, price, image) " +
                "VALUES (\""+car_name+"\", \""+car_model+"\",'available', \""+price+"\", \""+image+"\")";

        insertStmt(sql);
    }

    public boolean alreadyBooked()throws SQLException{
        String sql = "SELECT * FROM booked WHERE customer = \""+Main.currentUser.getEmail()+"\"";
        ResultSet rs = executeStmt(sql);
        rs.beforeFirst();
        if(!rs.isBeforeFirst()){
            return false;
        }else{
            return true;
        }
    }

    public void deleteCarFromDB(int id){
        String sql = "DELETE FROM car_table WHERE car_id = \""+id+"\"";
        stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getBorrowedCarForAdmin(String user_email){
        Statement stmt = null;
        String sql = "SELECT * FROM booked WHERE customer = \""+user_email+"\"";
        ResultSet rs = executeStmt(sql);

        return rs;
    }

    public void showAlert(String head,String body){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(head);
        alert.setHeaderText(null);
        alert.setContentText(body);
        alert.showAndWait();
    }

    public  String cryptWithMD5(String pass) throws NoSuchAlgorithmException {

            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();


    }






}
