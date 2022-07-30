package com.example.android.nextssenger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.nextssenger.viewmodel.LoginViewModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgot, textViewRegister;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        initViews();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setClickListeners();
    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    Intent intent = UsersActivity.newIntent(LoginActivity.this, user.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMsg) {
                if (errorMsg != null) {
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setClickListeners() {
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password!", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.login(email, password);
            }
        });
        textViewForgot.setOnClickListener(v -> {
            Intent intent = ResetPasswordActivity.newIntent(LoginActivity.this,
                    editTextEmail.getText().toString().trim());
            startActivity(intent);
        });
        textViewRegister.setOnClickListener(v -> {
            Intent intent = RegistrationActivity.newIntent(LoginActivity.this);
            startActivity(intent);
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.btn_login);
        textViewForgot = findViewById(R.id.tv_forgot);
        textViewRegister = findViewById(R.id.tv_register);
    }
}