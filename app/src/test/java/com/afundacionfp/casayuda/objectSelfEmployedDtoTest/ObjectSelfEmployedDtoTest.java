package com.afundacionfp.casayuda.objectSelfEmployedDtoTest;

import static org.junit.Assert.assertEquals;
import com.afundacionfp.casayuda.client.dtos.ObjectSelfEmployedDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ObjectSelfEmployedDtoTest {

    @Test
    public void WorkerDetailsDtoJsonParse() {


        try {
            BufferedReader json = new BufferedReader(
                    new FileReader("src/test/java/com/afundacionfp/casayuda/objectSelfEmployedDtoTest/workers.json"));

            String jsonString = "";
            String currentLine = json.readLine();
            while (currentLine != null) {
                jsonString += currentLine;
                currentLine = json.readLine();
            }

            JSONArray jsonArray = new JSONArray(jsonString);

            ObjectSelfEmployedDto dto1 = new ObjectSelfEmployedDto(jsonArray.getJSONObject(0));
            ObjectSelfEmployedDto dto2 = new ObjectSelfEmployedDto(jsonArray.getJSONObject(1));

            // JSON -> DTO Tests
            assertEquals(dto1.getUserId(), 2);
            assertEquals(dto1.getName(), "Wolfgang");
            assertEquals(dto1.getSurname(), "Amadeus");
            assertEquals(dto1.getJobs().get(0), "cooker");
            assertEquals(dto1.getJobs().get(1), "plumber");
            assertEquals(dto1.getPricePerHour(), 18000);

            assertEquals(dto2.getUserId(), 11);
            assertEquals(dto2.getName(), "Johan");
            assertEquals(dto2.getSurname(), "Sebastian");
            assertEquals(dto2.getJobs().get(0), "electrician");
            assertEquals(dto2.getPricePerHour(), 11500);


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}
