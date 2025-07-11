package com.example.tanks46.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tanks46.R;
import com.example.tanks46.view.GameView;

public class MainActivity extends AppCompatActivity {
    private ImageButton shootButton;
    private ImageView[] bulletViews;
    private int currentAmmo = 6;
    private int maxAmmo = 6;
    private boolean isReloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shootButton = findViewById(R.id.myButton);
        bulletViews = new ImageView[] {
                findViewById(R.id.bullet1),
                findViewById(R.id.bullet2),
                findViewById(R.id.bullet3),
                findViewById(R.id.bullet4),
                findViewById(R.id.bullet5),
                findViewById(R.id.bullet6)
        };
        GameView gameView = findViewById(R.id.gameView);

        updateAmmoDisplay();

        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAmmo > 0) {
                    gameView.shoot();
                    currentAmmo--;
                    updateAmmoDisplay();
                }

                if (currentAmmo <= 0 && !isReloading) {
                    isReloading = true;
                    shootButton.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentAmmo = maxAmmo;
                            updateAmmoDisplay();
                            isReloading = false;
                            shootButton.setEnabled(true);
                        }
                    }, 1500);
                }
            }
        });
    }

    private void updateAmmoDisplay() {
        for (int i = 0; i < bulletViews.length; i++) {
            bulletViews[i].setVisibility(i < currentAmmo ? View.VISIBLE : View.INVISIBLE);
        }
    }
}