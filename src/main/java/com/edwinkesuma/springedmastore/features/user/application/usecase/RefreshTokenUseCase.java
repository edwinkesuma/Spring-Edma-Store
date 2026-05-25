package com.edwinkesuma.springedmastore.features.user.application.usecase;

public interface RefreshTokenUseCase {
    String execute(String refreshToken);
}
