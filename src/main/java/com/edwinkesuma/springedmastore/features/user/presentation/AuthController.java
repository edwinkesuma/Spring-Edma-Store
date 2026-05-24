package com.edwinkesuma.springedmastore.features.user.presentation;

import com.edwinkesuma.springedmastore.features.user.application.dto.RequestRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterDTO> register(@Valid @RequestBody RequestRegisterDTO dto) {
        ResponseRegisterDTO registeredUser = registerUserUseCase.execute(dto);

        return new ResponseEntity<ResponseRegisterDTO>(registeredUser, HttpStatus.CREATED);
    }
}
