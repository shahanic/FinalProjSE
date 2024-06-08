package com.example.finalprojectse;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class EmergencysmsActivity extends AppCompatActivity {
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 1011;

    private Button emergencyButton;
    private Location location;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencysms);

        emergencyButton = findViewById(R.id.but_em);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionsIfNeeded();
            }
        });
    }

    private void requestPermissionsIfNeeded() {
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        // Check and request SMS permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.SEND_SMS);
        }

        // Check and request contacts permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_CONTACTS);
        }

        if (!permissionsToRequest.isEmpty()) {
            String[] permissionsArray = permissionsToRequest.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissionsArray, MY_PERMISSIONS_REQUEST_CODE);
        } else {
            // All permissions granted, proceed with app functionality
            getLocationAndPickContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with functionality
                getLocationAndPickContact();
            } else {
                // Handle permission denied scenarios
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissions[0])) {
                        showPermissionRationaleDialog(permissions);
                    } else {
                        Toast.makeText(this, "Please enable permissions in Settings", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void showPermissionRationaleDialog(final String[] permissions) {
        new AlertDialog.Builder(EmergencysmsActivity.this  )
                .setMessage("These permissions are mandatory for the app to function properly. Please allow them.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(permissions, MY_PERMISSIONS_REQUEST_CODE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create().show();
    }

    private void getLocationAndPickContact() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    location = task.getResult();
                    pickContact();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to get location", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle location permission not granted case
        }
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                cursor.close();

                showConfirmDialog(number);
            }
        }
    }

    private void showConfirmDialog(String number) {
        if (location != null) {
            // Generate Google Maps link
            String googleMapsLink = "https://www.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();

            // Create SMS message with Google Maps link
            String message = "I AM IN AN EMERGENCY, PLEASE HELP!\nMy Location: " +
                    "\nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() +
                    "\nGoogle Maps Link: " + googleMapsLink;

            // Show confirmation dialog with the message content
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Emergency Message")
                    .setMessage("Send the following message to " + number + "?\n\n" + message)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendEmergencySMS(number, message);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            // Handle location not available
            Toast.makeText(getApplicationContext(), "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmergencySMS(String number, String message) {
        // Send SMS to the selected contact
        try {
            SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
            Log.d("SMS", "SMS sent to: " + number);
            Toast.makeText(getApplicationContext(), "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SMS", "Failed to send SMS to: " + number, e);
        }
    }
}
