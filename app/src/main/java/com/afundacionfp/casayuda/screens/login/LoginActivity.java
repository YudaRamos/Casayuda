package com.afundacionfp.casayuda.screens.login;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;
import com.afundacionfp.casayuda.client.handlers.LoginResponseHandler;
import com.afundacionfp.casayuda.screens.nearbyworker.NearbyWorkersActivity;
import com.afundacionfp.casayuda.screens.register.RegisterActivity;
import com.afundacionfp.casayuda.screens.request.YourRequestsActivity;
import com.afundacionfp.casayuda.screens.videoactivity.VideoActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText email;
    private TextInputEditText password;
    private LinearProgressIndicator progressBar;
    private Button loginButton, forgotPasswordButton;
    //IDs necesarias para los métodos onClickApp_1() y onClickApp_2().
    final String appId_1 = "com.nyrds.pixeldungeon.ml",
            appId_2 = "com.ustwo.monumentvalley";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editText_LoginEmail);
        password = findViewById(R.id.editText_LoginPassword);
        loginButton = findViewById(R.id.login);
        forgotPasswordButton = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.linearProgress);
        progressBar.setVisibility(View.GONE);
        // Restores variables if theres a savedInstanceState Bundle.
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        loginButton.setOnClickListener(v -> {
            String sPassword = password.getText().toString();
            String sEmail = email.getText().toString();
            if(!sEmail.matches("") && !sPassword.matches("")) {
                loginRequest(email.getText().toString(), password.getText().toString());
            } else {
                Snackbar.make(loginButton, "Fill both fields, please.", Snackbar.LENGTH_SHORT).show();
            }
        });

        forgotPasswordButton.setOnClickListener(v -> {
            Snackbar.make(loginButton, "This feature is not implemented yet. Please contact our support team.", Snackbar.LENGTH_SHORT).show();
        });
    }

    public void loginRequest(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        RestClient.getInstance(getApplicationContext()).postUserLogin(email, password, new LoginResponseHandler() {
            @Override
            public void sessionsRequestDidComplete(LoginUserBodyResponseDto dto) {

                // Redirigir a la Activity adecuada en función del tipo de perfil

                Intent intent;
                if (dto.isIs_worker()) {
                    intent = new Intent(LoginActivity.this, YourRequestsActivity.class);

                } else {
                    intent = new Intent(LoginActivity.this, NearbyWorkersActivity.class);
                }
                startActivity(intent);
            }

            @Override
            public void requestDidFail(int statusCode) {
                if(statusCode==401) {
                    Snackbar.make(loginButton, "Email or password are incorrect!", Snackbar.LENGTH_SHORT).setAction("RETRY", view -> {
                        loginRequest(email, password);
                    }).show();
                } else {
                    Snackbar.make(loginButton, "Something went wrong...", Snackbar.LENGTH_SHORT).setAction("RETRY", view -> {
                        loginRequest(email, password);
                    }).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acerca_de:
                System.out.println("hola");
                showDialog();
                return true;
            case R.id.videoView:
                Intent vid = new Intent(LoginActivity.this, VideoActivity.class);
                startActivity(vid);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Crea y muestra una nueva instancia de la clase LA_AboutDialog como un DialogFragment en pantalla.
     */
    private void showDialog() {

        FragmentManager fragmentManager = getFragmentManager();
        LA_AboutDialog aboutDialog = LA_AboutDialog.newInstance("About");
        aboutDialog.setCancelable(true); //Permite cerrar el dialog al cliquear fuera de este.
        aboutDialog.show(fragmentManager, "Fragment");
    }

    /**
     * Abre un enlace a una aplicación "App 1" de la PlayStore, está destinado a ser invocado mediante un button.
     *
     * @param view La variable necesaria para asignarlo a un button.
     */
    public void onClickApp_1(View view) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId_1)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appId_1)));
        }
    }

    /**
     * Abre un enlace a una aplicación "App 2" de la PlayStore, está destinado a ser invocado mediante un button.
     *
     * @param view La variable necesaria para asignarlo a un button.
     */
    public void onClickApp_2(View view) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId_2)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appId_2)));
        }
    }


    // Instance state saving and restore.
    @Override
    // Save Bundle.
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("email", email.getText().toString());
        savedInstanceState.putString("password", password.getText().toString());
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    // Restore Bundle.
    public void onRestoreInstanceState(Bundle savedInstanceBundle) {
        super.onRestoreInstanceState(savedInstanceBundle);
        email.setText(savedInstanceBundle.getString("email"));
        password.setText(savedInstanceBundle.getString("password"));

    }


}

