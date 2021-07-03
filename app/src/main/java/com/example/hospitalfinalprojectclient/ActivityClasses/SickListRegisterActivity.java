package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Sick;
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

public class SickListRegisterActivity extends AppCompatActivity {
    private Button btnReconnectSickListRegister;
    private ProgressBar pbConnectSickListRegister;
    private ListView lvStaffsForRegister;
    private ArrayAdapter<Staff> adapter;
    private ArrayList<Staff> staffs;
    private Gson gson;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_list_register);
        init();
        initListener();
        connect();
    }

    public void connect() {
        pbConnectSickListRegister.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.103", 8888), 5000);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                runOnUiThread(() -> {
                    btnReconnectSickListRegister.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
                getStaffs();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(()-> pbConnectSickListRegister.setVisibility(View.INVISIBLE));
        }).start();
        gson = new Gson();
    }

    private void init() {
        btnReconnectSickListRegister = findViewById(R.id.btnReconnectSickListRegister);
        pbConnectSickListRegister = findViewById(R.id.pbConnectSickListRegister);
        lvStaffsForRegister = findViewById(R.id.lvStaffsForRegister);

        lvStaffsForRegister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent derivableIntent = getIntent();
                Sick derivableSick = (Sick) derivableIntent.getSerializableExtra(MyRequestCode.KEY_INTENT);
                String firstName = derivableSick.getFirstName();
                String lastName = derivableSick.getLastName();
                String time_visit = derivableSick.getTime_visit();
                Staff staff = staffs.get(position);
                String last_name_of_service_personnel = staff.getLastName();
                Sick sick = new Sick(firstName, lastName, time_visit, last_name_of_service_personnel);
                saveSick(sick);
                Intent intent = new Intent(getApplicationContext(), SickReportActivity.class);
                intent.putExtra(MyRequestCode.KEY_INTENT, sick);
                startActivity(intent);
            }
        });

    }
    private void initListener() {
        btnReconnectSickListRegister.setOnClickListener(v -> {
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
                    lvStaffsForRegister.setAdapter(adapter);
                });
            }
        }).start();
    }

    public void saveSick(Sick sick) {
        String jsonSick = gson.toJson(sick);

        new Thread(() -> {
            writer.println(MyRequestCode.KEY_PUT_SICK);
            writer.println(jsonSick);
            writer.flush();
        }).start();
    }
}