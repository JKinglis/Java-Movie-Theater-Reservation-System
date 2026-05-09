package FinalProject;


//Stores customer or DBA account information.


import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // login information
    protected String username;
    protected String password;

    // role type (CUSTOMER or DBA)
    protected Role role;

    // personal information
    protected String firstName;
    protected String lastName;
    protected String address;
    protected String email;
    protected Long phone;

    // empty constructor
    public User() {

        this.username = null;
        this.password = null;
    }

    // full constructor
    public User(String username,
                String password,
                Role role,
                String firstName,
                String lastName,
                String address,
                String email,
                Long phone) {

        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    // username getter
    public String getUsername() {
        return username;
    }

    // username setter
    public void setUsername(String username) {
        this.username = username;
    }

    // password getter
    public String getPassword() {
        return password;
    }

    // password setter
    public void setPassword(String password) {
        this.password = password;
    }

    // role getter
    public Role getRole() {
        return role;
    }

    // role setter
    public void setRole(Role role) {
        this.role = role;
    }

    // first name getter
    public String getFirstName() {
        return firstName;
    }

    // first name setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // last name getter
    public String getLastName() {
        return lastName;
    }

    // last name setter
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // address getter
    public String getAddress() {
        return address;
    }

    // address setter
    public void setAddress(String address) {
        this.address = address;
    }

    // email getter
    public String getEmail() {
        return email;
    }

    // email setter
    public void setEmail(String email) {
        this.email = email;
    }

    // phone getter
    public Long getPhone() {
        return phone;
    }

    // phone setter
    public void setPhone(Long phone) {
        this.phone = phone;
    }

    // debug print
    @Override
    public String toString() {

        return "User [username=" + username
                + ", role=" + role
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", address=" + address
                + ", email=" + email
                + ", phone=" + phone + "]";
    }
}