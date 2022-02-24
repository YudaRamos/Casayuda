package com.afundacionfp.casayuda.client.handlers;

import com.afundacionfp.casayuda.client.dtos.workerdetailbodyresponsedto.WorkerDetailBodyResponseDto;

public interface GetWorkerDetailHandler extends RestClientBaseResponseHandler{
    public void requestDidComplete(WorkerDetailBodyResponseDto dto);
}
