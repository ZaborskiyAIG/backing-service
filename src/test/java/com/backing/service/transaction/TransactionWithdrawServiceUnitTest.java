package com.backing.service.transaction;

import com.backing.service.dto.request.TransactionRequestDto;
import com.backing.service.entity.BankAccount;
import com.backing.service.entity.TransactionHistory;
import com.backing.service.entity.enums.TransactionType;
import com.backing.service.exception.BalanceException;
import com.backing.service.exception.PinCodeNotEqualsException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.TransactionHistoryRepository;
import com.backing.service.service.impl.transaction.TransactionWithdrawServiceImpl;
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
public class TransactionWithdrawServiceUnitTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private TransactionWithdrawServiceImpl transactionWithdrawService;

    private final long bankAccountId = 1L;
    private final BigDecimal balance = BigDecimal.valueOf(250);
    private TransactionRequestDto dto;

    @BeforeEach
    public void setup(){
        dto = new TransactionRequestDto("1234", BigDecimal.valueOf(100));
    }

    @Test
    void transactionWithdrawSuccessful() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode(dto.getPinCode())
                .id(bankAccountId)
                .balance(balance)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));

        transactionWithdrawService.transaction(bankAccountId, dto);

        ArgumentCaptor<BankAccount> accountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(bankAccountRepository).save(accountArgumentCaptor.capture());

        BankAccount resultBankAccount = accountArgumentCaptor.getValue();
        assertEquals(resultBankAccount.getId(), bankAccount.getId());
        assertEquals(resultBankAccount.getBalance(), balance.subtract(dto.getPrice()));


        ArgumentCaptor<TransactionHistory> transactionHistoryArgumentCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
        verify(transactionHistoryRepository).save(transactionHistoryArgumentCaptor.capture());

        TransactionHistory transactionHistory = transactionHistoryArgumentCaptor.getValue();
        assertEquals(transactionHistory.getTransactionType(), TransactionType.WITHDRAW);
        assertNotNull(transactionHistory.getDate());
        assertEquals(transactionHistory.getBankAccount().getId(), bankAccountId);

    }

    @Test
    void transactionWithdraw_pinCodeNotEqualExceptional() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode("1111")
                .id(bankAccountId)
                .balance(balance)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));

        PinCodeNotEqualsException e = assertThrows(
                PinCodeNotEqualsException.class,
                () -> transactionWithdrawService.transaction(bankAccountId, dto),
                "PinCodeNotEqualsException was expected"
        );
        assertEquals("pin-codes not equal", e.getMessage());
    }

    @Test
    void transactionWithdraw_Exceptional() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode(dto.getPinCode())
                .id(bankAccountId)
                .balance(BigDecimal.ZERO)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));

        BalanceException e = assertThrows(
                BalanceException.class,
                () -> transactionWithdrawService.transaction(bankAccountId, dto),
                "BalanceException was expected"
        );
        assertEquals("there are not enough funds on the balance", e.getMessage());
    }

}
