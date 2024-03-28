package com.dataanon.api.dto.auth;

public record LoginResponse(String token, UserInfoDto user) {}
