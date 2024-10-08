
package com.example.myproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myproject.R;

public class MainActivity extends AppCompatActivity {
    /**
     * The method runs on create. It controls the whole activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quit = findViewById(R.id.quit);
        Button startButton = findViewById(R.id.start);

        quit.setOnClickListener(v -> finish());

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}