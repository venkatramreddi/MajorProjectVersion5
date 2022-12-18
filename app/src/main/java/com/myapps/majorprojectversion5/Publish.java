package com.myapps.majorprojectversion5;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Publish extends Fragment {

    private TextInputEditText etTitle, etQuestion, etOption1, etOption2, etOption3, etOption4;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    private ImageView ivUpload;
    private ProgressBar progressBar;
    private final StorageReference storageReference = FirebaseStorage.getInstance("gs://majorprojectversion5.appspot.com").getReference();
    private final DatabaseReference refAdsData = FirebaseDatabase.getInstance("https://majorprojectversion5-default-rtdb.firebaseio.com/").getReference("AdsData");
    private final DatabaseReference refTotalAds = FirebaseDatabase.getInstance("https://majorprojectversion5-default-rtdb.firebaseio.com/").getReference().child("TotalAds");
    private final DatabaseReference refTitles = FirebaseDatabase.getInstance("https://majorprojectversion5-default-rtdb.firebaseio.com/").getReference("Titles");
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private int totalNoAds;
    private String correctAnswer;
    private Uri imageUri;
    private String uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        etTitle = view.findViewById(R.id.etTitle);
        etQuestion = view.findViewById(R.id.etQuestion);
        etOption1 = view.findViewById(R.id.etOption1);
        etOption2 = view.findViewById(R.id.etOption2);
        etOption3 = view.findViewById(R.id.etOption3);
        etOption4 = view.findViewById(R.id.etOption4);
        checkBox1 = view.findViewById(R.id.checkbox1);
        checkBox2 = view.findViewById(R.id.checkbox2);
        checkBox3 = view.findViewById(R.id.checkbox3);
        checkBox4 = view.findViewById(R.id.checkbox4);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        ivUpload = view.findViewById(R.id.ivUpload);
        MaterialButton btnUpload = view.findViewById(R.id.btnUpload);

        checkBox1.setOnClickListener(view1 -> {
            correctAnswer = "1";
            if (checkBox4.isChecked())
                checkBox4.toggle();
            if (checkBox2.isChecked())
                checkBox2.toggle();
            if (checkBox3.isChecked())
                checkBox3.toggle();
        });
        checkBox2.setOnClickListener(view2 -> {
            correctAnswer = "2";
            if (checkBox1.isChecked())
                checkBox1.toggle();
            if (checkBox4.isChecked())
                checkBox4.toggle();
            if (checkBox3.isChecked())
                checkBox3.toggle();
        });
        checkBox3.setOnClickListener(view3 -> {
            correctAnswer = "3";
            if (checkBox1.isChecked())
                checkBox1.toggle();
            if (checkBox2.isChecked())
                checkBox2.toggle();
            if (checkBox4.isChecked())
                checkBox4.toggle();
        });
        checkBox4.setOnClickListener(view4 -> {
            correctAnswer = "4";
            if (checkBox1.isChecked())
                checkBox1.toggle();
            if (checkBox2.isChecked())
                checkBox2.toggle();
            if (checkBox3.isChecked())
                checkBox3.toggle();
        });


        ivUpload.setOnClickListener(view12 -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        });


        btnUpload.setOnClickListener(view13 -> {
            if (imageUri != null) {
                progressBar.setVisibility(View.INVISIBLE);
                uploadToFirebase(imageUri);
                ivUpload.setImageResource(R.drawable.ic_baseline_image_24);
                etOption1.setText("");
                etOption2.setText("");
                etOption3.setText("");
                etOption4.setText("");
                etQuestion.setText("");
                etTitle.setText("");
                progressBar.setVisibility(View.INVISIBLE);

            } else {
                Toast.makeText(requireActivity().getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
            }

        });


        return view;
    }

    private void uploadToFirebase(Uri imageUri) {


        progressBar.setVisibility(View.VISIBLE);
        String title = Objects.requireNonNull(etTitle.getText()).toString();
        String question = Objects.requireNonNull(etQuestion.getText()).toString();
        String option1 = Objects.requireNonNull(etOption1.getText()).toString();
        String option2 = Objects.requireNonNull(etOption2.getText()).toString();
        String option3 = Objects.requireNonNull(etOption3.getText()).toString();
        String option4 = Objects.requireNonNull(etOption4.getText()).toString();


        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        long l = System.currentTimeMillis();
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            progressBar.setVisibility(View.VISIBLE);
            UrlClass urlClass = new UrlClass(uri.toString());
            AdDetails adDetails = new AdDetails(title, question, option1, option2, option3, option4, String.valueOf(correctAnswer), String.valueOf(urlClass.url));
            if (!refAdsData.child(title).setValue(adDetails).isComplete()) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Ad Uploaded Successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), "Failed Uploading Ad", Toast.LENGTH_LONG).show();
            }


        }));


    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = requireActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            ivUpload.setImageURI(imageUri);
        }
    }


}

class AdCount {
    int Total;

    public AdCount(int n) {
        this.Total = n;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}

class Title {
    String title;

    public Title(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

class UrlClass {
    String url;

    public UrlClass(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

class AdDetails {
    String title, question, option1, option2, option3, option4, correctAnswer, imageUrl;


    public AdDetails(String title, String question, String option1, String option2, String option3, String option4, String correctAnswer, String imageUrl) {
        this.title = title;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}