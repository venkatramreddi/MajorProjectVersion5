package com.myapps.majorprojectversion5;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class Profile extends Fragment {

    Toolbar toolbar;
    EditText et;
    MaterialButton btnEditProfile;
    ImageView ivProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


//        toolbar = view.findViewById(R.id.toolBarProfile);
//        toolbar.setTitle("Profile");
//        toolbar.setTitleTextColor(Color.WHITE);

        et = view.findViewById(R.id.etProfileName);
        ivProfile = view.findViewById(R.id.ivProfile);

        et.setCursorVisible(false);
        et.setLongClickable(false);
        et.setClickable(false);
        et.setFocusable(false);
        et.setSelected(false);
        et.setKeyListener(null);
        et.setBackgroundResource(android.R.color.transparent);
//        btnEditProfile = view.findViewById(R.id.btnEditProfile);
//        btnEditProfile.setOnClickListener(view1 -> {
//            et.setCursorVisible(true);
//            et.setLongClickable(true);
//            et.setClickable(true);
//            et.setFocusable(true);
//            et.setSelected(true);

//            ivProfile.setClickable(true);

//            ivProfile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//            btnEditProfile.setText(R.string.Save);


//        });


        return view;
    }
}