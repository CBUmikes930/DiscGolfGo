package com.steinbock.discgolfgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

        findViewById(R.id.back_button).setOnClickListener(view -> {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.flFragment);
            if (f == null || f.getTag() == null)
                return;

            switch (f.getTag()) {
                case "ADD_TO_BAG":
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, bagFragment, "BAG").commit();
                    return;
                default:
                    // Hide the back button
                    findViewById(R.id.back_button).setVisibility(View.GONE);
                    // Switch the fragment to the home fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment, "HOME").commit();
                    // Select the "Home" menu option
                    ((BottomNavigationView) findViewById(R.id.bottomNavView)).setSelectedItemId(R.id.home);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        switch (item.getItemId()) {
            case R.id.home:
                System.out.println("Home");
                findViewById(R.id.back_button).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment, "HOME").commit();
                return true;

            case R.id.bag:
                System.out.println("Bag");
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, bagFragment, "BAG").commit();
                return true;

            case R.id.courses:
                System.out.println("Courses");
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, courseFragment, "COURSE").commit();
                return true;

            case R.id.settings:
                System.out.println("Settings");
                return true;
        }
        return false;
    }
}