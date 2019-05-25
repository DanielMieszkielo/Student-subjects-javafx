package com.library.Models;

import com.library.DAO.StudentDAO;

public class Student extends BaseModel {
    private int id;
    private String firstName;
    private String lastName;
    private String pesel;

    private boolean created = false;

    public Student(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    @Override
    public void save() {
        if (this.created) {
            StudentDAO.getInstance().update(this);
        } else {
            this.id = StudentDAO.getInstance().create(this);
            this.created = true;
        }
    }
}