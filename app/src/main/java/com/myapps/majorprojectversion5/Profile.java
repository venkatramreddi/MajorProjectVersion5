package com.myapps.majorprojectversion5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {
    TextView tvCoins, tvProfileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvCoins = view.findViewById(R.id.tvWalletCoins);

        DatabaseReference refWallet = FirebaseDatabase.getInstance("https://majorprojectversion5-default-rtdb.firebaseio.com/").getReference("Wallet");
        DatabaseReference refUserData = FirebaseDatabase.getInstance("https://majorprojectversion5-default-rtdb.firebaseio.com/").getReference("UsersData");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        assert firebaseUser != null;
        refWallet.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String coins = snapshot.child("coins").getValue(String.class);
                    tvCoins.setText(coins);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        return view;
    }
}