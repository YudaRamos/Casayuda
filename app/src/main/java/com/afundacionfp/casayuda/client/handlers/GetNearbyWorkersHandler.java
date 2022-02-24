package com.afundacionfp.casayuda.client.handlers;

import com.afundacionfp.casayuda.client.dtos.ObjectSelfEmployedDto;
import java.util.List;

public interface GetNearbyWorkersHandler extends RestClientBaseResponseHandler {
    void WorkersRequestDidComplete(List<ObjectSelfEmployedDto> response);
}
