package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.example.hospitalfinalprojectclient.Sick;
import com.example.hospitalfinalprojectclient.Staff;

public class StaffWorkActivity extends AppCompatActivity {
    private Button btnInfo, btnDelete;
    private Staff staff;
    private Intent intent;
    private Intent derivableIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_work);
        initView();
        initListener();
    }

    private void initView() {
        btnInfo = findViewById(R.id.btnInfo);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void initListener() {
        btnInfo.setOnClickListener(this::infoAboutSicks);
        btnDelete.setOnClickListener(this::deleteSick);
    }

    private void infoAboutSicks(View view) {
        derivableIntent = getIntent();
        staff = (Staff) derivableIntent.getSerializableExtra(MyRequestCode.KEY_INTENT);
        intent = new Intent(getApplicationContext(), StaffInfoAboutSicks.class);
        intent.putExtra(MyRequestCode.KEY_INTENT, staff);
        startActivity(intent);
    }

    private void deleteSick(View view) {
        Intent intent = new Intent(getApplicationContext(), StaffDeleteSickActivity.class);
        startActivity(intent);
    }
}