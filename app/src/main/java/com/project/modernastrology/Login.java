package com.project.modernastrology;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.modernastrology.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity implements View.OnClickListener{

    ActivityLoginBinding binding;
    String phone;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences("MODERN_ASTROLOGY", MODE_PRIVATE);
        editor = preferences.edit();
        phone = "";
        binding.send.setOnClickListener(this);
        binding.textView.setOnClickListener(this);
        try{
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo == null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Date Connection Required")
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(preferences.getString("PHONE", "").isEmpty())
            Toast.makeText(this, R.string.validnumber, Toast.LENGTH_SHORT).show();
        else if(!preferences.getString("PHONE", "").isEmpty())
        {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.send:
            {
                if(binding.phone.getText().toString().isEmpty())
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setMessage("Phone Number Cannot be Blank")
                            .setCancelable(false)
                            .setPositiveButton("OK", null);
                    alertDialog.show();
                }
                else if(binding.phone.getText().toString().length()!=10)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setMessage("Phone Number Cannot be Greater or Less than 10 Digits")
                            .setCancelable(false)
                            .setPositiveButton("OK", null);
                    alertDialog.show();
                }
                else
                {
                   if(new Database(this).register(binding.name.getText().toString(), binding.phone.getText().toString()))
                   {
                       editor.putString("PHONE", binding.phone.getText().toString());
                       editor.putString("NAME", binding.name.getText().toString());
                       editor.apply();
                       Intent intent = new Intent(Login.this, MainActivity.class);
                       startActivity(intent);
                   }
                   else
                       Toast.makeText(Login.this, "Failed", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.textView:
            {
                try{
                    Intent intent = new Intent(Login.this, Channel.class);
                    startActivity(intent);
                }
                catch(Exception e)
                {}
                break;
            }
        }
    }
}