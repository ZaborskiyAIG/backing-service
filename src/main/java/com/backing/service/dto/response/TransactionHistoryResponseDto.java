package com.backing.service.dto.response;

import com.backing.service.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponseDto {

    private Long id;
    private Long bankAccountId;
    private BigDecimal price;
    private TransactionType transactionType;
    private Long transferToBankAccountId;
    private LocalDateTime date;

    public TransactionHistoryResponseDto(Long id, Long bankAccountId, BigDecimal price, TransactionType transactionType, LocalDateTime date) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.price = price;
        this.transactionType = transactionType;
        this.date = date;
    }
}