package com.afundacionfp.casayuda;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.handlers.UserResponseHandler;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest201Register {
    private boolean PetitionCompleted = false;

    @Test
    public void testRegisterSuccess() throws InterruptedException {

        Date fecha = new Date();

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).userRegister("Ejemplo",
                "Test Apellido Ejémplez",
                "test@email.com"+fecha.getTime(),
                "0000", new String[]{"Cleaning", "Kitchen", "Plumbing", "Electricity",
                        "Baby-sitter", "Dependent care"}, 1, 368, 1234,
                new UserResponseHandler() {
                    @Override
                    public void sessionsRequestDidComplete() {
                        System.out.println("La petición se ha completado");
                        PetitionCompleted=true;
                    }

                    @Override
                    public void requestDidFail(int statusCode) {

                    }
                });
        Thread.sleep(10000);
        assertTrue(PetitionCompleted);

    }

}
