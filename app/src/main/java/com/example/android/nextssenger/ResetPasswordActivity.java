package com.example.android.nextssenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.nextssenger.architecture.ResetPasswordViewModel;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String EXTRA_EMAIL = "email";

    private EditText editTextEmail;
    private Button buttonReset;

    private ResetPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        initViews(email);
        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();
        setClickListeners();
    }

    private void setClickListeners() {
        buttonReset.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.resetPassword(email);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMsg) {
                if (errorMsg != null) {
                    Toast.makeText(ResetPasswordActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(ResetPasswordActivity.this, "The reset link has been successfully sent!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews(String email) {
        editTextEmail = findViewById(R.id.edit_reset_email);
        buttonReset = findViewById(R.id.btn_reset_password);
        editTextEmail.setText(email);
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }
}