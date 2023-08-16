package com.backing.service.repository;

import com.backing.service.dto.response.BeneficiaryBankAccountResponseDto;
import com.backing.service.entity.Beneficiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    Optional<Beneficiary> getByUsername(String username);

    @Query("SELECT new com.backing.service.dto.response.BeneficiaryBankAccountResponseDto(b.id, b.username) FROM Beneficiary b")
    Page<BeneficiaryBankAccountResponseDto> getAllBeneficiaryDto(Pageable pageable);
}