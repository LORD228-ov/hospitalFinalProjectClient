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

public class StaffInfoAboutSicks extends AppCompatActivity {
    private ProgressBar pbConnectStaffInfoAboutSicks;
    private Button btnReconnectStaffInfoAboutSicks, btnBackStaffInfoAboutSicks;
    private ListView lvSicksInfo;
    private ArrayAdapter<Sick> adapter;
    private ArrayList<Sick> sicks;
    private Gson gson;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;
    private Staff staff;
    private String jsonStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_info_about_sicks);

        init();
        initListener();
        connect();
    }

    public void connect() {
        pbConnectStaffInfoAboutSicks.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.103", 8888), 5000);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                runOnUiThread(() -> {
                    btnReconnectStaffInfoAboutSicks.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
                getSicksWhoRecordedToYou();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(()-> pbConnectStaffInfoAboutSicks.setVisibility(View.INVISIBLE));
        }).start();
        gson = new Gson();
        jsonStaff = gson.toJson(staff);
    }

    private void init() {
        Intent derivableIntent = getIntent();
        staff = (Staff) derivableIntent.getSerializableExtra(MyRequestCode.KEY_INTENT);
        pbConnectStaffInfoAboutSicks = findViewById(R.id.pbConnectStaffInfoAboutSicks);
        btnReconnectStaffInfoAboutSicks = findViewById(R.id.btnReconnectStaffInfoAboutSicks);
        btnBackStaffInfoAboutSicks = findViewById(R.id.btnBackStaffInfoAboutSicks);
        lvSicksInfo = findViewById(R.id.lvSicksInfo);

        lvSicksInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back();
            }
        });
    }

    private void initListener() {
        btnReconnectStaffInfoAboutSicks.setOnClickListener(v -> {
            connect();
        });
        btnBackStaffInfoAboutSicks.setOnClickListener(v -> {
            back();
        });
    }

    public void getSicksWhoRecordedToYou() {
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_GET_SICKS_WHO_RECORDED_TO_YOU);
            writer.println(jsonStaff);
            writer.flush();
            if (scanner.hasNextLine()) {
                String jsonSick = scanner.nextLine();
                Type type = new TypeToken<List<Sick>>() {
                }.getType();
                sicks = gson.fromJson(jsonSick, type);
                runOnUiThread(() -> {
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sicks);
                    lvSicksInfo.setAdapter(adapter);
                });
            }
        }).start();
    }

    private void back() {
        Intent intent = new Intent(getApplicationContext(), StaffWorkActivity.class);
        intent.putExtra(MyRequestCode.KEY_INTENT, staff);
        startActivity(intent);
    }
}