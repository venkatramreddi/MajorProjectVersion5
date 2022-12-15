package com.myapps.majorprojectversion5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(etEmail.getText()).toString();
            String password = Objects.requireNonNull(etPassword.getText()).toString();
            if (email.length() >= 11) {
                if (password.length() >= 8) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isComplete()) {
                            startActivity(new Intent(getApplicationContext(), Main.class));
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    etPassword.setError("Error in Password");
                }
            } else {
                etEmail.setError("Error in Email");
            }
        });
    }
}