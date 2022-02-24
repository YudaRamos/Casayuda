package com.afundacionfp.casayuda;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.SyncStatusObserver;

import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        try {
            File archivo = new File("/src/test//java/com/afundacionfp/casayuda");
            FileReader eufrates= new FileReader(archivo);
            BufferedReader eufrates2 = new BufferedReader(eufrates);
            String jsonString="";
            String linea = eufrates2.readLine();
            while(linea!=null){
                jsonString+=linea;
                System.out.println(linea);
                linea=eufrates2.readLine();
            }
            JSONObject object= new JSONObject(jsonString);

            LoginUserBodyResponseDto dto=new LoginUserBodyResponseDto(object);
            assertEquals(3, dto.getUser_id());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}