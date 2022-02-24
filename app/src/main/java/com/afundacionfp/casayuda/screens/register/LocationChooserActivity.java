package com.afundacionfp.casayuda.screens.register;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.afundacionfp.casayuda.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationChooserActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    // Starts pointing to A Coruña coordinates.
    private static double latitude = 43.3715 , longitude = -8.39597;
    private LatLng currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chooser);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // Resources.
        Button acceptButton = findViewById(R.id.accept_button);
        /**
         * When the "Accept" button is clicked, the coordinates from the marker are retrieved,
         * and sent back to the activity from which it was instantiated.
         */
        acceptButton.setOnClickListener(v -> {
            latitude = currentLocation.latitude;
            longitude = currentLocation.longitude;
            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker with a default location in A Coruña, Spain.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in current coordinates
        currentLocation = new LatLng(latitude, longitude);
        // Marker is draggable so the user can choose a new position.
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Select your location").
                draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        // Updates marker actual position.
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                currentLocation = marker.getPosition();
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
    }

}