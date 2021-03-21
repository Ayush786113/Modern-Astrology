package com.project.modernastrology;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.project.modernastrology.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    String name, date, time, b_place, c_place, question, request, apptime, phone, booking, appointment;
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
        binding.question.setOnClickListener(this);
        binding.time.setOnClickListener(this);
        binding.date.setOnClickListener(this);
        binding.bookingmode.setOnClickListener(this);
        binding.heading.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("MODERN_ASTROLOGY", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        phone = sharedPreferences.getString("PHONE", null);
        database = new Database(this);
        try{
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo == null)
            {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setMessage("Data Connection Required")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .setCancelable(true);
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
                    case R.id.signout:
                    {
                        Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                        editor.remove("PHONE");
                        editor.remove("NAME");
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.contact:
                    {
                        Intent intent = new Intent(MainActivity.this, Contact.class);
                        startActivity(intent);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.name.setText(sharedPreferences.getString("NAME", null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.question:
            {
                appoinmetnt();
                break;
            }
            case R.id.date:
            {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        date();
                    }
                };
                runOnUiThread(run);
                break;
            }
            case R.id.time:
            {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        time();
                    }
                };
                runOnUiThread(run);
                break;
            }
            case R.id.bookingmode:
            {
                bookingMode();
                break;
            }
            case R.id.heading:
            {
                try{
                    Intent intent = new Intent(MainActivity.this, Channel.class);
                    startActivity(intent);
                }
                catch(Exception e)
                {}
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
        name = binding.name.getText().toString();
        date = binding.date.getText().toString();
        time = binding.time.getText().toString();
        b_place = binding.place.getText().toString();
        c_place = binding.presentplace.getText().toString();
        try{
            database.write(name, date, time, b_place, c_place, phone, booking, appointment);
            Intent intent = new Intent(MainActivity.this, Contact.class);
            startActivity(intent);
        }
        catch(Exception e)
        {
            Toast.makeText(MainActivity.this, "Error in Database", Toast.LENGTH_SHORT).show();
        }
//        Uri uri = Uri.parse("upi://pay").buildUpon()
//                .appendQueryParameter("pa", "whitelightofuniverse-1@okicici")
//                .appendQueryParameter("pn", "Modern Astrology")
//				.appendQueryParameter("mc", "")
//                .appendQueryParameter("tn", "Modern Astrology Booking")
//                .appendQueryParameter("am", "1")
//                .appendQueryParameter("cu", "INR")
//                .build();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        Intent chooser = Intent.createChooser(intent, "Select a Payment Method");
//        if(chooser.resolveActivity(getPackageManager()) != null)
//            startActivityForResult(chooser, 1);
//        else
//            Toast.makeText(MainActivity.this, R.string.noapp, Toast.LENGTH_SHORT).show();
    }

    void appTime()
    {
        MaterialTimePicker.Builder timeBuilder = new MaterialTimePicker.Builder();
        timeBuilder.setTitleText("Select Appointment Time");
        timeBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
        MaterialTimePicker materialTimePicker = timeBuilder.build();
        materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = materialTimePicker.getHour()+":"+materialTimePicker.getMinute();
                apptime = materialTimePicker.getHour()+":"+materialTimePicker.getMinute();
                appointment = appointment+" at "+apptime;
                alert();
            }
        });
    }

    void bookingMode()
    {
        View view = getLayoutInflater().inflate(R.layout.booking, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your preffered prediction mode")
                .setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MaterialRadioButton onPhone = view.findViewById(R.id.overthephone);
                        MaterialRadioButton inChamber = view.findViewById(R.id.inchamber);
                        if(onPhone.isChecked())
                            booking = "Over The Phone";
                        else if(inChamber.isChecked())
                            booking = "In Chamber";
                        pay();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        builder.show();
    }

    void alert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Important")
                .setMessage("The Astrologer will contact you to confirm your booking!")
                .setPositiveButton("Accept", null);
        builder.create().show();
    }

    void appoinmetnt()
    {
        Calendar calendar = Calendar.getInstance();
        CalendarConstraints.Builder calendarconstraintsbuilder = new CalendarConstraints.Builder();
        calendar.add(Calendar.DATE, 10);
        long date = calendar.getTimeInMillis();
        calendarconstraintsbuilder.setValidator(DateValidatorPointForward.from(date));
        MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText("Select Appointment Date");
        dateBuilder.setCalendarConstraints(calendarconstraintsbuilder.build());
        MaterialDatePicker materialDatePicker = dateBuilder.build();
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                appointment = materialDatePicker.getHeaderText();
                appTime();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            if(data!=null)
                Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
            name = binding.name.getText().toString();
            date = binding.date.getText().toString();
            time = binding.time.getText().toString();
            b_place = binding.place.getText().toString();
            c_place = binding.presentplace.getText().toString();
            try{
                database.write(name, date, time, b_place, c_place, phone, booking, appointment);
            }
            catch(Exception e)
            {
                Toast.makeText(MainActivity.this, "Error in Database", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(MainActivity.this, "Payment Failed", Toast.LENGTH_LONG).show();
    }
}