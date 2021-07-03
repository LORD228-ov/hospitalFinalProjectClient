package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.CustomDialogFragment;
import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Sick;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class SickReportActivity extends AppCompatActivity {
    private ProgressBar pbConnectSickReport;
    private Button btnReconnectSickReport;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    private Sick sick;

    private MenuItem exitItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_report);

        Intent intent = getIntent();
        sick = (Sick) intent.getSerializableExtra(MyRequestCode.KEY_INTENT);

//        exitItem.setVisible(false);

        initView();
        initListener();
        connect();

    }

    public void connect() {
        pbConnectSickReport.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.103", 8888), 5000);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                runOnUiThread(() -> {
//                    exitItem.setVisible(true);
                    btnReconnectSickReport.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(()-> pbConnectSickReport.setVisibility(View.INVISIBLE));
        }).start();
    }

    private void initView() {
        pbConnectSickReport = findViewById(R.id.pbConnectSickReport);
        btnReconnectSickReport = findViewById(R.id.btnReconnectSickReport);
        exitItem = findViewById(R.id.exit);
    }

    private void initListener() {
        btnReconnectSickReport.setOnClickListener(v -> {
            connect();
        });
    }

    private void showDialog() {
        CustomDialogFragment dialog = new CustomDialogFragment(sick);
        dialog.show(getSupportFragmentManager(), "custom");
//        dialog.createDialog(sick);
    }

    public void deleteSick(Sick sick) {
        String time_visit = sick.getTime_visit();
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_DELETE_SICK);
            writer.println(time_visit);
            writer.flush();
            boolean result = Boolean.parseBoolean(scanner.nextLine());
            runOnUiThread(() -> {
            if (result) {
                Toast.makeText(this, "sign out!", Toast.LENGTH_SHORT).show();
            } if (!result){
                Toast.makeText(this, "stop clicking that button", Toast.LENGTH_SHORT).show();
            }
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alert_dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.alertDialog:
                showDialog();
                return true;
            case R.id.exit:
                deleteSick(sick);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}