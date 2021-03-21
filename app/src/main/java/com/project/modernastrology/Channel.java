package com.project.modernastrology;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.project.modernastrology.databinding.ActivityChannelBinding;

public class Channel extends AppCompatActivity {

    ActivityChannelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChannelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try{
            binding.webview.setWebViewClient(new WebViewClient());
            binding.webview.loadUrl("https://youtube.com/c/ModernAstrology");
            WebSettings settings = binding.webview.getSettings();
            settings.setJavaScriptEnabled(true);
        }
        catch (Exception e)
        {}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(binding.webview.canGoBack())
            binding.webview.goBack();
        else
            startActivity(new Intent(Channel.this, Login.class));
    }
}