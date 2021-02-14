package com.project.modernastrology;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.modernastrology.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    String name, date, time, b_place, c_place, question, request, uid, phone;
    Database database;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.free.setOnClickListener(this);
        binding.paid.setOnClickListener(this);
        binding.question.setOnClickListener(this);
        binding.time.setOnClickListener(this);
        binding.date.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("MODERN_ASTROLOGY", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        database = new Database(this);
        try{
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo == null)
            {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setMessage("Date Connection Required")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .setCancelable(false);
                builder.create().show();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Internal Error, Report the Developer", Toast.LENGTH_LONG).show();
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.message:
                    {
                        Intent intent = new Intent(MainActivity.this, Prediction.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.signout:
                    {
                        Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                        editor.remove("PHONE");
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        Intent intent;
        boolean waitPage = sharedPreferences.getBoolean("WAIT", false);
        if(waitPage)
        {
            intent = new Intent(MainActivity.this, Wait.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.question:
            {
                question();
                break;
            }
            case R.id.date:
            {
                date();
                break;
            }
            case R.id.time:
            {
                time();
                break;
            }
            case R.id.free:
            {
                name = binding.name.getText().toString();
                date = binding.date.getText().toString();
                time = binding.time.getText().toString();
                b_place = binding.place.getText().toString();
                c_place = binding.presentplace.getText().toString();
                request = "Free";
//                uid = user.getUid();
                phone = sharedPreferences.getString("PHONE", null);
                if(!(name.isEmpty() && date.isEmpty() && time.isEmpty() && b_place.isEmpty() && c_place.isEmpty() && request.isEmpty() && phone.isEmpty()))
                {
                    try{
                        database.write(name, date, time, b_place, c_place, question, request, phone);
                        editor.putBoolean("WAIT", true);
                        Intent intent = new Intent(MainActivity.this, Wait.class);
                        startActivity(intent);
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Error in Database", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case R.id.paid:
            {
                if(!(name.isEmpty() && date.isEmpty() && time.isEmpty() && b_place.isEmpty() && c_place.isEmpty() && request.isEmpty() && phone.isEmpty())) {
                    try {
                        database.write(name, date, time, b_place, c_place, question, request, phone);
                        editor.putBoolean("WAIT", true);
                        Intent intent = new Intent(MainActivity.this, Wait.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error in Database", Toast.LENGTH_SHORT).show();
                    }
                }
                name = binding.name.getText().toString();
                date = binding.date.getText().toString();
                time = binding.time.getText().toString();
                b_place = binding.place.getText().toString();
                c_place = binding.presentplace.getText().toString();
                request = "Paid";
//                uid = user.getUid();
                phone = sharedPreferences.getString("PHONE", null);
                pay();
                break;
            }
        }
        editor.commit();
    }

    void date()
    {
        MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText(getString(R.string.select_date));
        MaterialDatePicker materialDatePicker = dateBuilder.build();
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.date.setText(materialDatePicker.getHeaderText());
            }
        });
    }
    void time()
    {
        MaterialTimePicker.Builder timeBuilder = new MaterialTimePicker.Builder();
        timeBuilder.setTitleText(getString(R.string.select_time));
        timeBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
        MaterialTimePicker materialTimePicker = timeBuilder.build();
        materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = materialTimePicker.getHour()+":"+materialTimePicker.getMinute();
                binding.time.setText(time);
            }
        });
    }
    void question()
    {
        View view = getLayoutInflater().inflate(R.layout.question, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle(getString(R.string.question))
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText ques = view.findViewById(R.id.questiontext);
                        question = ques.getText().toString();
                    }
                });
        builder.show();
    }
    void pay()
    {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "")
                .appendQueryParameter("pn", "")
                .appendQueryParameter("tn", "Modern Astrology - Paid Preddiction Request")
                .appendQueryParameter("am", "")
                .appendQueryParameter("cu", "INR")
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        Intent chooser = Intent.createChooser(intent, "Select a Payment Method");
        if(chooser.resolveActivity(getPackageManager()) != null)
            startActivityForResult(chooser, 1);
        else
            Toast.makeText(MainActivity.this, R.string.noapp, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            if(data!=null)
                Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
            try{
                database.write(name, date, time, b_place, c_place, question, request, phone);
                editor.putBoolean("WAIT", true);
                Intent intent = new Intent(MainActivity.this, Wait.class);
                startActivity(intent);
            }
            catch(Exception e)
            {
                Toast.makeText(MainActivity.this, "Error in Database", Toast.LENGTH_SHORT).show();
            }
        }
    }
}