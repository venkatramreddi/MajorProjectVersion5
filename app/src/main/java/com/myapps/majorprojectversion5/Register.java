package com.myapps.majorprojectversion5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity {

    DatabaseReference refUsersData = FirebaseDatabase.getInstance("https://majorprojectversion5-default-rtdb.firebaseio.com/").getReference("UsersData");
    DatabaseReference refWallet = FirebaseDatabase.getInstance("https://majorprojectversion5-default-rtdb.firebaseio.com/").getReference("Wallet");
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextInputEditText etEmail, etUsername, etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Main.class));
        } else {
            etEmail = findViewById(R.id.etEmailRegister);
            etPassword = findViewById(R.id.etPasswordRegister);
            etUsername = findViewById(R.id.etUsernameRegister);
            MaterialButton btnRegister = findViewById(R.id.btnRegister);
            MaterialButton btnLogin = findViewById(R.id.btnRegisterLogin);
            btnLogin.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));


            btnRegister.setOnClickListener(view -> {
                String email = Objects.requireNonNull(etEmail.getText()).toString();
                String username = Objects.requireNonNull(etUsername.getText()).toString();
                String password = Objects.requireNonNull(etPassword.getText()).toString();

                if (email.length() >= 11) {
                    if (password.length() >= 8) {
                        if (username.length() >= 8) {
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                if (task.isComplete()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String uid = firebaseUser.getUid();
                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                    myEdit.putString("uid", Objects.requireNonNull(firebaseUser).getUid());
                                    myEdit.putString("ad", "1");
                                    myEdit.apply();

                                    UserData userData = new UserData(email, username, password, firebaseUser.getUid());

                                    refUsersData.child(firebaseUser.getUid()).setValue(userData).addOnCompleteListener(task1 -> {
                                        if (task1.isComplete()) {
                                            Toast.makeText(getApplicationContext(), "User Data Inserted Successfully!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error Inserting User Details!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    refWallet.child(firebaseUser.getUid()).child("coins").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                Toast.makeText(getApplicationContext(), "Wallet Created Successfully!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Failed Creating Wallet", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                                    startActivity(new Intent(getApplicationContext(), Main.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed Creating Account!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            etUsername.setError("username must be at least 8 chars");
                        }
                    } else {
                        etPassword.setError("pwd must be at least 8 chars");
                    }
                } else {
                    etEmail.setError("error in email");
                }


            });


        }

    }
}

class UserData {
    String email, username, password, userid;

    public UserData(String email, String username, String password, String userid) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class Coins {
    String coins;

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public Coins() {
    }

    public Coins(String coins) {
        this.coins = coins;
    }
}