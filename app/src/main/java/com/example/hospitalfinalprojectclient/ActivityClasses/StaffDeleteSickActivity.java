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

public class StaffDeleteSickActivity extends AppCompatActivity {
    private ProgressBar pbConnectStaffDeleteSick;
    private Button btnReconnectStaffDeleteSick, btnBackStaffDeleteSick;
    private ListView lvSicksForDeletion;
    private ArrayAdapter<Sick> adapter;
    private ArrayList<Sick> sicks;
    private Gson gson;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_delete_sick);
        init();
        initListener();
        connect();
    }

    public void connect() {
        pbConnectStaffDeleteSick.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.103", 8888), 5000);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                runOnUiThread(() -> {
                    btnReconnectStaffDeleteSick.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
                getSicks();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(()-> pbConnectStaffDeleteSick.setVisibility(View.INVISIBLE));
        }).start();
        gson = new Gson();
    }


    private void init() {
        pbConnectStaffDeleteSick = findViewById(R.id.pbConnectStaffDeleteSick);
        btnReconnectStaffDeleteSick = findViewById(R.id.btnReconnectStaffDeleteSick);
        btnBackStaffDeleteSick = findViewById(R.id.btnBackStaffDeleteSick);
        lvSicksForDeletion = findViewById(R.id.lvSicksForDeletion);

        lvSicksForDeletion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sick sick = sicks.get(position);
                deleteSick(sick);
                back();
            }
        });
    }

    private void initListener() {
        btnReconnectStaffDeleteSick.setOnClickListener(v -> {
            connect();
        });
        btnBackStaffDeleteSick.setOnClickListener(v -> {
            back();
        });
    }

    public void getSicks() {
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_GET_SICKS);
            writer.flush();
            if (scanner.hasNextLine()) {
                String jsonSick = scanner.nextLine();
                Type type = new TypeToken<List<Sick>>() {
                }.getType();
                sicks = gson.fromJson(jsonSick, type);
                runOnUiThread(() -> {
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sicks);
                    lvSicksForDeletion.setAdapter(adapter);
                });
            }
        }).start();
    }

    public void deleteSick(Sick sick) {
        String time_visit = sick.getTime_visit();
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_DELETE_SICK);
            writer.println(time_visit);
            writer.flush();
        }).start();
    }

    private void back() {
        Intent intent = new Intent(getApplicationContext(), StaffWorkActivity.class);
        startActivity(intent);
    }
}