package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private Button btnSick, btnStaff;
    private TextView tvChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      initView();
      initListener();

    }
    private void initView() {
        btnSick = findViewById(R.id.btnSick);
        btnStaff = findViewById(R.id.btnStaff);
        tvChoose = findViewById(R.id.tvChoose);
    }

    private void initListener() {
        btnSick.setOnClickListener(this::startAsSick);
        btnStaff.setOnClickListener(this::startAsStaff);
    }

    private void startAsSick(View view) {
        Intent intent = new Intent(getApplicationContext(), SickRegisterActivity.class);
        startActivity(intent);
    }

    private void startAsStaff(View view) {
        Intent intent = new Intent(getApplicationContext(), CheckStaffActivity.class);
        startActivity(intent);
    }
}