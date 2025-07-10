package com.example.tanks46.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tanks46.R;
import com.example.tanks46.view.GameView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView; // Declare GameView as a class variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton shootButton = findViewById(R.id.myButton);
        gameView = findViewById(R.id.gameView); // Initialize the GameView

        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call shoot method on the GameView
                gameView.shoot();
            }
        });
    }
}
