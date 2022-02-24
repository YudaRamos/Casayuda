package com.afundacionfp.casayuda;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;
import com.afundacionfp.casayuda.client.dtos.ObjectSelfEmployedDto;
import com.afundacionfp.casayuda.client.handlers.LoginResponseHandler;
import com.afundacionfp.casayuda.client.handlers.WorkersResponseHandler;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTestsGetWorkers {
    private String loginToken;

    @Before
    public void precondition() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        RestClient.getInstance(appContext).userLogin("pepedepura@afundacion.org", "password", new LoginResponseHandler() {
            @Override
            public void sessionsRequestDidComplete(LoginUserBodyResponseDto dto) {

            }

            @Override
            public void requestDidFail(int statusCode) {

            }
        });
    }

    @Test
    public void WorkersRequestWorksOk() {
        final boolean[] requestSuccessful = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        RestClient.getInstance(appContext).requestGetWorkers(-33.885643, 151.187424, 10,  new WorkersResponseHandler() {
            @Override
            public void WorkersRequestDidComplete(List<ObjectSelfEmployedDto> response) {
                requestSuccessful[0] = true;
            }

            @Override
            public void requestDidFail(int statusCode) {
                requestSuccessful[0] = false;
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(requestSuccessful[0]);
    }

    @Test
    public void WorkersRequestWorks400() {
        final boolean[] requestFailed = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).requestGetWorkers(-33.885643, 151.187424, 10, new WorkersResponseHandler() {
            @Override
            public void WorkersRequestDidComplete(List<ObjectSelfEmployedDto> response) {
            }

            @Override
            public void requestDidFail(int statusCode) {
                assertEquals(400, statusCode);
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(requestFailed[0]);
    }

    @Test
    public void WorkersRequestWorks401() {
        final boolean[] requestFailed = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).requestGetWorkers(-33.885643, 151.187424, 10, new WorkersResponseHandler() {
            @Override
            public void WorkersRequestDidComplete(List<ObjectSelfEmployedDto> response) {
            }

            @Override
            public void requestDidFail(int statusCode) {
                assertEquals(401, statusCode);
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(requestFailed[0]);
    }
    @Test
    public void WorkersRequestWorks403() {
        final boolean[] requestFailed = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).requestGetWorkers(-33.885643, 151.187424, 10, new WorkersResponseHandler() {
            @Override
            public void WorkersRequestDidComplete(List<ObjectSelfEmployedDto> response) {
            }

            @Override
            public void requestDidFail(int statusCode) {
                assertEquals(403, statusCode);
            }
        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(requestFailed[0]);
    }
}