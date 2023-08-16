package com.backing.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BankAccountResponseDto {

    private Long id;
    @JsonIgnore
    private Long beneficiaryId;
    private BigDecimal balance;
    private String accountNumber;

    public BankAccountResponseDto(Long id, Long beneficiaryId, BigDecimal balance) {
        this.id = id;
        this.beneficiaryId = beneficiaryId;
        this.balance = balance;
    }
}