package com.sanittas.AuthServer.controller;

public record LoginDtoResponse(
        Integer id,
        String username,
        String token
) {
}
