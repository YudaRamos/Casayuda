package com.afundacionfp.casayuda.screens.register;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.handlers.UserResponseHandler;
import com.afundacionfp.casayuda.screens.login.LoginActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText name;
    private TextInputEditText lastname;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private SwitchMaterial toggleButton;
    private TextInputEditText priceperhour;
    private TextInputLayout priceperhourLayout;
    private LinearProgressIndicator progressBar;
    private Button location;
    private Button multichoice;
    private Button register;
    private String p,sname,slastname,semail,spassword;
    private String cp;
    int price=0;
    String[] arrayProfesion=null;
    boolean isWorker=false;
    // 'cleaner', 'cooker', 'plumber', 'electrician', 'babysitter', 'caregiver'.

    String[] profesions_list = {"Cleaner", "Cooker", "Plumber", "Electrician", "Babysitter", "Caregiver"};
    ArrayList<String> profesionChecked=new ArrayList<>();
    private boolean[] isCheckedList = {false, false, false, false, false, false};
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        name = findViewById(R.id.editText_Name);
        lastname = findViewById(R.id.editText_Lastname);
        email = findViewById(R.id.editText_Email);
        password = findViewById(R.id.editText_Password);
        confirmPassword = findViewById(R.id.editText_ConfirmPassword);
        toggleButton = findViewById(R.id.toggleButton);
        priceperhour = findViewById(R.id.editText_PriceHour);
        priceperhourLayout = findViewById(R.id.price_hour);
        location = findViewById(R.id.location);
        multichoice = findViewById(R.id.multichoice);
        progressBar = findViewById(R.id.linearProgress);

        progressBar.setVisibility(View.INVISIBLE);
        toggleButton.setChecked(false);
        priceperhourLayout.setVisibility(View.INVISIBLE);
        priceperhour.setVisibility(View.INVISIBLE);
        location.setVisibility(View.INVISIBLE);
        multichoice.setVisibility(View.INVISIBLE);
        register = findViewById(R.id.register);

        // Restores variables if theres a savedInstanceState Bundle.
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        multichoice.setOnClickListener(view -> multiCheckBoxesDialog());
        toggleButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                priceperhourLayout.setVisibility(View.VISIBLE);
                priceperhour.setVisibility(View.VISIBLE);
                location.setVisibility(View.VISIBLE);
                multichoice.setVisibility(View.VISIBLE);
                isWorker=true;
            } else {
                priceperhourLayout.setVisibility(View.INVISIBLE);
                priceperhour.setVisibility(View.INVISIBLE);
                location.setVisibility(View.INVISIBLE);
                multichoice.setVisibility(View.INVISIBLE);
                isWorker=false;
            }
        });


        /**
         * LocationChooserActivity Launcher
         * Instantiates the activity and allows to retrieve back latitude and longitude
         * coordinates.
         */
        ActivityResultLauncher<Intent> mapLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent resultData = result.getData();
                        if (resultData != null) {
                            latitude = resultData.getDoubleExtra("latitude", 0);
                            longitude = resultData.getDoubleExtra("longitude", 0);
                        }
                    }
                }
        );

        location.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LocationChooserActivity.class);
            mapLauncher.launch(intent);
        });

        register.setOnClickListener(v -> {
            registerRequest();
        });

    }

    public void registerRequest() {
        Context context = getApplicationContext();
        p = password.getText().toString();
        cp = confirmPassword.getText().toString();
        sname=name.getText().toString();
        slastname=lastname.getText().toString();
        semail=email.getText().toString();
        spassword=password.getText().toString();
        if(checkBlanks()) {
            return;
        }
        if (!p.equals(cp)) {
            Snackbar.make(toggleButton, "Your password doesn´t match", Snackbar.LENGTH_SHORT).show();
            password.setText("");
            confirmPassword.setText("");
        }
        //Si es usuario_trabajador
        if(isWorker){
            price = Integer.parseInt(priceperhour.getText().toString());
            arrayProfesion = profesionChecked.toArray(new String[0]);
            for (String s : arrayProfesion) {
                s  = s.toLowerCase(Locale.ROOT);
            }
        }
        progressBar.setVisibility(View.VISIBLE);
        //lanzamos la petición de registro
        RestClient.getInstance(context).postUserRegister(name.getText().toString(), lastname.getText().toString(), email.getText().toString(),
                password.getText().toString(), arrayProfesion, price, longitude, latitude, new UserResponseHandler() {
                    @Override
                    public void sessionsRequestDidComplete() {
                        Snackbar.make(toggleButton, "Register successfully", Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void requestDidFail(int statusCode) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(toggleButton, "Something went wrong!", Snackbar.LENGTH_SHORT).setAction("RETRY", view -> {
                            registerRequest();
                        }).show();
                    }
                });
    }

    public boolean checkBlanks() {
        p = password.getText().toString();
        cp = confirmPassword.getText().toString();
        sname=name.getText().toString();
        slastname=lastname.getText().toString();
        semail=email.getText().toString();
        spassword=password.getText().toString();
        if (p.matches("")) {
            Snackbar.make(toggleButton, "Fill all fields, please.", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (cp.matches("")) {
            Snackbar.make(toggleButton, "Fill all fields, please.", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (sname.matches("")) {
            Snackbar.make(toggleButton, "Fill all fields, please.", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (slastname.matches("")) {
            Snackbar.make(toggleButton, "Fill all fields, please.", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (semail.matches("")) {
            Snackbar.make(toggleButton, "Fill all fields, please.", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (spassword.matches("")) {
            Snackbar.make(toggleButton, "Fill all fields, please.", Snackbar.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
    public void multiCheckBoxesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Services offered");
        builder.setMultiChoiceItems(profesions_list, isCheckedList, (dialogInterface, which, isChecked) -> isCheckedList[which] = isChecked);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            for (int i = 0; i < profesions_list.length; i++) {
                if (isCheckedList[i]) {
                    profesionChecked.add(profesions_list[i]);
                }
            }
        });
        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            dialog.dismiss();
            for (int i = 0; i < profesions_list.length; i++) {
                if (isCheckedList[i]) {
                    isCheckedList[i] = false;
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
    // Instance state saving and restore.
    @Override
    // Save Bundle.
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("name", sname);
        savedInstanceState.putString("lastname", slastname);
        savedInstanceState.putString("email", semail);
        savedInstanceState.putString("password", spassword);

        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    // Restore Bundle.
    public void onRestoreInstanceState(Bundle savedInstanceBundle) {
        super.onRestoreInstanceState(savedInstanceBundle);

        name.setText(savedInstanceBundle.getString("name"));
        lastname.setText(savedInstanceBundle.getString("lastname"));
        email.setText(savedInstanceBundle.getString("email"));
        password.setText(savedInstanceBundle.getString("password"));

    }




}