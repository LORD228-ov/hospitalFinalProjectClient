package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Staff;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaffSignUpActivity extends AppCompatActivity {
    private ProgressBar pbConnectStaffSignUp;
    private Button btnReconnectStaffSignUp;
    private ListView lvStaffsForSignUp;
    private ArrayAdapter<Staff> adapter;
    private ArrayList<Staff> staffs;
    private Gson gson;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_sign_up);

        init();
        connect();
    }
    public void connect() {
        pbConnectStaffSignUp.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.103", 8888), 5000);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                runOnUiThread(() -> {
                    btnReconnectStaffSignUp.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
                getStaffs();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(()-> pbConnectStaffSignUp.setVisibility(View.INVISIBLE));
        }).start();
        gson = new Gson();
    }

    private void init() {
        pbConnectStaffSignUp = findViewById(R.id.pbConnectStaffSignUp);
        btnReconnectStaffSignUp = findViewById(R.id.btnReconnectStaffSignUp);
        lvStaffsForSignUp = findViewById(R.id.lvStaffsForSignUp);

        lvStaffsForSignUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Staff staff = staffs.get(position);
                Intent intent = new Intent(getApplicationContext(), StaffWorkActivity.class);
                intent.putExtra(MyRequestCode.KEY_INTENT, staff);
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        btnReconnectStaffSignUp.setOnClickListener(v -> {
            connect();
        });
    }

    public void getStaffs() {
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_GET_STAFFS);
            writer.flush();
            if (scanner.hasNextLine()) {
                String jsonStaff = scanner.nextLine();
                Type type = new TypeToken<List<Staff>>() {
                }.getType();
                staffs = gson.fromJson(jsonStaff, type);
                runOnUiThread(() -> {
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, staffs);
                    lvStaffsForSignUp.setAdapter(adapter);
                });
            }
        }).start();
    }
}