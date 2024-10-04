package com.example.myproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.myproject.R;

import com.example.myproject.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quit = findViewById(R.id.quit);

        quit.setOnClickListener(v -> finish());
    }
}