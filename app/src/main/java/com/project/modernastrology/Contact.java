package com.project.modernastrology;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.modernastrology.databinding.ActivityContactBinding;

public class Contact extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityContactBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String details = "A/c Name: Krishnamurti Academy of Modern Astrology and Spiritual Development \n \n A/c No.: 19620100017279 \n \n Bank Name: Bank of Baroda \n \n IFSC Code: BARB0CALBAN \n \n Phone - +91 9748588862 \n \n Send a screenshot of payment of Rs. 900 in the given Whatsapp number to the Astrologer";
        binding.details.setText(details);

        binding.bottomNavigationView.setSelectedItemId(R.id.contact);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences("MODERN_ASTROLOGY", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:
            {
                Intent intent = new Intent(Contact.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.signout:
            {
                Toast.makeText(Contact.this, "Signed Out", Toast.LENGTH_SHORT).show();
                editor.remove("PHONE");
                editor.remove("NAME");
                editor.apply();
                Intent intent = new Intent(Contact.this, Login.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}