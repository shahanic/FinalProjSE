package com.example.finalprojectse;

import static com.example.finalprojectse.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.finalprojectse.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find the buttons
        Button emergencyButton = findViewById(R.id.emergencyButton);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);

        // Set click listeners
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle emergency button click
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 1 click
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 2 click
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 3 click
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 4 click
            }
        });


        // Setup bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    // Handle Home item click
                    return true;
                } else if (item.getItemId() == R.id.navigation_profile) {
                    // Start ProfileActivity when Profile item is clicked
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                    return true;
                } else if (item.getItemId() == id.navigation_contacts) {
                    startActivity(new Intent(HomeActivity.this, ContactsActivity.class));
                    // Handle Settings item click
                    return true;
                }
                // Add conditions for other BottomNavigationView items if needed
                return false;
            }
        });
    }
}

