package com.project.modernastrology;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;
    private boolean result;
    private SharedPreferences preferences;


    Database() {
    }

    Database(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public boolean register(String name, String phone)
    {
        databaseReference.child("Users").child(phone).setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful() && task.isComplete()) {
                    result = true;
                }
            }
        });
        return true;
    }

    public void write(String name, String date, String time, String b_place, String c_place, String phone, String booking, String appointment) {
        User user = new User();
        user.setName(name);
        user.setDate(date);
        user.setTime(time);
        user.setB_place(b_place);
        user.setC_place(c_place);
        user.setPhone(phone);
        user.setBooking(booking);
        user.setAppointment(appointment);
        databaseReference.child("User Data").child(phone).push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful() && task.isComplete())
                    Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
