package com.project.modernastrology;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Wait extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        sharedPreferences = getSharedPreferences("MODERN_ASTROLOGY", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!sharedPreferences.getBoolean("WAIT", false))
            startActivity(new Intent(Wait.this, Prediction.class));
        Handler handler = new Handler();
        try{
            handler.postDelayed(change, 10000); //10800000
        }
        catch (Exception e)
        {
            Toast.makeText(Wait.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable change = new Runnable() {
        @Override
        public void run() {
            editor.putBoolean("WAIT", false);
            editor.apply();
            Intent intent = new Intent(Wait.this, Prediction.class);
            startActivity(intent);
        }
    };
}