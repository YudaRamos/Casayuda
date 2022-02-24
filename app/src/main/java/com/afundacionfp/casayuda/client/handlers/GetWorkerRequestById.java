package com.afundacionfp.casayuda.client.handlers;

import com.afundacionfp.casayuda.client.dtos.JobRequestObjectDto;
import java.util.List;

public interface GetWorkerRequestById extends RestClientBaseResponseHandler {
    void requestDidComplete(List<JobRequestObjectDto> response);
}
