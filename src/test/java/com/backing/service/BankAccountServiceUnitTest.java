package com.backing.service;

import com.backing.service.dto.request.BankAccountRequestDto;
import com.backing.service.dto.response.BankAccountResponseDto;
import com.backing.service.dto.response.BeneficiaryBankAccountResponseDto;
import com.backing.service.entity.BankAccount;
import com.backing.service.entity.Beneficiary;
import com.backing.service.exception.EntityNotExistException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.BeneficiaryRepository;
import com.backing.service.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceUnitTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private Beneficiary beneficiary;
    private final String pinCode = "1234";

    @BeforeEach
    public void setup(){
        beneficiary = new Beneficiary(1L, "username1");
    }

    @Test
    void createBankAccountSuccessful() {
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(beneficiary.getUsername(), pinCode);

        when(beneficiaryRepository.getByUsername(beneficiary.getUsername())).thenReturn(Optional.of(beneficiary));

        bankAccountService.createBankAccount(bankAccountRequestDto);

        ArgumentCaptor<BankAccount> accountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(bankAccountRepository).save(accountArgumentCaptor.capture());

        BankAccount bankAccount = accountArgumentCaptor.getValue();
        assertEquals(bankAccount.getBeneficiary().getId(), beneficiary.getId());
        assertEquals(bankAccount.getPinCode(), bankAccountRequestDto.getPinCode());
    }

    @Test
    void createBankAccount_notFoundBeneficiaryExceptional() {
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(beneficiary.getUsername(), pinCode);

        when(beneficiaryRepository.getByUsername(any())).thenReturn(Optional.empty());

        EntityNotExistException e = assertThrows(
                EntityNotExistException.class,
                () -> bankAccountService.createBankAccount(bankAccountRequestDto),
                "EntityNotExistException was expected"
        );
        assertEquals(String.format("Beneficiary by username = %s not found", beneficiary.getUsername()), e.getMessage());
    }

    @Test
    void getBeneficiaryBankAccountResponseDtoSuccessful() {
        int pageNumber = 1;
        int itemsOnPage = 10;
        List<BeneficiaryBankAccountResponseDto> beneficiaryBankAccountResponseDtos = List.of(
                new BeneficiaryBankAccountResponseDto(1L, "username1"),
                new BeneficiaryBankAccountResponseDto(2L, "username2"),
                new BeneficiaryBankAccountResponseDto(3L, "username3"),
                new BeneficiaryBankAccountResponseDto(4L, "username4")
        );
        List<Long> beneficiaryIds = List.of(1L, 2L, 3L, 4L);
        List<BankAccountResponseDto> bankAccountResponseDtos = List.of(
                new BankAccountResponseDto(1L, 1L, BigDecimal.ZERO),
                new BankAccountResponseDto(2L, 1L, BigDecimal.ZERO),
                new BankAccountResponseDto(3L, 2L, BigDecimal.ZERO),
                new BankAccountResponseDto(4L, 3L, BigDecimal.ZERO),
                new BankAccountResponseDto(5L, 3L, BigDecimal.ZERO),
                new BankAccountResponseDto(6L, 4L, BigDecimal.ZERO)
        );
        Page<BeneficiaryBankAccountResponseDto> page = new PageImpl<>(beneficiaryBankAccountResponseDtos);
        when(beneficiaryRepository.getAllBeneficiaryDto(PageRequest.of(pageNumber - 1, itemsOnPage)))
                .thenReturn(page);
        when(bankAccountRepository.getByBeneficiaryIds(beneficiaryIds)).thenReturn(bankAccountResponseDtos);

        Page<BeneficiaryBankAccountResponseDto> result = bankAccountService.getBeneficiaryBankAccountResponseDto(pageNumber, itemsOnPage);

        BeneficiaryBankAccountResponseDto content = result.getContent().get(0);
        assertEquals(content.getId(), beneficiaryBankAccountResponseDtos.get(0).getId());
        assertEquals(content.getBankAccounts().size(), 2);
        assertEquals(content.getBankAccounts().get(0).getId(), 1);
        assertEquals(content.getBankAccounts().get(1).getId(), 2);

        BeneficiaryBankAccountResponseDto content1 = result.getContent().get(1);
        assertEquals(content1.getId(), beneficiaryBankAccountResponseDtos.get(1).getId());
        assertEquals(content1.getBankAccounts().size(), 1);
        assertEquals(content1.getBankAccounts().get(0).getId(), 3);

        BeneficiaryBankAccountResponseDto content2 = result.getContent().get(2);
        assertEquals(content2.getId(), beneficiaryBankAccountResponseDtos.get(2).getId());
        assertEquals(content2.getBankAccounts().size(), 2);
        assertEquals(content2.getBankAccounts().get(0).getId(), 4);
        assertEquals(content2.getBankAccounts().get(1).getId(), 5);

        BeneficiaryBankAccountResponseDto content3 = result.getContent().get(3);
        assertEquals(content3.getId(), beneficiaryBankAccountResponseDtos.get(3).getId());
        assertEquals(content3.getBankAccounts().size(), 1);
        assertEquals(content3.getBankAccounts().get(0).getId(), 6);
    }
}