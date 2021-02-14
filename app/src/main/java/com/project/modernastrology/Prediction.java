package com.project.modernastrology;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Prediction extends AppCompatActivity {

    ListView prediction;
    Database database;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        prediction = findViewById(R.id.list_item);
        bottomNavigationView = findViewById(R.id.bottom);
        progressDialog = new ProgressDialog(Prediction.this);
        database = new Database(this);
        sharedPreferences = getSharedPreferences("MODERN_ASTROLOGY", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove("WAIT");
        editor.apply();
        progressDialog.setMessage("Loading Data");
        database.read(prediction, sharedPreferences.getString("PHONE", ""), progressDialog);
        bottomNavigationView.setSelectedItemId(R.id.message);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.home:
                    {
                        Intent intent = new Intent(Prediction.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.signout:
                    {
                        Toast.makeText(Prediction.this, "Signed Out", Toast.LENGTH_SHORT).show();
                        editor.remove("PHONE");
                        editor.apply();
                        Intent intent = new Intent(Prediction.this, Login.class);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
        progressDialog.dismiss();
    }
}