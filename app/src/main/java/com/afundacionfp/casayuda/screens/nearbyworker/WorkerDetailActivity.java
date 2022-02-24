package com.afundacionfp.casayuda.screens.nearbyworker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afundacionfp.casayuda.R;
import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.workerdetailbodyresponsedto.WorkerDetailBodyResponseDto;
import com.afundacionfp.casayuda.client.handlers.GetWorkerDetailHandler;
import com.afundacionfp.casayuda.client.handlers.PostWorkerCommentHandler;
import com.afundacionfp.casayuda.screens.comment.CommentData;
import com.afundacionfp.casayuda.screens.comment.CommentsRecyclerViewAdapter;
import com.afundacionfp.casayuda.screens.makerequestactivity.MakeRequestActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class WorkerDetailActivity extends AppCompatActivity {
    private RatingBar simpleRatingBar;
    private RecyclerView recyclerView;
    private int workerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_detail);
        getSupportActionBar().hide();

        Button commentButton = findViewById(R.id.worker_comment_button);

        // Save id
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        workerId = (int) b.get("id");
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
        int hiredByUserId = sharedPref.getInt("user_id", 0);

        recyclerView = findViewById(R.id.worker_detail_recycler_view);
        recyclerView.setVisibility(View.GONE);

        Activity activity = this;
        TextView previuslyHireTextView = findViewById(R.id.previuslyHireTextView);

        previuslyHireTextView.setVisibility(View.INVISIBLE);
        //Show hired previusly



        //Hire button that launch the Worker Hire Activity.
        Button hiredButton = findViewById(R.id.hire_button);
        hiredButton.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), MakeRequestActivity.class);
            intent.putExtra("id",workerId);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            Explode explode = new Explode();
            explode.setDuration(500); // Duración en milisegundos
            getWindow().setExitTransition(explode);

            Toast.makeText(getApplicationContext(),"You are on the great way", Toast.LENGTH_SHORT).show();
        });

        RestClient.getInstance(getApplicationContext()).getWorkerDetail(workerId,
                hiredByUserId,
                new GetWorkerDetailHandler() {
                    @Override
                    public void requestDidComplete(WorkerDetailBodyResponseDto dto) {

                        TextView name = findViewById(R.id.name);
                        TextView surname = findViewById(R.id.surname);
                        TextView price = findViewById(R.id.price);
                        TextView services = findViewById(R.id.services);

                        name.setText(dto.getName());
                        surname.setText(dto.getSurname());

                        StringBuilder stringBuilder = new StringBuilder();

                        dto.getJobs().forEach(job->{
                            String aux;
                            if(!stringBuilder.toString().equals(""))
                                stringBuilder.append(", ");
                            aux = job.toString();
                            aux = aux.substring(0,1).toUpperCase() + aux.substring(1).toLowerCase();
                            stringBuilder.append(aux);
                        });

                        price.setText(String.valueOf(dto.getPricePerHour() + "€/hour"));
                        services.setText(stringBuilder.toString());

                        dto.getHiredBefore().forEach(jobs -> {
                            if(jobs.getHirerId() == hiredByUserId) {
                                previuslyHireTextView.setVisibility(View.VISIBLE);
                                previuslyHireTextView.setText("Previusly hired");
                            }
                        });

                        List<CommentData> data = new ArrayList<>();
                        dto.getComments().forEach(comment ->
                                data.add(new CommentData(comment.getAuthorName(),comment.getComment(),comment.getRating())));

                        recyclerView.setVisibility(View.VISIBLE);
                        CommentsRecyclerViewAdapter adapter = new CommentsRecyclerViewAdapter(data, activity);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        //Adding info to the rating bar
                        float rating = averageRating(data);
                        simpleRatingBar = findViewById(R.id.rating); // initiate a rating bar
                        simpleRatingBar.setRating(rating); // get rating number from a rating bar
                    }

                    @Override
                    public void requestDidFail(int statusCode) {
                        Toast.makeText(getApplicationContext(), "Something went wrong, try again later...", Toast.LENGTH_SHORT).show();
                    }
                });

        commentButton.setOnClickListener(view -> {
            //Hiding the keyboard when the user click on the comment button
            hideKeyboard(WorkerDetailActivity.this);
            // Find the EditText at XML
            EditText writeComment = findViewById(R.id.workerCommentTextView);
            // Extract the text writed in the EditText and create a String variable with it
            String textCommented = writeComment.getText().toString();
            RatingBar ratingBar = findViewById(R.id.workerCommentRatingBar);
            sendCommentRequest(textCommented, ratingBar.getRating());
            writeComment.setText("");
        });
    }

    /**
     * Calculate the average rating of comments
     *
     * @param dataset The List of all comments
     * @return The average rating
     */
    public float averageRating(List<CommentData> dataset) {
        float rating = 0;
        for (int i = 0; i < dataset.size(); i++) {
            rating += dataset.get(i).getRating();
        }
        rating = rating / dataset.size();

        return rating;
    }


    /**
     * Send comments to server
     * @param comment User comment
     * @param rating User rating
     * @author Javier Miralles Rancaño
     */
    private void sendCommentRequest(String comment, float rating) {

        RestClient.getInstance(getApplicationContext()).postWorkerComment(workerId, (int) rating, comment,  new PostWorkerCommentHandler() {
            @Override
            public void workersCommentsRequestDidComplete()  {
                Snackbar.make(recyclerView, "Thanks for your feedback!", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void requestDidFail(int statusCode) {
                Snackbar.make(recyclerView, "Something went wrong, try again later...Error: " + statusCode, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}