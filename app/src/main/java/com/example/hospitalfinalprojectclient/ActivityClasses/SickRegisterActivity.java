package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Sick;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

public class SickRegisterActivity extends AppCompatActivity {
    private EditText etMassageSickFirst_name, etMassageSickLast_name;
    private Button btnSickRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_register);

        initView();
        initListener();
    }
    private void initView() {
        btnSickRegister = findViewById(R.id.btnSickRegister);
        etMassageSickFirst_name = findViewById(R.id.etMassageSickFirst_name);
        etMassageSickLast_name = findViewById(R.id.etMassageSickLast_name);

    }

    private void initListener() {
        btnSickRegister.setOnClickListener(this::continueRegistration);
    }

    private void continueRegistration(View view) {
        String First_name = etMassageSickFirst_name.getText().toString();
        String Last_name = etMassageSickLast_name.getText().toString();

        if (First_name.isEmpty()) {
            etMassageSickFirst_name.setError("empty");
            return;
        }

        if (Last_name.isEmpty()) {
            etMassageSickLast_name.setError("empty");
            return;
        }

        Sick sick = new Sick(First_name, Last_name);
        Intent intent = new Intent(getApplicationContext(), CustomCalendarActivity.class);
        intent.putExtra(MyRequestCode.KEY_INTENT, sick);
        startActivity(intent);
    }

}