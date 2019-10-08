package com.example.sachin.employeeattendanceapp.Activities.DataModels;

import com.google.gson.annotations.SerializedName;

public class Employees {

    @SerializedName("emp_id")
    String emp_id;

    @SerializedName("name")
    String name;

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
