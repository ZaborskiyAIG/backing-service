package com.backing.service.transaction;

import com.backing.service.dto.request.TransactionRequestDto;
import com.backing.service.entity.BankAccount;
import com.backing.service.entity.TransactionHistory;
import com.backing.service.entity.enums.TransactionType;
import com.backing.service.exception.PinCodeNotEqualsException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.TransactionHistoryRepository;
import com.backing.service.service.impl.transaction.TransactionDepositServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionDepositServiceUnitTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private TransactionDepositServiceImpl transactionDepositService;

    private final long bankAccountId = 1L;
    private TransactionRequestDto dto;
    private final BigDecimal balance = BigDecimal.ZERO;

    @BeforeEach
    public void setup(){
        dto = new TransactionRequestDto("1234", BigDecimal.valueOf(100));
    }

    @Test
    void transactionDepositSuccessful() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode(dto.getPinCode())
                .id(bankAccountId)
                .balance(balance)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));

        transactionDepositService.transaction(bankAccountId, dto);

        ArgumentCaptor<BankAccount> accountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(bankAccountRepository).save(accountArgumentCaptor.capture());

        BankAccount newBankAccount = accountArgumentCaptor.getValue();
        assertEquals(newBankAccount.getBalance(), balance.add(dto.getPrice()));

        ArgumentCaptor<TransactionHistory> transactionHistoryArgumentCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
        verify(transactionHistoryRepository).save(transactionHistoryArgumentCaptor.capture());

        TransactionHistory transactionHistory = transactionHistoryArgumentCaptor.getValue();
        assertEquals(transactionHistory.getTransactionType(), TransactionType.DEPOSIT);
        assertNotNull(transactionHistory.getDate());
        assertEquals(transactionHistory.getBankAccount().getId(), bankAccountId);
    }

    @Test
    void transactionDeposit_pinCodeNotEqualExceptional() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode("1111")
                .id(bankAccountId)
                .balance(balance)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));

        PinCodeNotEqualsException e = assertThrows(
                PinCodeNotEqualsException.class,
                () -> transactionDepositService.transaction(bankAccountId, dto),
                "PinCodeNotEqualsException was expected"
        );
        assertEquals("pin-codes not equal", e.getMessage());
    }
}