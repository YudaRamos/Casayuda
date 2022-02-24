package com.afundacionfp.casayuda.screens.request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.JobRequestObjectDto;
import com.afundacionfp.casayuda.client.handlers.GetWorkerRequestById;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class YourRequestsActivity extends AppCompatActivity {

    private Context context = this;
    private BottomNavigationView navButton;
    private Activity activity;
    private RecyclerView recycle;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<JobRequestObjectDto> toggleList = new ArrayList<>();
    private List<JobRequestObjectDto> rawResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_requests);
        getSupportActionBar().hide();
        activity = this;
        // Create the recycler view
        recycle = findViewById(R.id.yourRequestsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recycle.setLayoutManager(manager);
        recycle.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewAdapter(toggleList, context);
        recycle.setAdapter(recyclerViewAdapter);

        navButton = findViewById(R.id.bottom_navigation);

        getRequest();

        navButton.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.accepted_requests:
                    toggleList.clear();
                    rawResponse.forEach(data ->{
                        if(data.getIsAccepted())
                            toggleList.add(data);
                    });
                    recyclerViewAdapter.updateData(toggleList);
                    break;
                default:
                    toggleList.clear();
                    rawResponse.forEach(data ->{
                        if(!data.getIsAccepted())
                            toggleList.add(data);
                    });
                    recyclerViewAdapter.updateData(toggleList);
                    break;
            }
            return true;
        });
    }
    public void getRequest(){
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        RestClient.getInstance(this).getWorkerRequestById(sharedPref.getInt("user_id", 0),  new GetWorkerRequestById() {
            @Override
            public void requestDidComplete(List<JobRequestObjectDto> response) {
                rawResponse.addAll(response);
                forceUpdateRecycleView();
            }

            @Override
            public void requestDidFail(int statusCode) {
                Snackbar.make(recycle, "Something went wrong...", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void forceUpdateRecycleView(){
        toggleList.clear();
        rawResponse.forEach(data ->{
                if(!data.getIsAccepted())
                    toggleList.add(data);
        });
        recyclerViewAdapter.updateData(toggleList);
    }
}