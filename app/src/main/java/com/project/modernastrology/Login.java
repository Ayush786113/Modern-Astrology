package com.project.modernastrology;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.modernastrology.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

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
                        .setCancelable(false);
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
//        user = auth.getCurrentUser();
//        if(user != null)
//        {
//            Intent intent = new Intent(Login.this, MainActivity.class);
//            intent.putExtra("UserInfo", user);
//            startActivity(intent);
//            new Database().setUser(user);
//        }
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
                    editor.putString("PHONE", binding.phone.getText().toString());
                    editor.apply();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
//                if(!binding.phone.getText().toString().isEmpty())
//                {
//                    editor.putString("PHONE", binding.phone.getText().toString());
//                    editor.apply();
//                    Intent intent = new Intent(Login.this, MainActivity.class);
//                    startActivity(intent);
//                }
//                else
//                {
//
//                }
//                try{
//                    progressDialog.show();
//                    options = PhoneAuthOptions.newBuilder(auth)
//                            .setPhoneNumber(binding.phone.getText().toString())
//                            .setTimeout((long) 120, TimeUnit.SECONDS)
//                            .setActivity(this)
//                            .setCallbacks(callbacks)
//                            .build();
//                    PhoneAuthProvider.verifyPhoneNumber(options);
//                }
//                catch(Exception e)
//                {
//                    System.out.println(e);
//                    Toast.makeText(Login.this, R.string.validnumber, Toast.LENGTH_SHORT).show();
//                }
//                progressDialog.dismiss();
                break;
            }


//                try{
//                    otpCode = binding.otp.getText().toString();
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otpCode);
//                    signIn(credential);
//                }
//                catch (Exception e)
//                {
//                    System.out.println(e);
//                    Toast.makeText(Login.this, "Error in Authentiicatio", Toast.LENGTH_SHORT).show();
//                }


        }
    }

    //    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            Toast.makeText(Login.this, R.string.success, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(Login.this, R.string.fail, Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            Toast.makeText(Login.this, "Code Sent", Toast.LENGTH_SHORT).show();
//            verificationCode = s;
//        }
//    };

//    private void signIn(PhoneAuthCredential phoneAuthCredential)
//    {
//        auth.signInWithCredential(phoneAuthCredential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                      if(task.isSuccessful() && task.isComplete())
//                      {
//                          Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
//                          user = task.getResult().getUser();
//                          new Database().setUser(user);
//                          System.out.println(user);
//                          Intent intent = new Intent(Login.this, MainActivity.class);
//                          intent.putExtra("UserInfo", user);
//                          startActivity(intent);
//                      }
//                      else
//                          Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}