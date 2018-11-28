package carRentalSystem.model;

/**
 * Class abstracting a registered user of type user
 */
public class RegisteredUser extends User{

    /**
     * Constructor for the class
     * Sets the name email and password for an admin
     * @param name name of the user
     * @param email email of the user
     * @param password password of the user
     */
    public RegisteredUser(int id,String name, String email,String omang, String password) {
        super(id, name, email, omang, password);
//        setAccountType("user");
    }




}
