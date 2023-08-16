package com.backing.service.repository;

import com.backing.service.dto.response.BankAccountResponseDto;
import com.backing.service.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("""
            SELECT new com.backing.service.dto.response.BankAccountResponseDto(ba.id, ba.beneficiary.id, ba.balance, ba.accountNumber)
            FROM BankAccount ba
            WHERE ba.beneficiary.id in (:beneficiaryIds)
            """)
    List<BankAccountResponseDto> getByBeneficiaryIds(List<Long> beneficiaryIds);

    Optional<BankAccount> getByAccountNumber(String accountNumber);
}