package com.afundacionfp.casayuda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;
import com.afundacionfp.casayuda.client.dtos.ObjectSelfEmployedDto;
import com.afundacionfp.casayuda.client.handlers.LoginResponseHandler;
import com.afundacionfp.casayuda.client.handlers.WorkersCommentsResponseHandler;
import com.afundacionfp.casayuda.client.handlers.WorkersResponseHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTestsPostComment {
    private String loginToken;

    @Before
    public void Precondition() {
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
    public void WorkersCommentsRequestWorksOk() {
        final boolean[] requestSuccessful = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        RestClient.getInstance(appContext).requestPostWorkerComments(1, 5, "Comentario",  new WorkersCommentsResponseHandler() {
            @Override
            public void workersCommentsRequestDidComplete() {
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
    public void WorkersCommentsRequestWorks401() {
        final boolean[] requestFailed = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).requestPostWorkerComments(1, 5, "Comentario",  new WorkersCommentsResponseHandler() {
            @Override
            public void workersCommentsRequestDidComplete() {

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
    public void WorkersCommentsRequestWorks403() {
        final boolean[] requestFailed = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).requestPostWorkerComments(1, 5, "Comentario",  new WorkersCommentsResponseHandler() {


            @Override
            public void workersCommentsRequestDidComplete() {

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
    @Test
    public void WorkersCommentsRequestWorks404() {
        final boolean[] requestFailed = new boolean[1];

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).requestPostWorkerComments(554546895, 5, "Comentario",  new WorkersCommentsResponseHandler() {
            @Override
            public void workersCommentsRequestDidComplete() {

            }

            @Override
            public void requestDidFail(int statusCode) {
                assertEquals(404, statusCode);
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