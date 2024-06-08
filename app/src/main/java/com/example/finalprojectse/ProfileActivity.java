package com.example.finalprojectse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    TextView tvGreeting, tvUsername, tvEmail;
    Button btnLogout;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvGreeting = findViewById(R.id.textViewGreeting);
        tvUsername = findViewById(R.id.textViewUsername);
        tvEmail = findViewById(R.id.textViewEmail);
        btnLogout = findViewById(R.id.buttonLogout);
        db = new Database(this);

        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username","");


        tvGreeting.setText("Hello, " + username);

        if (username != null && !username.isEmpty()) {
            Cursor cursor = db.getUserInfo(username);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String dbEmail = cursor.getString(cursor.getColumnIndex("email"));
                    tvUsername.setText("Username: " + username);
                    tvEmail.setText("Email: " + dbEmail);
                } else {
                    tvUsername.setText("Username: not found");
                    tvEmail.setText("Email: not found");
                }
                cursor.close();
            } else {
                tvUsername.setText("Username: error");
                tvEmail.setText("Email: error");
            }
        }

        // Logout button functionality
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_profile) {
                    // Stay on ProfileActivity when Profile item is clicked
                    return true;
                } else if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_contacts) {
                    startActivity(new Intent(ProfileActivity.this, ContactsActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Set the selected item in the BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
    }
}
