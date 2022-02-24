package com.afundacionfp.casayuda.screens.nearbyworker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.ObjectSelfEmployedDto;
import com.afundacionfp.casayuda.client.handlers.GetNearbyWorkersHandler;
import com.afundacionfp.casayuda.screens.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * NearbyWorkersActivity class, manage this screen. It has a RecyclerView that shows all workers near phone location.
 * When a Worker is clicked, initialize WorkerDetailActivity
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class NearbyWorkersActivity extends AppCompatActivity implements LocationListener {

    // Location params
    protected LocationManager locationManager;
    protected double latitude;
    protected double longitude;

    // XML params
    private ProgressBar progressBar;
    private TextView progressText, nearbyWorkersTitle, workerName, workerPrice, workerServices;
    private ImageView bg1, bg2;
    private RecyclerView nearbyWorkersRecyclerView;

    // Aux params to spinner and toggle visible
    int i = 0;
    private boolean visible = true;

    /**
     * Before starting main workflow, we ask and check for location perms, if user doesnt provided it, return to another activity.
     * onCreate, lets show the steps for the main workflow Activity:
     * 1. Start location manager and check permissions
     * 2. Get all XML params with findById
     * 3. Vanish recyclerview and make visible loading spinner
     * 4. Get location (async, some ms...)
     * 5. After location is taken on onLocationChanged(), stop location manager and start getNearbyWorkers().
     * 6. If the request is OK, show recycler view and vanish loading spinner
     * 6*. If the request failed, show User "Error ***, try again later..." through loading spinner text.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_workers);
        getSupportActionBar().hide();

        // Launcher perms (Used when Android ask location perms
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    Intent intent;
                    if(isGranted) {
                        // We have perms, reload activity
                        intent = new Intent(this, NearbyWorkersActivity.class);
                    } else {
                        // User dont want to let us get the location, return to other activity
                        intent = new Intent(this, LoginActivity.class);
                    }
                    startActivity(intent);
                });

        // Before start the activity workflow, check if user have right perms.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // User have perms, so start main activity workflow
            mainActivityWorkflow();

            // Ask user if we could get the permissions.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected.
            askUserForPerms(requestPermissionLauncher);
        } else {
            // Ask user for permissions.
            // The registered ActivityResultCallback gets the result of this request.
            requestPerms(requestPermissionLauncher);
        }
    }

    /**
     * Request perms through Android activity result launcher
     * @param resultLauncher
     * @author Javier Miralles Rancaño
     */
    private void requestPerms(ActivityResultLauncher<String> resultLauncher) {
        resultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Dialog, asking for location perms
     * @param resultLauncher
     * @author Javier Miralles Rancaño
     */
    private void askUserForPerms(ActivityResultLauncher<String> resultLauncher) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("We need your location! ¿Could we get it?");
        builder1.setCancelable(true);

        // User want to give us perms, redirect to Android´s ask permission.
        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    requestPerms(resultLauncher);
                });

        // User dont want perms, redirect to another activity
        builder1.setNegativeButton(
                "No",
                (dialog, id) -> {
                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * Main activity workflow
     * @author Javier Miralles Rancaño
     */
    private void mainActivityWorkflow() {
        // Main workflow
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        nearbyWorkersTitle = findViewById(R.id.nearby_workers_title);
        workerName = findViewById(R.id.worker_name);
        workerPrice = findViewById(R.id.worker_price);
        workerServices = findViewById(R.id.worker_services);
        bg1 = findViewById(R.id.nearby_workers_bg1);
        bg2 = findViewById(R.id.nearby_workers_bg2);
        nearbyWorkersRecyclerView = findViewById(R.id.nearbyWorkersRecyclerView);
        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);

        toggleVisible();
        getLocation();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= 100) {
                    progressText.setText("" + i + "%");
                    progressBar.setProgress(i);
                    i++;
                    handler.postDelayed(this, 35);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        }, 50);
    }

    /**
     * Toggle XML params thanks to a auxiliar boolean
     * @author Javier Miralles Rancaño
     */
    private void toggleVisible() {
        if(visible) {
            nearbyWorkersTitle.setVisibility(View.GONE);
            workerName.setVisibility(View.GONE);
            workerPrice.setVisibility(View.GONE);
            workerServices.setVisibility(View.GONE);
            bg1.setVisibility(View.GONE);
            bg2.setVisibility(View.GONE);
            nearbyWorkersRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
        } else {
            nearbyWorkersTitle.setVisibility(View.VISIBLE);
            workerName.setVisibility(View.VISIBLE);
            workerPrice.setVisibility(View.VISIBLE);
            workerServices.setVisibility(View.VISIBLE);
            bg1.setVisibility(View.VISIBLE);
            bg2.setVisibility(View.VISIBLE);
            nearbyWorkersRecyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
        }
        visible = !visible;
    }

    /**
     * After getNearbyWorkers() failed, show user that request failed...
     * @param statusCode Error code from request
     * @author Javier Miralles Rancaño
     */
    @SuppressLint("SetTextI18n")
    private void cannotGetNearbyWorkers(int statusCode) {
        progressBar.setVisibility(View.GONE);
        progressText.setText("Error " + statusCode + ", try again later...");
    }

    /**
     * After getNearbyWorkers(), initialize Recycler View to show nearby workers.
     * @param list Workers list from getNearbyWorkers()
     * @author Javier Miralles Rancaño
     */
    private void initializeRecyclerView(List<ObjectSelfEmployedDto> list) {
        toggleVisible();
        // RecyclerView Initialization.
        List<NearbyWorkersEntryData> dataSet = new ArrayList<>();
        list.forEach(worker ->
                dataSet.add(new NearbyWorkersEntryData(worker.getUserId(), worker.getName(), String.valueOf(worker.getPricePerHour()), worker.getJobs())));
        NearbyWorkersEntryRecyclerViewAdapter adapter =
                new NearbyWorkersEntryRecyclerViewAdapter(dataSet, this);
        nearbyWorkersRecyclerView.setAdapter(adapter);
        nearbyWorkersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * Make a request to requestGetWorkers with lat, long from the location with 20k meters radius and hardcoded token.
     * If request fail -> cannotGetNearbyWorkers()
     * If request OK -> initializeRecyclerView()
     * @author Javier Miralles Rancaño
     */
    private void getNearbyWorkers() {
        RestClient.getInstance(getApplicationContext()).getNearbyWorkers(latitude, longitude, 20000,  new GetNearbyWorkersHandler() {
            @Override
            public void WorkersRequestDidComplete(List<ObjectSelfEmployedDto> response) {
                initializeRecyclerView(response);
            }

            @Override
            public void requestDidFail(int statusCode) {
                cannotGetNearbyWorkers(statusCode);
            }
        });
    }

    /**
     * Stop location manager from listen to current location
     * @author Javier Miralles Rancaño
     */
    private void stopLocationManager() {
        locationManager.removeUpdates(this);
    }

    /**
     * Initialize location manager to get location, overriding location listener methods.
     * @author Javier Miralles Rancaño
     */
    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if(latitude!=0) {
            stopLocationManager();
            getNearbyWorkers();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}