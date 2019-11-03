package com.developer.ipphoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class FullScreenImage extends AppCompatActivity {
    ImageView fullphoto;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        username = findViewById(R.id.textView2);
        fullphoto = findViewById(R.id.imageView2);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("picture_url");
        String valueusername = extras.getString("username");

        username.setText(valueusername);

        Glide
                .with(FullScreenImage.this)
                .load(value)
                .placeholder(R.drawable.giphy)
                .into(fullphoto);
    }
}
