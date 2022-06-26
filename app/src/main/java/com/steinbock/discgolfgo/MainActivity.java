package com.steinbock.discgolfgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity
    implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView botNavView;

    HomeFragment homeFragment = new HomeFragment();
    BagFragment bagFragment = new BagFragment();
    CourseFragment courseFragment = new CourseFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.back_button).setVisibility(View.GONE);

        botNavView = findViewById(R.id.bottomNavView);
        botNavView.setOnItemSelectedListener(this);
        botNavView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        findViewById(R.id.back_button).setVisibility(View.GONE);

        switch (item.getItemId()) {
            case R.id.home:
                System.out.println("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                return true;

            case R.id.bag:
                System.out.println("Bag");
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, bagFragment).commit();
                return true;

            case R.id.courses:
                System.out.println("Courses");
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, courseFragment).commit();
                return true;

            case R.id.settings:
                System.out.println("Settings");
                return true;
        }
        return false;
    }
}