package com.dataanon.api.repository;

import com.dataanon.api.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findBySectorId(String sectorId);
}
