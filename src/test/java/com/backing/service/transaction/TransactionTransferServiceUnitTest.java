package com.backing.service.transaction;

import com.backing.service.dto.request.TransactionRequestDto;
import com.backing.service.entity.BankAccount;
import com.backing.service.entity.TransactionHistory;
import com.backing.service.entity.enums.TransactionType;
import com.backing.service.exception.BalanceException;
import com.backing.service.exception.PinCodeNotEqualsException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.TransactionHistoryRepository;
import com.backing.service.service.impl.transaction.TransactionTransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionTransferServiceUnitTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private TransactionTransferServiceImpl transactionTransferService;

    private final long bankAccountId = 1L;
    private final BigDecimal balance = BigDecimal.valueOf(250);
    private final BigDecimal transferBalance = BigDecimal.ZERO;
    private TransactionRequestDto dto;
    private final String accountNumber = "1234123412341234";
    private final String transferAccountNumber = "1111222233334444";

    @BeforeEach
    public void setup(){
        dto = new TransactionRequestDto("1234", BigDecimal.valueOf(100), transferAccountNumber);
    }

    @Test
    void transactionTransferSuccessful() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode(dto.getPinCode())
                .id(bankAccountId)
                .accountNumber(accountNumber)
                .balance(balance)
                .build();
        BankAccount transferBankAccount = BankAccount.builder()
                .pinCode("1222")
                .id(2L)
                .accountNumber(transferAccountNumber)
                .balance(transferBalance)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));
        when(bankAccountRepository.getByAccountNumber(transferAccountNumber)).thenReturn(Optional.of(transferBankAccount));

        transactionTransferService.transaction(bankAccountId, dto);

        ArgumentCaptor<BankAccount> accountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(bankAccountRepository, times(2)).save(accountArgumentCaptor.capture());

        List<BankAccount> resultBankAccounts = accountArgumentCaptor.getAllValues();
        assertEquals(resultBankAccounts.get(0).getId(), bankAccount.getId());
        assertEquals(resultBankAccounts.get(0).getBalance(), balance.subtract(dto.getPrice()));
        assertEquals(resultBankAccounts.get(1).getBalance(), transferBalance.add(dto.getPrice()));


        ArgumentCaptor<TransactionHistory> transactionHistoryArgumentCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
        verify(transactionHistoryRepository).save(transactionHistoryArgumentCaptor.capture());

        TransactionHistory transactionHistory = transactionHistoryArgumentCaptor.getValue();
        assertEquals(transactionHistory.getTransactionType(), TransactionType.TRANSFER);
        assertNotNull(transactionHistory.getDate());
        assertEquals(transactionHistory.getBankAccount().getId(), bankAccountId);
        assertEquals(transactionHistory.getTransferToBankAccount().getId(), transferBankAccount.getId());

    }

    @Test
    void transactionTransfer_pinCodeNotEqualExceptional() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode("1111")
                .id(bankAccountId)
                .accountNumber(accountNumber)
                .balance(balance)
                .build();
        BankAccount transferBankAccount = BankAccount.builder()
                .pinCode("1222")
                .id(2L)
                .accountNumber(transferAccountNumber)
                .balance(BigDecimal.ZERO)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));
        when(bankAccountRepository.getByAccountNumber(transferAccountNumber)).thenReturn(Optional.of(transferBankAccount));

        PinCodeNotEqualsException e = assertThrows(
                PinCodeNotEqualsException.class,
                () -> transactionTransferService.transaction(bankAccountId, dto),
                "PinCodeNotEqualsException was expected"
        );
        assertEquals("pin-codes not equal", e.getMessage());
    }

    @Test
    void transactionTransfer_Exceptional() {
        BankAccount bankAccount = BankAccount.builder()
                .pinCode(dto.getPinCode())
                .id(bankAccountId)
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .build();
        BankAccount transferBankAccount = BankAccount.builder()
                .pinCode("1222")
                .id(2L)
                .accountNumber(transferAccountNumber)
                .balance(transferBalance)
                .build();

        when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));
        when(bankAccountRepository.getByAccountNumber(transferAccountNumber)).thenReturn(Optional.of(transferBankAccount));

        BalanceException e = assertThrows(
                BalanceException.class,
                () -> transactionTransferService.transaction(bankAccountId, dto),
                "BalanceException was expected"
        );
        assertEquals("there are not enough funds on the balance", e.getMessage());
    }
}
