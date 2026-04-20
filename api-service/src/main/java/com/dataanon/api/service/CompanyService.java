package com.dataanon.api.service;

import com.dataanon.api.domain.entity.User;
import com.dataanon.api.domain.enums.UserRole;
import com.dataanon.api.dto.company.CompanyDto;
import com.dataanon.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyDto> listAccessible(User currentUser) {
        if (currentUser.getRole() == UserRole.CLIENT) {
            if (currentUser.getCompany() == null) {
                return List.of();
            }
            return List.of(CompanyDto.from(currentUser.getCompany()));
        }
        return companyRepository.findAll().stream()
                .map(CompanyDto::from)
                .toList();
    }

    public CompanyDto getById(Long id, User currentUser) {
        if (currentUser.getRole() == UserRole.CLIENT) {
            if (currentUser.getCompany() == null || !currentUser.getCompany().getId().equals(id)) {
                throw new AccessDeniedException("Access denied");
            }
        }
        return companyRepository.findById(id)
                .map(CompanyDto::from)
                .orElseThrow(() -> new RuntimeException("Company not found: " + id));
    }
}
