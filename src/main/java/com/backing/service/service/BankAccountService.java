package com.backing.service.service;

import com.backing.service.dto.request.BankAccountRequestDto;
import com.backing.service.dto.response.BeneficiaryBankAccountResponseDto;
import org.springframework.data.domain.Page;


public interface BankAccountService {

    void createBankAccount(BankAccountRequestDto bankAccountDto);

    Page<BeneficiaryBankAccountResponseDto> getBeneficiaryBankAccountResponseDto(Integer pageNumber, Integer itemsOnPage);
}