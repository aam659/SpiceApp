package com.example.spiceapp.FirebaseObjects;


/**
 * Handler class for whenever you want to pull a User object from the Firebase database.
 * Does not contain the moods or current preference
 *
 * Author Ryan Simpson
 */
public class User {

    private String email;
    private String fName;
    private String lName;
    private String phoneNumber;

    public User(String email, String fName, String lName, String phoneNumber){
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.phoneNumber = phoneNumber;
    }

    public User(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
