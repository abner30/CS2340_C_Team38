package com.example.myproject.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;

import com.example.myproject.R;

public class Logistics extends AppCompatActivity {

    /**
     * This method runs on create.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);
//        ViewCompat.setOnApplyWindowInsetsListener(
//        findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(
//            WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top,
//            systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}
