package com.example.sachin.employeeattendanceapp.Activities.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.sachin.employeeattendanceapp.Activities.DataModels.Employees;
import com.example.sachin.employeeattendanceapp.Activities.Utils.Constants;
import com.example.sachin.employeeattendanceapp.Activities.Utils.SharedPreferenceUtils;
import com.example.sachin.employeeattendanceapp.Activities.WebServices.RestClient;
import com.example.sachin.employeeattendanceapp.Activities.WebServices.RestInterface;
import com.example.sachin.employeeattendanceapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeList extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    ArrayList<Employees> employeeList;
    ArrayList<String> employeesNames = new ArrayList<>();
    ArrayList<String> yearList;
    ArrayList<String> monthList;
    SharedPreferenceUtils sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        sharedPreferences = new SharedPreferenceUtils(EmployeeList.this);
        getEmployeeList();
    }

    private void getEmployeeList() {

        employeeList = new ArrayList<>();

        RestInterface restInterface = RestClient.getClient().create(RestInterface.class);
        restInterface.getEmployees().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.code() == 200){

                    JsonElement jsonElement = response.body();

                    JsonArray jsonEmployeesList = jsonElement.getAsJsonArray();

                    Employees employees;
                    for (int i=0; i<jsonEmployeesList.size(); i++){

                        employees = new Employees();
                        JsonObject jsonEmpObj = jsonEmployeesList.get(i).getAsJsonObject();

                        if (jsonEmpObj.has("emp_id")){


                            if (jsonEmpObj.get("emp_id").isJsonNull()){
                                employees.setEmp_id("0");
                            } else{
                                employees.setEmp_id(jsonEmpObj.getAsJsonPrimitive("emp_id").getAsString());
                            }

                        }

                        if (jsonEmpObj.has("name")){
                            if (jsonEmpObj.get("name").getAsString().equals(" ")){
                                employees.setName("Select Employees");
                            } else{
                                employees.setName(jsonEmpObj.getAsJsonPrimitive("name").getAsString());
                            }

                        }
                            employeeList.add(employees);

                    }
                    Log.i("employeeListSize", ""+ employeeList.size());
                } else {
                    Toast.makeText(EmployeeList.this,"failed to fetch data", Toast.LENGTH_LONG).show();
                }

                ShowUI(employeeList);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(EmployeeList.this,"failed to fetch data", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void ShowUI(ArrayList<Employees> employeeList) {

        Spinner employeesDropdown = findViewById(R.id.employeesDropdown);
        employeesDropdown.setOnItemSelectedListener(this);

        Spinner monthDropdown = findViewById(R.id.monthDropdown);
        monthDropdown.setOnItemSelectedListener(this);

        Spinner yearDropdown = findViewById(R.id.yearDropdown);
        yearDropdown.setOnItemSelectedListener(this);

        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(this);

//        Toolbar employeesToolbar = findViewById(R.id.employeesToolbar);
//
        TextView employeesDetailsTitle = findViewById(R.id.employeesDetailsTitle);

        employeesDetailsTitle.setText("Employee Attendance app");

        yearList = new ArrayList<>();
        yearList.add("2010");
        yearList.add("2011");
        yearList.add("2012");
        yearList.add("2013");
        yearList.add("2014");
        yearList.add("2015");
        yearList.add("2016");
        yearList.add("2017");
        yearList.add("2018");
        yearList.add("2019");

        monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        for (int i=0; i< employeeList.size(); i++){
            employeesNames.add(employeeList.get(i).getName());
        }

        ArrayAdapter<String> employeeSpinnerData = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, employeesNames);
        employeeSpinnerData.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        employeesDropdown.setAdapter(employeeSpinnerData);

        ArrayAdapter<String> yearSpinnerData = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, yearList);
        yearSpinnerData.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        yearDropdown.setAdapter(yearSpinnerData);

        ArrayAdapter<String> monthSpinnerData = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, monthList);
        monthSpinnerData.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        monthDropdown.setAdapter(monthSpinnerData);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){

            case R.id.employeesDropdown:
                String empId = employeeList.get(position).getEmp_id();
                String empName = employeeList.get(position).getName();

                sharedPreferences.putString(Constants.EMP_ID, empId);
                sharedPreferences.putString(Constants.EMP_NAME, empName);

                Log.i("empId", empId);
                Log.i("empName", empName);
                break;

            case R.id.monthDropdown:
                String month = monthList.get(position);
                Log.i("month", month);

                sharedPreferences.putString(Constants.MONTH, month);

                break;

            case R.id.yearDropdown:
                String year = yearList.get(position);
                Log.i("year", year);

                sharedPreferences.putString(Constants.YEAR, year);

                break;

        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.reportButton){

            Intent intent = new Intent(EmployeeList.this, AttendanceDetails.class);
            startActivity(intent);

        }

    }
}
