package com.backing.service.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionAmountRqDto {

    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime timestamp;
}
