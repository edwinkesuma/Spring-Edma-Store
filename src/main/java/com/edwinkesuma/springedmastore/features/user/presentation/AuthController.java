package com.edwinkesuma.springedmastore.features.user.presentation;

import com.edwinkesuma.springedmastore.features.user.application.dto.RequestLoginDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.RequestRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseLoginDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.usecase.LoginUserUseCase;
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
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterDTO> register(@Valid @RequestBody RequestRegisterDTO dto) {
        ResponseRegisterDTO registeredUser = registerUserUseCase.execute(dto);

        return new ResponseEntity<ResponseRegisterDTO>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody RequestLoginDTO dto) {
        ResponseLoginDTO response = loginUserUseCase.execute(dto);

        if (response.user() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
