package com.dataanon.api.controller;

import com.dataanon.api.domain.entity.User;
import com.dataanon.api.dto.company.CompanyDto;
import com.dataanon.api.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    @Operation(summary = "List companies accessible to current user")
    public ResponseEntity<List<CompanyDto>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(companyService.listAccessible(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(companyService.getById(id, user));
    }
}
