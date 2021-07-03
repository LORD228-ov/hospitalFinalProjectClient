package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Sick;
import com.example.hospitalfinalprojectclient.Staff;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class StaffRegisterActivity extends AppCompatActivity {
    private EditText etStaffFirst_name, etStaffLast_name, etStaffProfession;
    private Button btnRegister, btnReconnectStaffRegister;
    private ProgressBar pbConnectStaffRegister;

    private Gson gson;
    private Socket socket;
    private PrintWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_register);

        initView();
        initListener();
        connect();
    }
    private void initView() {
        etStaffFirst_name = findViewById(R.id.etStaffFirst_name);
        etStaffLast_name = findViewById(R.id.etStaffLast_name);
        etStaffProfession = findViewById(R.id.etStaffProfession);
        btnRegister = findViewById(R.id.btnRegister);
        pbConnectStaffRegister = findViewById(R.id.pbConnectStaffRegister);
        btnReconnectStaffRegister = findViewById(R.id.btnReconnectStaffRegister);
    }

    private void initListener() {
        btnRegister.setOnClickListener(this::register);
        btnReconnectStaffRegister.setOnClickListener(v -> {
            connect();
        });
    }

    public void connect() {
        pbConnectStaffRegister.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.103", 8888), 5000);
                writer = new PrintWriter(socket.getOutputStream());
                runOnUiThread(() -> {
                    btnReconnectStaffRegister.setVisibility(View.INVISIBLE);
                    btnRegister.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(()-> pbConnectStaffRegister.setVisibility(View.INVISIBLE));
        }).start();
        gson = new Gson();
    }

    private void register(View view) {
        String First_name = etStaffFirst_name.getText().toString();
        String Last_name = etStaffLast_name.getText().toString();
        String Profession = etStaffProfession.getText().toString();

        if (First_name.isEmpty()) {
            etStaffFirst_name.setError("empty");
            return;
        }

        if (Last_name.isEmpty()) {
            etStaffLast_name.setError("empty");
            return;
        }

        if (Profession.isEmpty()) {
            etStaffProfession.setError("short password");
            return;
        }

        Staff staff = new Staff(First_name, Last_name, Profession);
        saveStaff(staff);
        Intent intent = new Intent(getApplicationContext(), StaffWorkActivity.class);
        intent.putExtra(MyRequestCode.KEY_INTENT, staff);
        startActivity(intent);
    }
    public void saveStaff(Staff staff) {
        String jsonSick = gson.toJson(staff);
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_PUT_STAFF);
            writer.println(jsonSick);
            writer.flush();
        }).start();
    }
}