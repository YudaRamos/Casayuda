package com.afundacionfp.casayuda.screens.makerequestactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.handlers.PostHireWorkerHandler;
import com.afundacionfp.casayuda.screens.nearbyworker.NearbyWorkersActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class MakeRequestActivity extends AppCompatActivity {
    CalendarView currentDate;
    TextView currentTime;
    private String hireDate;
    private String hireTime;
    private String moreInfo;
    private String hireDatetime;
    private int workerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        workerId = (int) b.get("id");

        //TextViews
        currentDate = findViewById(R.id.calendarRequestView);
        currentTime = findViewById(R.id.current_time_text);

        //Multinline text
        TextInputEditText infoText = findViewById(R.id.editText_ExplainedJob);

        // Buttons
        final Button setHour = findViewById(R.id.set_hour_button);
        final Button create = findViewById(R.id.create_button);

        // OnCLickListeners
        setHour.setOnClickListener(view -> {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });

        //Pick the date on calendar
        currentDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> hireDate = year + "-"+ month + "-"+ dayOfMonth + "T");

        //Create a request to hire a service
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreInfo = infoText.getText().toString();
                hireTime = currentTime.getText().toString();


                hireDatetime = hireDate + hireTime +":00Z";

                //Check that all the parameters are filled.
                if(!moreInfo.equals("") && !hireDate.equals("") && !hireTime.equals("")){

                    RestClient.getInstance(getApplicationContext()).postHireWorker(workerId,
                            hireDatetime,
                            moreInfo,
                            new PostHireWorkerHandler() {
                                @Override
                                public void requestDidComplete() {
                                    Snackbar.make(infoText, "CREATED APPOINTMENT AT: "
                                            + hireDate + " " + hireTime, Snackbar.LENGTH_SHORT).show();

                                    //returning to the previus activity.
                                    Intent intent = new Intent(getApplicationContext(), NearbyWorkersActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void requestDidFail(int statusCode) {
                                    Snackbar.make(infoText, "Something went wrong...", Snackbar.LENGTH_SHORT).show();
                                }
                            });


                }
                else {
                    Snackbar.make(infoText, "Fill empty fields, please.", Snackbar.LENGTH_SHORT).show();
                }


            }
        });

    }

    // From https://stackoverflow.com/questions/5123407/losing-data-when-rotate-screen
    // Used to keep TextViews data after rotation.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        //savedInstanceState.putString("date", (String) current_date.getDate());
        savedInstanceState.putString("hour", (String) currentTime.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        //current_date.setText((String) savedInstanceState.get("date"));
        currentTime.setText((String) savedInstanceState.get("hour"));
    }
}