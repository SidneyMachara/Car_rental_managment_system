package carRentalSystem.model;

/**
 * Class Describes an admin of type User
 */
public class AdminUser extends User {

    /**
     * Constructor for the class
     * Sets the name email and password for an admin
     * @param name admin's name
     * @param email admin's email
     * @param password admin's password
     */
    public AdminUser(String name, String email, String password) {
        super(name, email, password);
//        setAccountType("admin");
    }



}
