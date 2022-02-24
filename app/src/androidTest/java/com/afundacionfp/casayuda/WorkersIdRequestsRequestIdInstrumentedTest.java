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
public class WorkersIdRequestsRequestIdInstrumentedTest {

    private boolean requestCompleted = false;
    private boolean requestTokenFailed = false;
    private boolean invalidTokenOrId = false;
    private boolean userIdNotExists = false;

    @Before
    public void workersIdRequestsPrecondition() {
        // PRECONDITIONS for the test:
        // For example:
        // * In Database there exists an object 'XXX' with attributes:
        //   - id: 2
        //   - request_id: 501
        //   - token: "39kgZPajuUCP4YEbZFqL"

        // Unimplemented yet
    }

    @Test // code 201
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