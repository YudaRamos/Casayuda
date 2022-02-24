package com.afundacionfp.casayuda;

import static org.junit.Assert.assertEquals;

import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class UserLoginBodyResponseDtoTest {
    @Test
    public void UserLoginBodyResponseDto(){
        try{
            BufferedReader line = new BufferedReader(
                    new FileReader("src/test/java/com/afundacionfp/casayuda/"+"sessions.json"));
                    String lineRead="";
                    String currentLine=line.readLine();
                    while(currentLine!=null){
                        lineRead+=currentLine;
                        currentLine=line.readLine();
                    }
            JSONObject jsonObject=new JSONObject(lineRead);
            LoginUserBodyResponseDto dto = new LoginUserBodyResponseDto(jsonObject);
            assertEquals(dto.getUser_id(), 3);
            assertEquals(dto.getNamee(), "Pepe");
            assert(dto.isIs_worker());
            assertEquals(dto.getToken(), "39kgZPajuUCP4YEbZFqL");
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }
}
