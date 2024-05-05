package com.example.finalprojectse;

public class Profile {
    private String username;
    private String email;
    private String address;
    private String maritalStatus;
    private int age;
    private String birthday;
    private String allergies;
    // Additional fields as needed

    public Profile(String username, String email, String address, String maritalStatus, int age, String birthday, String allergies) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.maritalStatus = maritalStatus;
        this.age = age;
        this.birthday = birthday;
        this.allergies = allergies;
    }

    // Getter methods for each field

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public int getAge() {
        return age;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAllergies() {
        return allergies;
    }

    // Setter methods can be added if needed

}