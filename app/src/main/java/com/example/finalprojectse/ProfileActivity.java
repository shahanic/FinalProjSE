package com.example.finalprojectse;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Dummy data (replace with actual data from database)
        String username = "JohnDoe";
        String email = "johndoe@example.com";
        String address = "123 Main St, City, Country";
        String maritalStatus = "Single";
        int age = 30;
        String birthday = "January 1, 1990";
        String allergies = "Peanuts, Shellfish";

        // Set user details
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(username);

        TextView emailTextView = findViewById(R.id.emailTextView);
        emailTextView.setText(email);

        TextView addressTextView = findViewById(R.id.addressTextView);
        addressTextView.setText(address);

        TextView maritalStatusTextView = findViewById(R.id.maritalStatusTextView);
        maritalStatusTextView.setText(maritalStatus);

        TextView ageTextView = findViewById(R.id.ageTextView);
        ageTextView.setText(String.valueOf(age));

        TextView birthdayTextView = findViewById(R.id.birthdayTextView);
        birthdayTextView.setText(birthday);

        TextView allergiesTextView = findViewById(R.id.allergiesTextView);
        allergiesTextView.setText(allergies);

        // Set profile picture (dummy image)
        ImageView profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setImageResource(R.drawable.baseline_person_24);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected (MenuItem item){
                if (item.getItemId() == R.id.navigation_profile) {
                    // Start ProfileActivity when Profile item is clicked

                    return true;

                } else if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    // Handle Home item click
                    return true;
                } else if (item.getItemId() == R.id.navigation_contacts) {
                    startActivity(new Intent(ProfileActivity.this, ContactsActivity.class));
                    // Handle Settings item click
                    return true;
                }
                // Add conditions for other BottomNavigationView items if needed
                return false;
            }
        });
    }
}