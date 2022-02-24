package com.afundacionfp.casayuda.screens.request;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.handlers.PostAcceptWorkerRequestHandler;
import com.google.android.material.snackbar.Snackbar;

public class SeeRequestActivity extends AppCompatActivity {

    TextView seeRequestHirer;
    TextView seeRequestDate;
    TextView seeRequestHour;
    TextView seeRequestsMessage;
    Button seeRequestButtonAccept;
    private Context context = this;
    private int workerId;
    private int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_request);
        getSupportActionBar().hide();
        Activity activity = this;

        // Create the get Intent object
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        requestId = (int) b.get("id");

        // Find the id in the activity
        seeRequestHirer = findViewById(R.id.seeRequestHirer);
        seeRequestDate = findViewById(R.id.seeRequestDate);
        seeRequestHour = findViewById(R.id.seeRequestHour);
        seeRequestsMessage = findViewById(R.id.seeRequestsMessage);
        seeRequestButtonAccept = findViewById(R.id.seeRequestButtonAccept);

        // Retrieve the data
        String seeRequestHirerData = intent.getStringExtra("hirer");
        String seeRequestDateData = intent.getStringExtra("date");
        System.out.println(seeRequestDateData);
        String hour = seeRequestDateData.substring(11, seeRequestDateData.length() - 1);
        String seeRequestMessageData = intent.getStringExtra("message");

        seeRequestDateData = seeRequestDateData.substring(0, 10);

        // Show the data
        seeRequestHirer.setText(seeRequestHirerData);
        seeRequestDate.setText(seeRequestDateData);
        seeRequestHour.setText(hour);
        seeRequestsMessage.setText(seeRequestMessageData);

        // If you press the button, do something
        seeRequestButtonAccept.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
            RestClient.getInstance(getApplicationContext()).postAcceptWorkerRequest(sharedPref.getInt("user_id", 0),
                    requestId,
                    new PostAcceptWorkerRequestHandler() {
                        @Override
                        public void requestDidComplete() {
                            Snackbar.make(seeRequestButtonAccept, "Job accepted.", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void requestDidFail(int statusCode) {
                            Snackbar.make(seeRequestButtonAccept, "Something went wrong...", Snackbar.LENGTH_SHORT).show();                        }
                    });
        });
    }
}