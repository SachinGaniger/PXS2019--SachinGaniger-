package com.example.sachin.employeeattendanceapp.Activities.WebServices;

import com.example.sachin.employeeattendanceapp.Activities.DataModels.AttendanceDetailsInput;
import com.example.sachin.employeeattendanceapp.Activities.DataModels.Employees;
import com.example.sachin.employeeattendanceapp.Activities.DataModels.EmployeesAttendanceDetails;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestInterface {

    @GET("att_rprt_api.php?e76c37b493ea168cea60b8902072387caf297979")
    Call<JsonElement> getEmployees();

    @POST("att_rprt_api.php?e76c37b493ea168cea60b8902072387caf297979")
    Call<JsonElement> getAttendanceDetails(@Body AttendanceDetailsInput attendanceDetailsInput);

}
