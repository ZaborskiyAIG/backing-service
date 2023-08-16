package com.backing.service.repository;

import com.backing.service.dto.response.TransactionHistoryResponseDto;
import com.backing.service.entity.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    @Query("""
            SELECT new com.backing.service.dto.response.TransactionHistoryResponseDto(
                th.id,
                th.bankAccount.id,
                th.price,
                th.transactionType,
                th.transferToBankAccount.id,
                th.date
            )
            FROM TransactionHistory th
            WHERE th.bankAccount.id = :bankAccountId
            OR th.transferToBankAccount.id = :bankAccountId
            ORDER BY th.date
            """)
    Page<TransactionHistoryResponseDto> getByBankAccountId(Long bankAccountId, Pageable pageable);
}