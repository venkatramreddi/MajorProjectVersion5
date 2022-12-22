package com.myapps.majorprojectversion5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TestAfterAd extends AppCompatActivity {

    TextView tvQuestion, tvOption1, tvOption2, tvOption3, tvOption4;
    TextInputEditText etCorrectAnswer;
    MaterialButton btnSubmit;
    String ques, op1, op2, op3, op4, correctAns;
    String title;
    ProgressBar progressBar;
    String coins;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_after_ad);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");


        tvQuestion = findViewById(R.id.textViewQuestion);
        tvOption1 = findViewById(R.id.textViewOption1);
        tvOption2 = findViewById(R.id.textViewOption2);
        tvOption3 = findViewById(R.id.textViewOption3);
        tvOption4 = findViewById(R.id.textViewOption4);
        etCorrectAnswer = findViewById(R.id.editTextAnswer);
        btnSubmit = findViewById(R.id.btnSubmitAnswer);
        progressBar = findViewById(R.id.pbTest);
        progressBar.setVisibility(View.INVISIBLE);


        title = getIntent().getStringExtra("title");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdsData/" + title);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Wallet/" + uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ques = "Question :" + snapshot.child("question").getValue(String.class);
                    op1 = "1. " + snapshot.child("option1").getValue(String.class);
                    op2 = "2. " + snapshot.child("option2").getValue(String.class);
                    op3 = "3. " + snapshot.child("option3").getValue(String.class);
                    op4 = "4. " + snapshot.child("option4").getValue(String.class);
                    correctAns = snapshot.child("correctAnswer").getValue(String.class);


                    tvQuestion.setText(ques);
                    tvOption1.setText(op1);
                    tvOption2.setText(op2);
                    tvOption3.setText(op3);
                    tvOption4.setText(op4);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnSubmit.setOnClickListener(view -> {
            String temp = Objects.requireNonNull(etCorrectAnswer.getText()).toString();
            if (temp.equals(correctAns)) {
                btnSubmit.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_LONG).show();
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        coins = snapshot.child("coins").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (coins == null) {
                    n = 0;
                }

                int newPoints = n + 100;
                Coins coins = new Coins(String.valueOf(newPoints));
                databaseReference1.setValue(coins).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Toast.makeText(getApplicationContext(), "Points Added Successfully, Redirecting to Home", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.VISIBLE);
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(() -> {
                            Intent intent = new Intent(getApplicationContext(), Main.class);
                            startActivity(intent);
                        }, 3000L);
                    }

                });


            } else {
                Toast.makeText(getApplicationContext(), "Wrong Answer, Redirecting to Home", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
                Handler mHandler = new Handler();
                mHandler.postDelayed(() -> {
                    Intent intent = new Intent(getApplicationContext(), Main.class);
                    startActivity(intent);
                }, 3000L);

            }
        });


    }

    @Override
    public void onBackPressed() {

    }
}
