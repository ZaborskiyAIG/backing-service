package com.backing.service.service.impl;

import com.backing.service.dto.request.BankAccountRequestDto;
import com.backing.service.dto.response.BankAccountResponseDto;
import com.backing.service.dto.response.BeneficiaryBankAccountResponseDto;
import com.backing.service.entity.BankAccount;
import com.backing.service.entity.Beneficiary;
import com.backing.service.exception.EntityNotExistException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.BeneficiaryRepository;
import com.backing.service.service.BankAccountService;
import com.backing.service.util.AccountNumberUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    @Override
    @Transactional
    public void createBankAccount(BankAccountRequestDto bankAccountDto) {
        Beneficiary beneficiary = beneficiaryRepository.getByUsername(bankAccountDto.getUsername())
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Beneficiary by username = %s not found", bankAccountDto.getUsername())
                ));

        BankAccount bankAccount = new BankAccount(bankAccountDto.getPinCode(), beneficiary, AccountNumberUtil.generatedAccountNumber());
        bankAccountRepository.save(bankAccount);
    }



    @Override
    public Page<BeneficiaryBankAccountResponseDto> getBeneficiaryBankAccountResponseDto(Integer pageNumber, Integer itemsOnPage) {
        Page<BeneficiaryBankAccountResponseDto> page = beneficiaryRepository.getAllBeneficiaryDto(PageRequest.of(pageNumber - 1, itemsOnPage));
        List<Long> beneficiaryIds = page.getContent().stream()
                .map(BeneficiaryBankAccountResponseDto::getId)
                .toList();

        Map<Long, List<BankAccountResponseDto>> beneficiaryIdToBankAccountMap = getBeneficiaryIdToBankAccountMapByBeneficiaryIds(beneficiaryIds);
        page.getContent().forEach(beneficiary -> beneficiary.setBankAccounts(beneficiaryIdToBankAccountMap.get(beneficiary.getId())));
        return page;
    }



    private Map<Long, List<BankAccountResponseDto>> getBeneficiaryIdToBankAccountMapByBeneficiaryIds(List<Long> beneficiaryIds) {
        List<BankAccountResponseDto> bankAccounts = bankAccountRepository.getByBeneficiaryIds(beneficiaryIds);
        return bankAccounts.stream()
                .collect(Collectors.groupingBy(BankAccountResponseDto::getBeneficiaryId));
    }
}