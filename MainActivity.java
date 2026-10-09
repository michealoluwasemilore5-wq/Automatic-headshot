package com.example.ffheadshothack;

import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SeekBar sensitivityBar;
    private TextView sensitivityText;
    private Button startBtn, stopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        sensitivityBar = findViewById(R.id.sensitivityBar);
        sensitivityText = findViewById(R.id.sensitivityText);
        startBtn = findViewById(R.id.startBtn);
        stopBtn = findViewById(R.id.stopBtn);

        // Sensitivity Logic
        sensitivityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensitivityText.setText("Sensitivity: " + progress);
                // Save this value to SharedPreferences or pass it to the Service
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Start Service Button
        startBtn.setOnClickListener(v -> {
            if (!isAccessibilitySettingsOn()) {
                Toast.makeText(this, "Please enable Accessibility in Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            } else {
                startService(new Intent(this, HeadshotService.class));
                Toast.makeText(this, "Hack Started", Toast.LENGTH_SHORT).show();
            }
        });

        // Stop Service Button
        stopBtn.setOnClickListener(v -> {
            stopService(new Intent(this, HeadshotService.class));
            Toast.makeText(this, "Hack Stopped", Toast.LENGTH_SHORT).show();
        });
    }

    // Check if Accessibility is enabled
    private boolean isAccessibilitySettingsOn() {
        String serviceName = getPackageName() + "/" + HeadshotService.class.getName();
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(serviceName.toLowerCase());
            }
        }
        return false;
    }
}