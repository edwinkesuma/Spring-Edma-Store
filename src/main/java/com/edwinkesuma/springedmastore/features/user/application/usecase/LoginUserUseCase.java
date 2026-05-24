package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.features.user.application.dto.RequestLoginDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseLoginDTO;

public interface LoginUserUseCase {
    ResponseLoginDTO execute(RequestLoginDTO request);
}
