package com.backing.service.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BeneficiaryBankAccountResponseDto {

    private Long id;
    private String username;
    private List<BankAccountResponseDto> bankAccounts;

    public BeneficiaryBankAccountResponseDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}