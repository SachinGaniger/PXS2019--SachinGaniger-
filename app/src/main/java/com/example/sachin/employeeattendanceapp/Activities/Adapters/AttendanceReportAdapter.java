package com.example.sachin.employeeattendanceapp.Activities.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sachin.employeeattendanceapp.Activities.DataModels.AttendanceReport;
import com.example.sachin.employeeattendanceapp.R;

import java.util.ArrayList;

public class AttendanceReportAdapter extends RecyclerView.Adapter<AttendanceReportAdapter.ViewHolder> {

    int maxDays;
    ArrayList<AttendanceReport> attendanceReportsList;

    public AttendanceReportAdapter(int _maxDays, ArrayList<AttendanceReport> attendanceReportsList){
        this.maxDays = _maxDays;
        this.attendanceReportsList = attendanceReportsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_report_details, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        AttendanceReport attendanceReport = attendanceReportsList.get(i);

        viewHolder.monthText.setText(String.valueOf(attendanceReport.getDay()));
        viewHolder.hoursLoggedText.setText(attendanceReport.getHours());
//            for (int j=0; j<attendanceReportsList.size(); j++){
//                if (i+1 == attendanceReportsList.get(j).getDay()){
//                    AttendanceReport attendanceReport = attendanceReportsList.get(i);
//                    viewHolder.hoursLoggedText.setText(attendanceReport.getHours());
//                } else {
//                    viewHolder.hoursLoggedText.setText("Not available");
//                }
//            }


    }

    @Override
    public int getItemCount() {
//        return maxDays;
        return attendanceReportsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView monthText;
        TextView hoursLoggedText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            monthText = itemView.findViewById(R.id.monthText);
            hoursLoggedText = itemView.findViewById(R.id.hoursLoggedText);
        }
    }
}
