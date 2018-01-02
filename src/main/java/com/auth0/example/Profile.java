package com.auth0.example;

/**
 * Profile model.
 * @author hung.nguyen
 */
public class Profile {

    private String email;
    private String firstName;
    private String familyName;

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
