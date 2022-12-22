package com.myapps.majorprojectversion5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WebViewAd extends AppCompatActivity {

    WebView webView;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_ad);
        webView = findViewById(R.id.webView);

        String title = getIntent().getStringExtra("title");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AdsData/" + title + "/imageUrl");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    imageUrl = snapshot.getValue(String.class);
//                    Toast.makeText(getApplicationContext(), imageUrl, Toast.LENGTH_LONG).show();
                    webView.loadUrl(imageUrl);

                    String data = "<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width=" + "100%" + ", initial-scale=0.65 \" /></head>";
                    data = data + "<body><center><img width=\"" + "100%" + "\" src=\"" + imageUrl + "\" /></center></body></html>";
                    webView.loadData(data, "text/html", null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), TestAfterAd.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            intent.putExtras(bundle);
            startActivity(intent);
        }, 10000L);

    }

    @Override
    public void onBackPressed() {

    }
}