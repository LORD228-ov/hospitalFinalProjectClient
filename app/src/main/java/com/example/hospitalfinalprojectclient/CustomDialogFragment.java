package com.example.hospitalfinalprojectclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.hospitalfinalprojectclient.ActivityClasses.SickReportActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CustomDialogFragment extends DialogFragment {
    Sick sick;


    public CustomDialogFragment(Sick sick) {
        this.sick = sick;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String firstName = sick.getFirstName();
        String lastName = sick.getLastName();
        String time_visit = sick.getTime_visit();
        String last_name_of_service_personnel = sick.getLast_name_of_service_personnel();
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Ура! Вы записались к нам в больницу!")
                .setIcon(android.R.drawable.ic_input_get)
                .setMessage("Здравсвуйте, " + firstName + " " + lastName + " вы записались на "
                        + time_visit + " к " + last_name_of_service_personnel + "-у, Приходите и не опаздывайте!")
                .setPositiveButton("OK", null)
                .create();
    }

//    public Dialog createDialog(Sick sick) {
//        String firstName = sick.getFirstName();
//        String lastName = sick.getLastName();
//        String time_visit = sick.getTime_visit();
//        String last_name_of_service_personnel = sick.getLastName();
//        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
//        return builder
//                .setTitle("Ура! Вы записались к нам в больницу!")
//                .setIcon(android.R.drawable.ic_input_get)
//                .setMessage("Здравсвуйте, " + firstName + " " + lastName + " вы записались на "
//                        + time_visit + " к " + last_name_of_service_personnel + "-у, Приходите и не опаздывайте!")
//                .setPositiveButton("OK", null)
//                .create();
//    }

}
