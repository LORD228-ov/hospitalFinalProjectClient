package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Sick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CustomCalendarActivity extends AppCompatActivity {
    CustomCalendar customCalendar;
    private Button btnReconnectCustomCalendar;
    private ProgressBar pbConnectCustomCalendar;
    private Gson gson;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;
    private ArrayList<Sick> sicks;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_calendar);
        initView();
        initListener();
        connect();
    }

    public void connect() {
        pbConnectCustomCalendar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.0.103", 8888), 5000);
                writer = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                runOnUiThread(() -> {
                    btnReconnectCustomCalendar.setVisibility(View.INVISIBLE);
                    customCalendar.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(()-> pbConnectCustomCalendar.setVisibility(View.INVISIBLE));
        }).start();
        gson = new Gson();
        initCustomCalendar();
        getSicks();
    }

    private void initView() {
        pbConnectCustomCalendar = findViewById(R.id.pbConnectCustomCalendar);
        btnReconnectCustomCalendar = findViewById(R.id.btnReconnectCustomCalendar);
        customCalendar = findViewById(R.id.custom_calendar);
    }

    private void initListener() {
        btnReconnectCustomCalendar.setOnClickListener(v -> {
            connect();
        });
    }


    private void initCustomCalendar() {
        HashMap<Object, Property> descHashMap = new HashMap<>();

        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.default_view;
        defaultProperty.dateTextViewResource = R.id.text_view;

        descHashMap.put("default", defaultProperty);

        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.text_view;

        descHashMap.put("current", currentProperty);

        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.text_view;

        descHashMap.put("present", presentProperty);

        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.text_view;

        descHashMap.put("absent", absentProperty);

        customCalendar.setMapDescToProp(descHashMap);

        HashMap<Integer, Object> dateHashMap = new HashMap<>();

        Calendar calendar = Calendar.getInstance();

        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");
        dateHashMap.put(1, "absent");
        dateHashMap.put(2, "present");
        dateHashMap.put(3, "absent");
        dateHashMap.put(4, "present");
        dateHashMap.put(5, "absent");
        dateHashMap.put(6, "present");
        dateHashMap.put(7, "absent");
        dateHashMap.put(8, "present");
        dateHashMap.put(9, "absent");
        dateHashMap.put(10, "present");
        dateHashMap.put(11, "absent");
        dateHashMap.put(12, "present");
        dateHashMap.put(13, "absent");
        dateHashMap.put(14, "present");
        dateHashMap.put(15, "absent");
        dateHashMap.put(16, "present");
        dateHashMap.put(17, "absent");
        dateHashMap.put(18, "present");
        dateHashMap.put(19, "absent");
        dateHashMap.put(20, "present");
        dateHashMap.put(21, "absent");
        dateHashMap.put(22, "present");
        dateHashMap.put(23, "absent");
        dateHashMap.put(24, "present");
        dateHashMap.put(25, "absent");
        dateHashMap.put(26, "present");
        dateHashMap.put(27, "absent");
        dateHashMap.put(28, "present");
        dateHashMap.put(29, "absent");
        dateHashMap.put(30, "present");
        dateHashMap.put(31, "absent");

        customCalendar.setDate(calendar, dateHashMap);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String time_visit = selectedDate.get(Calendar.DAY_OF_MONTH)
                        + "." + (selectedDate.get(Calendar.MONTH) + 1)
                        + "." + selectedDate.get(Calendar.YEAR);


                result = checkSicks(time_visit);
                if (!result) {
                    Intent derivableIntent = getIntent();
                    Sick derivableSick = (Sick) derivableIntent.getSerializableExtra(MyRequestCode.KEY_INTENT);
                    String firstName = derivableSick.getFirstName();
                    String lastName = derivableSick.getLastName();
                    Sick sick = new Sick(firstName, lastName, time_visit);
                    Intent intent = new Intent(getApplicationContext(), SickListRegisterActivity.class);
                    intent.putExtra(MyRequestCode.KEY_INTENT, sick);
                    startActivity(intent);
                }
                if (result) {
                    Toast.makeText(CustomCalendarActivity.this, "День занят!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getSicks() {
        new Thread(() -> {
            result = false;
            writer.println(MyRequestCode.KEY_GET_SICKS);
            writer.flush();
            if (scanner.hasNextLine()) {
                String jsonSick = scanner.nextLine();
                Type type = new TypeToken<List<Sick>>() {
                }.getType();
                sicks = gson.fromJson(jsonSick, type);
            }
        }).start();
    }

    public boolean checkSicks(String time_visit) {
        result = false;
        for (Sick sick : sicks) {
            if (sick.getTime_visit().equals(time_visit)) {
                result = true;
                break;
            }
        }
        return result;
    }
}