package com.example.sachin.employeeattendanceapp.Activities.Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.sachin.employeeattendanceapp.Activities.Adapters.AttendanceReportAdapter;
import com.example.sachin.employeeattendanceapp.Activities.DataModels.AttendanceDetailsInput;
import com.example.sachin.employeeattendanceapp.Activities.DataModels.AttendanceReport;
import com.example.sachin.employeeattendanceapp.Activities.DataModels.EmployeesAttendanceDetails;
import com.example.sachin.employeeattendanceapp.Activities.Utils.Constants;
import com.example.sachin.employeeattendanceapp.Activities.Utils.SharedPreferenceUtils;
import com.example.sachin.employeeattendanceapp.Activities.WebServices.RestClient;
import com.example.sachin.employeeattendanceapp.Activities.WebServices.RestInterface;
import com.example.sachin.employeeattendanceapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceDetails extends AppCompatActivity {

    ArrayList<EmployeesAttendanceDetails> attendanceDetailsList = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    SharedPreferenceUtils sharedPreferenceUtils;
    int maxDays = 0;
    int monthValue = 0;
    String name;
    String month;
    int year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);

        sharedPreferenceUtils = new SharedPreferenceUtils(this);

        name = sharedPreferenceUtils.getString(Constants.EMP_NAME);
        month = sharedPreferenceUtils.getString(Constants.MONTH);
        year = Integer.valueOf(sharedPreferenceUtils.getString(Constants.YEAR));

        getTotalDays(month, year);

        Toast.makeText(this,""+maxDays, Toast.LENGTH_SHORT).show();
        Log.i("from", ""+year +"-"+ "0"+(monthValue+1) +"-"+ "01");
        Log.i("to", ""+year +"-"+ "0"+(monthValue+1) +"-"+ maxDays);

        ////*******************Please uncomment this when using with the web service api************************////
