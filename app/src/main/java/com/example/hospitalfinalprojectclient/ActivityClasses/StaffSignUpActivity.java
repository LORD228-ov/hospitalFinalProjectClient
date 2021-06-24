package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Staff;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaffSignUpActivity extends AppCompatActivity {
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

        new Thread(() -> {
            try {
                socket = new Socket("192.168.0.103", 8888);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                runOnUiThread(() -> {
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
                getStaffs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void init() {
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
        gson = new Gson();
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