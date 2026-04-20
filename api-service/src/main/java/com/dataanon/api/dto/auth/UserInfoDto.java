package com.dataanon.api.dto.auth;

import com.dataanon.api.domain.entity.User;

public record UserInfoDto(Long id, String email, String name, String role, Long companyId) {

    public static UserInfoDto from(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getCompany() != null ? user.getCompany().getId() : null
        );
    }
}
