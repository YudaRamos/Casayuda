package com.afundacionfp.casayuda;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;
import com.afundacionfp.casayuda.client.handlers.LoginResponseHandler;
import com.afundacionfp.casayuda.client.handlers.UserResponseHandler;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest401Login {
    private String userEmail;
    private int resultingStatusCode = 0;

    @Before
    public void precondition() throws InterruptedException {
        Date fecha = new Date();
        userEmail= "test@email.com"+fecha.getTime();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).userRegister("Ejemplo",
                "Test Apellido Ejémplez",
                userEmail,
                "0000", new String[]{"Cleaning", "Kitchen", "Plumbing", "Electricity",
                        "Baby-sitter", "Dependent care"}, 1, 368, 1234,
                new UserResponseHandler() {
                    @Override
                    public void sessionsRequestDidComplete() {
                        System.out.println("La petición se ha completado");
                    }

                    @Override
                    public void requestDidFail(int statusCode) {

                    }
                });
        Thread.sleep(10000);
    }
    @Test
    public void testFailedLogin() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).userLogin(userEmail, "aaaa",
                new LoginResponseHandler() {
                    @Override
                    public void sessionsRequestDidComplete(LoginUserBodyResponseDto dto) {

                    }
                    @Override
                    public void requestDidFail(int statusCode) {
                        resultingStatusCode=statusCode;
                    }
                });
        Thread.sleep(10000);
        assertEquals(401, resultingStatusCode);
    }
}
