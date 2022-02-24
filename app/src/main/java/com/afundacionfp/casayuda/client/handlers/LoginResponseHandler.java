package com.afundacionfp.casayuda.client.handlers;

import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;

public interface LoginResponseHandler extends RestClientBaseResponseHandler {
    void sessionsRequestDidComplete(LoginUserBodyResponseDto dto);
}
