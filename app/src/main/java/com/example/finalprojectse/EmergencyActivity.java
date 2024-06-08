package com.example.finalprojectse;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmergencyActivity extends AppCompatActivity {

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int REQUEST_SMS_PERMISSION = 2;

    private LocationManager locationManager;
    private double longitudeGPS, latitudeGPS;
    private TextView addressValue, formattedMessage;
    private Spinner emergencyTypeSpinner;
    private Button updateLocationButton, sendSmsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        addressValue = findViewById(R.id.addressValue);
        formattedMessage = findViewById(R.id.formattedMessage);
        emergencyTypeSpinner = findViewById(R.id.emergencyTypeSpinner);
        updateLocationButton = findViewById(R.id.updateLocationButton);
        sendSmsButton = findViewById(R.id.sendSmsButton);

        // Initialize the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.emergency_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emergencyTypeSpinner.setAdapter(adapter);

        // Request initial location update
        requestLocationUpdates();

        updateLocationButton.setOnClickListener(v -> {
            if (checkLocationPermissions()) {
                updateLocation();
            } else {
                requestLocationPermissions();
            }
        });

        sendSmsButton.setOnClickListener(v -> {
            if (checkLocationPermissions()) {
                sendSMSWithIntent();
            } else {
                requestLocationPermissions();
            }
        });
    }

    private void requestLocationUpdates() {
        if (checkLocationPermissions()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS);
        } else {
            requestLocationPermissions();
        }
    }

    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    private void updateLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
            updateAddressAndMessage(latitudeGPS, longitudeGPS);
        } else {
            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAddressAndMessage(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String addressText = "Unable to get address";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                addressText = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        addressValue.setText(addressText);

        // Get selected emergency type
        String emergencyType = emergencyTypeSpinner.getSelectedItem().toString();

        // Update formatted message with emergency type
        String formattedText = String.format("Need Help!  %s\nLocation: %s. \n https://www[dot]google[dot]com/maps?q=%f,%f",
                emergencyType, addressText, latitude, longitude);
        formattedMessage.setText(formattedText);
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    private void sendSMSWithIntent() {
        String message = formattedMessage.getText().toString();
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:")); // This ensures only SMS apps respond
        smsIntent.putExtra("sms_body", message);

        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        } else {
            Toast.makeText(this, "No SMS app found", Toast.LENGTH_SHORT).show();
        }
    }
}
