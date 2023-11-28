package com.yplatform.model;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String username;
    private String salt;

    // Password constraints
    // - At least 8 characters
    // - At least one uppercase letter
    // - At least one lowercase letter
    // - At least one digit
    // - At least one special character
    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password format")
    private String password;

    // Constructors

    public User() {
    }

    public User(String email, String firstName, String lastName, Date dateOfBirth, String username, String salt, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.salt = salt;
        this.password = password;
    }

    public User(String email, String firstName, String lastName, Date dateOfBirth, String username, String salt, String hashedPassword) {
    }

    public User(String firstName, String lastName, Date dateOfBirth, String username, String email, String salt, String hashedPassword) {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt(){
        return salt;
    }
    public void setSalt(String salt){
        this.salt = salt;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Additional methods if needed

}