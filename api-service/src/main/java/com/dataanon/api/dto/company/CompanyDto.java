package com.dataanon.api.dto.company;

import com.dataanon.api.domain.entity.Company;

public record CompanyDto(Long id, String name, String sectorId, String region, String size) {

    public static CompanyDto from(Company c) {
        return new CompanyDto(c.getId(), c.getName(), c.getSectorId(), c.getRegion(), c.getSize());
    }
}
