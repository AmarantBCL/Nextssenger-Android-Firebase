package com.example.android.nextssenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.nextssenger.viewmodel.RegistrationViewModel;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextName, editTextLastName, editTextAge;
    private Button buttonSignUp;

    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        setClickListeners();
    }

    private void setClickListeners() {
        buttonSignUp.setOnClickListener(v -> {
            String email = trimEditText(editTextEmail);
            String password = trimEditText(editTextPassword);
            String name = trimEditText(editTextName);
            String lastName = trimEditText(editTextLastName);
            String age = trimEditText(editTextAge);
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || lastName.isEmpty()
                    || age.isEmpty()) {
                Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.register(email, password, name, lastName, Integer.parseInt(age));
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMsg) {
                if (errorMsg != null) {
                    Toast.makeText(RegistrationActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    Intent intent = UsersActivity.newIntent(RegistrationActivity.this, user.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.edit_register_email);
        editTextPassword = findViewById(R.id.edit_register_password);
        editTextName = findViewById(R.id.edit_register_name);
        editTextLastName = findViewById(R.id.edit_register_last_name);
        editTextAge = findViewById(R.id.edit_register_age);
        buttonSignUp = findViewById(R.id.btn_sign_up);
    }

    private String trimEditText(EditText editText) {
        return editText.getText().toString().trim();
    }
}