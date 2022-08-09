package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;

import com.example.myapplicationtest.databinding.ActivityTipBinding;

public class TipActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityTipBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);
        binding = ActivityTipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}