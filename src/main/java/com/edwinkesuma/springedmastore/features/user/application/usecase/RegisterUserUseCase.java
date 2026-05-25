package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.features.user.application.dto.RequestRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseRegisterDTO;

public interface RegisterUserUseCase {
    ResponseRegisterDTO execute(RequestRegisterDTO request);
}