//        getAttendanceDetails(month, year);

        ////*******************Please comment this when not using web service api from here************************////
        attendanceDetailsList.add(new EmployeesAttendanceDetails("1603","2019-08-01 10:00:56", "2019-08-01 19:46:47"));
        attendanceDetailsList.add(new EmployeesAttendanceDetails("1603","2019-08-02 09:40:33", "2019-08-02 09:40:33"));
        attendanceDetailsList.add(new EmployeesAttendanceDetails("1603","2019-08-03 10:08:12", "2019-08-03 18:08:55"));
        attendanceDetailsList.add(new EmployeesAttendanceDetails("1603","2019-08-16 13:50:47", "2019-08-16 19:56:23"));
        attendanceDetailsList.add(new EmployeesAttendanceDetails("1603","2019-08-17 11:55:16", null));
        ////*******************Please comment this when not using web service api till here************************////

        ////*******************Please comment this when not using web service api************************////
        showUiDetails();

    }

    private void showUiDetails() {

        String dateStart = "";
        String dateEnd = "";
        AttendanceReport attendanceReport;



//        Toolbar attendanceDetailsToolbar = findViewById(R.id.attendanceDetailsToolbar);

        TextView employeesDetailsTitle = findViewById(R.id.employeesDetailsTitle);

        employeesDetailsTitle.setText(name+"'s"+ " Attendance report for "+ month +" "+ year);

        ArrayList<AttendanceReport> attendanceReportsList = new ArrayList<>();

        for (int i=0; i<attendanceDetailsList.size(); i++){
            attendanceReport = new AttendanceReport();

            dateStart = attendanceDetailsList.get(i).getEntry_at();
            dateEnd = attendanceDetailsList.get(i).getExit_at();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d1 = null;
            Date d2 = null;


            try {
                if (dateStart != null) {
                    d1 = format.parse(dateStart);
                }
                if (dateEnd != null) {
                    d2 = format.parse(dateEnd);
                }

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

//                total = total + diff;

                int day = d1.getDate();

                long diffHours = diff / (60 * 60 * 1000) % 24;

                attendanceReport.setHours(String.valueOf(diffHours));
                attendanceReport.setDay(day);

                Log.i("day", ""+attendanceReport.getDay());
                Log.i("totalHours", ""+attendanceReport.getHours());
//                Log.i("total", ""+total);

                attendanceReportsList.add(attendanceReport);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        long total = 0;
        int daysPresentNum = attendanceReportsList.size();
        int daysAbsentNum = maxDays - attendanceReportsList.size();
        for (int i=0; i< attendanceReportsList.size(); i++){
            total = total+ Long.valueOf(attendanceReportsList.get(i).getHours());

        }
        Log.i("total", ""+total);
        Log.i("daysPresentNum", ""+daysPresentNum);
        Log.i("daysAbsentNum", ""+daysAbsentNum);
        Log.i("attendanceListSize", ""+attendanceReportsList.size());

        TextView hoursLogged = findViewById(R.id.hoursLogged);
        hoursLogged.setText(String.valueOf(total));

        TextView daysPresent = findViewById(R.id.daysPresent);
        daysPresent.setText(String.valueOf(daysPresentNum));

        TextView daysAbsent = findViewById(R.id.daysAbsent);
        daysAbsent.setText(String.valueOf(daysAbsentNum));

        RecyclerView attendanceDetailsRecycler = findViewById(R.id.attendanceDetailsRecycler);
        attendanceDetailsRecycler.setLayoutManager(new LinearLayoutManager(this));

        AttendanceReportAdapter attendanceReportAdapter = new AttendanceReportAdapter(maxDays, attendanceReportsList);
        attendanceDetailsRecycler.setAdapter(attendanceReportAdapter);

    }

    private void getAttendanceDetails(String month, int year) {

        String empId = sharedPreferenceUtils.getString(Constants.EMP_ID);

        AttendanceDetailsInput attendanceDetailsInput = new AttendanceDetailsInput();
        attendanceDetailsInput.setEmp_id(empId);
        attendanceDetailsInput.setFrom_dt(year +"-"+ "0"+(monthValue+1) +"-"+ "01" );
        attendanceDetailsInput.setTo_dt(year +"-"+ "0"+(monthValue+1) +"-"+ maxDays);

        RestInterface restInterface = RestClient.getClient().create(RestInterface.class);
        restInterface.getAttendanceDetails(attendanceDetailsInput).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.code() == 200){

                    JsonElement jsonElement = response.body();

                    JsonArray jsonAttendanceArray = jsonElement.getAsJsonArray();

                    EmployeesAttendanceDetails employeesAttendanceDetails;

                    for (int i=0; i<jsonAttendanceArray.size(); i++){

                        JsonObject jsonAttendanceObj = jsonAttendanceArray.getAsJsonObject();

                        employeesAttendanceDetails = new EmployeesAttendanceDetails("", "", "");
                        if (jsonAttendanceObj.has("emp_id")){
                            if (!jsonAttendanceObj.get("emp_id").isJsonNull()) {
                                employeesAttendanceDetails.setEmp_id(jsonAttendanceObj.getAsJsonPrimitive("emp_id").getAsString());
                            } else {
                                employeesAttendanceDetails.setEmp_id("0");
                            }
                        }

                        if (jsonAttendanceObj.has("entry_at")){
                            if (!jsonAttendanceObj.get("entry_at").isJsonNull()) {
                                employeesAttendanceDetails.setEntry_at(jsonAttendanceObj.getAsJsonPrimitive("entry_at").getAsString());
                            } else {
                                employeesAttendanceDetails.setEntry_at(jsonAttendanceObj.getAsJsonPrimitive("exit_at").getAsString());
                            }
                        }

                        if (jsonAttendanceObj.has("exit_at")){
                            if (!jsonAttendanceObj.get("entry_at").isJsonNull()) {
                                employeesAttendanceDetails.setExit_at(jsonAttendanceObj.getAsJsonPrimitive("exit_at").getAsString());
                            } else {
                                employeesAttendanceDetails.setExit_at(jsonAttendanceObj.getAsJsonPrimitive("entry_at").getAsString());
                            }
                        }

                        attendanceDetailsList.add(employeesAttendanceDetails);

                    }

                } else {
                    Toast.makeText(AttendanceDetails.this,"failed to fetch data cuz api is not proper", Toast.LENGTH_LONG).show();
                }

                showUiDetails();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(AttendanceDetails.this,"failed to fetch data cuz api is not proper", Toast.LENGTH_LONG).show();
            }
        });

    }
//    from_dt=2019-08-01 to_dt=2019-08-31

    private void getTotalDays(String month, int year) {

        switch (month){
            case "January":
                calendar.set(year, Calendar.JANUARY, 1);
                monthValue = Calendar.JANUARY;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "February":
                calendar.set(year, Calendar.FEBRUARY, 1);
                monthValue = Calendar.FEBRUARY;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "March":
                calendar.set(year, Calendar.MARCH, 1);
                monthValue = Calendar.MARCH;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "April":
                calendar.set(year, Calendar.APRIL, 1);
                monthValue = Calendar.APRIL;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "May":
                calendar.set(year, Calendar.MAY, 1);
                monthValue = Calendar.MAY;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "June":
                calendar.set(year, Calendar.JUNE, 1);
                monthValue = Calendar.JUNE;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "July":
                calendar.set(year, Calendar.JULY, 1);
                monthValue = Calendar.JULY;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "August":
                calendar.set(year, Calendar.AUGUST, 1);
                monthValue = Calendar.AUGUST;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "September":
                calendar.set(year, Calendar.SEPTEMBER, 1);
                monthValue = Calendar.SEPTEMBER;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "October":
                calendar.set(year, Calendar.OCTOBER, 1);
                monthValue = Calendar.OCTOBER;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "November":
                calendar.set(year, Calendar.NOVEMBER, 1);
                monthValue = Calendar.NOVEMBER;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;

            case "December":
                calendar.set(year, Calendar.DECEMBER, 1);
                monthValue = Calendar.DECEMBER;
                maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;
        }

    }

}
