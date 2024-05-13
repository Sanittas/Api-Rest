package com.sanittas.AuthServer.controller;

import jakarta.validation.constraints.NotBlank;

public record LoginDtoRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
