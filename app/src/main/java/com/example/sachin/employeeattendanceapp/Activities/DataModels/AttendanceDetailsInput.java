package com.example.sachin.employeeattendanceapp.Activities.DataModels;

import com.example.sachin.employeeattendanceapp.Activities.Activities.AttendanceDetails;
import com.google.gson.annotations.SerializedName;

public class AttendanceDetailsInput {

    @SerializedName("emp_id")
    String emp_id;

    @SerializedName("from_dt")
    String from_dt;

    @SerializedName("to_dt")
    String to_dt;

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getFrom_dt() {
        return from_dt;
    }

    public void setFrom_dt(String from_dt) {
        this.from_dt = from_dt;
    }

    public String getTo_dt() {
        return to_dt;
    }

    public void setTo_dt(String to_dt) {
        this.to_dt = to_dt;
    }
}
