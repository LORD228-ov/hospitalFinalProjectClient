package com.example.hospitalfinalprojectclient.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hospitalfinalprojectclient.CheckRegistration;
import com.example.hospitalfinalprojectclient.MyRequestCode;
import com.example.hospitalfinalprojectclient.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CheckStaffActivity extends AppCompatActivity {
    private EditText etLogin, etPassword;
    private Button btnStaffRegister, btnSignUp;

    private Gson gson;
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_staff);
        initView();
        initListener();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        gson = new Gson();
    }

    private void initView() {
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnStaffRegister = findViewById(R.id.btnStaffRegister);
        btnSignUp = findViewById(R.id.btnSignUp);

    }

    private void initListener() {
        btnStaffRegister.setOnClickListener(this::register);
        btnSignUp.setOnClickListener(this::signUp);
    }

    private void register(View view) {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        String hashPassword = hashFromPassword(password);
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_CHECK_REGISTRATION);
            writer.flush();
            String jsonCheckRegistration = scanner.nextLine();
            CheckRegistration checkRegistration = gson.fromJson(jsonCheckRegistration, CheckRegistration.class);
            runOnUiThread(() -> {
                if (login.equals(checkRegistration.getLogin_db()) && hashPassword.equals(checkRegistration.getPassword_db())) {
                    Intent intent = new Intent(getApplicationContext(), StaffRegisterActivity.class);
                    startActivity(intent);
                } else {
                    etLogin.setError("wrong login");
                    etPassword.setError("wrong password");
                }
            });
        }).start();

    }

    private void signUp(View view) {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        String hashPassword = hashFromPassword(password);
        new Thread(() -> {
            writer.println(MyRequestCode.KEY_CHECK_REGISTRATION);
            writer.flush();
            String jsonCheckRegistration = scanner.nextLine();
            CheckRegistration checkRegistration = gson.fromJson(jsonCheckRegistration, CheckRegistration.class);
            runOnUiThread(() -> {
                if (login.equals(checkRegistration.getLogin_db()) && hashPassword.equals(checkRegistration.getPassword_db())) {
                    Intent intent = new Intent(getApplicationContext(), StaffSignUpActivity.class);
                    startActivity(intent);
                } else {
                    etLogin.setError("wrong login");
                    etPassword.setError("wrong password");
                }
            });
        }).start();

    }

    private static String hashFromPassword(String password) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashBytes = md5.digest(password.getBytes());
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[hashBytes.length * 2];
        for (int j = 0; j < hashBytes.length; j++) {
            int v = hashBytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }
}