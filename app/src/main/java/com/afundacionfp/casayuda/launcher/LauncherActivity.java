package com.afundacionfp.casayuda.launcher;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import com.afundacionfp.casayuda.screens.request.YourRequestsActivity;
import com.afundacionfp.casayuda.screens.login.LoginActivity;
import com.afundacionfp.casayuda.screens.nearbyworker.NearbyWorkersActivity;


public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is the LauncherActivity.
        // It is used to dynamically decide what activity should be first presented to the user.
        // You can alter the following lines to easily test the new activities/interfaces that you'll be creating this sprint.

       //Contralamos en caso de que ya se haya logueado lanzar la actividad correspondiente
        //según el boolean isWorker
        //en caso contrario se lanza la actividad de login
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        if(sharedPref.getString("token", null) != null){
            if (!sharedPref.getBoolean("is_worker", false)) {
                Intent intent = new Intent(this, NearbyWorkersActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, YourRequestsActivity.class);
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
}
