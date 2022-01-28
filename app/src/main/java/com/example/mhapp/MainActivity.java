package com.example.mhapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mhapp.services.NotificationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Главная активити, создающая навигационную панель
 */
public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_DATE = "date";

    private UserProfileData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            userData = (UserProfileData)arguments.getSerializable("user_data");
        }

        BottomNavigationView navigationView = findViewById(R.id.navigation_view);

        // хранит контейнер и список фрагментов для переключения
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // создает панель навигации
        NavigationUI.setupWithNavController(navigationView, navController);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodRation.class);
                intent.putExtra("user_data", userData);
                intent.putExtra("current_date", sharedPreferences.getString(APP_PREFERENCES_DATE, ""));
                startActivity(intent);
            }
        });

        Intent intent = new Intent(MainActivity.this, NotificationService.class);
        intent.putExtra("user_data", userData);
        startService(intent);
    }
}