package com.example.hospitalfinalprojectclient;

import java.io.Serializable;

public class Sick implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String time_visit;
    private String last_name_of_service_personnel;

    public Sick(long id, String firstName, String lastName, String time_visit, String last_name_of_service_personnel) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.time_visit = time_visit;
        this.last_name_of_service_personnel = last_name_of_service_personnel;
    }

    public Sick(String firstName, String lastName, String time_visit, String last_name_of_service_personnel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.time_visit = time_visit;
        this.last_name_of_service_personnel = last_name_of_service_personnel;
    }

    public Sick(String firstName, String lastName, String time_visit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.time_visit = time_visit;
    }

    public Sick(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTime_visit() {
        return time_visit;
    }

    public void setTime_visit(String time_visit) {
        this.time_visit = time_visit;
    }

    public String getLast_name_of_service_personnel() {
        return last_name_of_service_personnel;
    }

    public void setLast_name_of_service_personnel(String last_name_of_service_personnel) {
        this.last_name_of_service_personnel = last_name_of_service_personnel;
    }

    @Override
    public String toString() {
        return "Sick{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", time_visit='" + time_visit + '\'' +
                ", last_name_of_service_personnel='" + last_name_of_service_personnel + '\'' +
                '}';
    }
}
