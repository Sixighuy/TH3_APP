package com.example.quanlykhohang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText edtUsername, edtPassword;
    private TextView tvError;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ánh xạ view
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();

        // chưa nhập gì thì báo lỗi luôn
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            tvError.setText("Vui lòng nhập tên đăng nhập và mật khẩu");
            tvError.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập và mật khẩu",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = dbHelper.checkLogin(username, password);
        if (ok) {
            // đúng thì qua màn hình chính
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // finish để nhấn Back không quay lại đây nữa
        } else {
            tvError.setText("Sai tên đăng nhập hoặc mật khẩu");
            tvError.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
