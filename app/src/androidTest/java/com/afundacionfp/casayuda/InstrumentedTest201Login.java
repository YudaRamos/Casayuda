package com.afundacionfp.casayuda;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.afundacionfp.casayuda.client.RestClient;
import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;
import com.afundacionfp.casayuda.client.handlers.LoginResponseHandler;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class InstrumentedTest201Login {
    private boolean PetitionCompleted=false;

    @Test
    public void testLoginSuccess() throws InterruptedException {


        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RestClient.getInstance(appContext).userLogin("pepedepura@fpcoruna.afundacion.org",
                "mypass1234", new LoginResponseHandler(){
                    @Override
                    public void sessionsRequestDidComplete(LoginUserBodyResponseDto dto) {
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
