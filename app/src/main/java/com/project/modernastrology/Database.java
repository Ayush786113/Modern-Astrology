package com.project.modernastrology;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;
    //    String number;
//    FirebaseUser user;
    private SharedPreferences preferences;


    Database() {
    }

    Database(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void write(String name, String date, String time, String b_place, String c_place, String question, String request, String phone) {
        User user = new User();
        user.setName(name);
        user.setDate(date);
        user.setTime(time);
        user.setB_place(b_place);
        user.setC_place(c_place);
        user.setQuestion(question);
        user.setRequest(request);
        user.setPhone(phone);
        databaseReference.child("User Data").child(phone).push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful() && task.isComplete())
                    Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void read(ListView listView, String number, ProgressDialog progressDialog) {
        progressDialog.show();
        LinkedList<String> uid = new LinkedList<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        databaseReference.child("User Data").child(number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    uid.add(dataSnapshot.toString());
                    User user = dataSnapshot.getValue(User.class);
                    String data = "Question: " + user.getQuestion() + "\n Prediction: " + user.getPrediction();
                    list.add(data);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
