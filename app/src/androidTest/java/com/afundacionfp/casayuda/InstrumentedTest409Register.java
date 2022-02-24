package com.afundacionfp.casayuda;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.handlers.UserResponseHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest409Register {
    private int resultingStatusCode = 0;
    @Test
    public void testRegister4009() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).userRegister("Ejemplo",
                "Test Apellido Ejémplez",
                "test@email.com",
                "aaaa", new String[]{"Cleaning", "Kitchen", "Plumbing", "Electricity",
                        "Baby-sitter", "Dependent care"}, 1, 368, 1234,
                new UserResponseHandler() {
                    @Override
                    public void sessionsRequestDidComplete() {
                    }

                    @Override
                    public void requestDidFail(int statusCode) {
                        resultingStatusCode=statusCode;
                    }
                });
        Thread.sleep(10000);
        assertEquals(409, resultingStatusCode);
    }
}
