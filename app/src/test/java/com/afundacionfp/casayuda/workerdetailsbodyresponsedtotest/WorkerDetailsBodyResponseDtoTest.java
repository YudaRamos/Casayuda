package com.afundacionfp.casayuda.workerdetailsbodyresponsedtotest;

import com.afundacionfp.casayuda.client.dtos.workerdetailbodyresponsedto.WorkerDetailBodyResponseDto;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Tests the creation of a WorkerDetailsBodyResponseDto Object from a local JSON File.
 */
public class WorkerDetailsBodyResponseDtoTest {
    @Test
    public void WorkerDetailsDtoJsonParse() {

        try {
            BufferedReader json = new BufferedReader(
                    new FileReader("src/test/java/com/afundacionfp/casayuda/" +
                            "workerdetailsbodyresponsedtotest/workerdetails.json"));
            String jsonString = "";
            String currentLine = json.readLine();
            while(currentLine != null) {
                jsonString += currentLine;
                currentLine = json.readLine();
            }
            JSONObject jsonObject = new JSONObject(jsonString);
            WorkerDetailBodyResponseDto dto = new WorkerDetailBodyResponseDto(jsonObject);

            // JSON -> DTO Tests
            assertEquals(dto.getUserId(), 2);
            assertEquals(dto.getName(), "Wolfgang");
            assertEquals(dto.getSurname(), "Amadeus");
            assertEquals(dto.getJobs().get(0), "cooker");
            assertEquals(dto.getJobs().get(1), "plumber");
            assertEquals(dto.getPricePerHour(), 18000);
            assertEquals(dto.getHiredBefore().get(0).getDatetime(), "2019-07-21T17:30:00Z");
            assertEquals(dto.getHiredBefore().get(0).getHirerId(), 3);
            assertTrue(dto.getHiredBefore().get(0).isAccepted());
            assertEquals(dto.getHiredBefore().get(0).getDatetime(), "2021-01-09T10:00:00Z");
            assertEquals(dto.getHiredBefore().get(0).getHirerId(), 3);
            assertFalse(dto.getHiredBefore().get(0).isAccepted());
            assertEquals(dto.getComments().get(0).getAuthorName(), "Deadpool");
            assertEquals(dto.getComments().get(0).getRating(), 2);
            assertEquals(dto.getComments().get(0).getComment(), "Me dejó la casa hecha un desastre...");
            assertEquals(dto.getComments().get(0).getAuthorName(), "Lobezno");
            assertEquals(dto.getComments().get(0).getRating(), 4);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}
