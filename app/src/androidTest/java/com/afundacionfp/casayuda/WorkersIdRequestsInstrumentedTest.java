package com.afundacionfp.casayuda;

import android.content.Context;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.JobRequestObjectDto;
import com.afundacionfp.casayuda.client.handlers.workersIdRequests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class WorkersIdRequestsInstrumentedTest {

    private boolean requestCompleted = false;
    private boolean requestTokenFailed = false;
    private boolean invalidTokenOrId = false;
    private boolean userIdNotExists = false;

    @Before
    public void workersIdRequestsPrecondition() {
        // PRECONDITIONS for the test:
        // For example:
        // * In Database there exists an object 'XXX' with attributes:
        //   - request_id: 298
        //   - hirer_name: "Maze W"
        //   - requested_datetime: "2021-01-07T17:30:00Z"
        //   - message: "This is my message for the precondition"
        //   - is_accepted: true

        // Unimplemented yet
    }

    @Test // code 200
    public void approvedWorkRequest() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).getWorkerIdRequests(
                100,
                new workersIdRequests() {
                    @Override
                    public void requestDidComplete(List<JobRequestObjectDto> response) {
                        System.out.println("La peticion se completo!");
                        requestCompleted = true;
                    }

                    @Override
                    public void requestDidFail(int statusCode) {}
                });
        Thread.sleep(10000);
        assertEquals(true, requestCompleted);
    }

    @Test // code 401
    public void failedTokenInWorkRequest() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).getWorkerIdRequests(
                100,

                new workersIdRequests() {
                    @Override
                    public void requestDidComplete(List<JobRequestObjectDto> response) {
                        System.out.println("La peticion se completo!");
                        requestTokenFailed = true;
                    }

                    @Override
                    public void requestDidFail(int statusCode) {}
                });
        Thread.sleep(10000);
        assertEquals(true, requestTokenFailed);
    }

    @Test // code 403
    public void invalidTokenOrIdWorkRequest() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).getWorkerIdRequests(
                100,

                new workersIdRequests() {
                    @Override
                    public void requestDidComplete(List<JobRequestObjectDto> response) {
                        System.out.println("La peticion se completo!");
                        invalidTokenOrId = true;
                    }

                    @Override
                    public void requestDidFail(int statusCode) {}
                });
        Thread.sleep(10000);
        assertEquals(true, invalidTokenOrId);
    }

    @Test // code 404
    public void userIdNotExistsWorkRequest() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).getWorkerIdRequests(
                99999,

                new workersIdRequests() {
                    @Override
                    public void requestDidComplete(List<JobRequestObjectDto> response) {
                        System.out.println("La peticion se completo!");
                        userIdNotExists = true;
                    }

                    @Override
                    public void requestDidFail(int statusCode) {}
                });
        Thread.sleep(10000);
        assertEquals(true, userIdNotExists);
    }
}