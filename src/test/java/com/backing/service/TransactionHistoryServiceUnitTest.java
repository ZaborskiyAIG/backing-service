package com.backing.service;

import com.backing.service.dto.response.TransactionHistoryResponseDto;
import com.backing.service.entity.enums.TransactionType;
import com.backing.service.exception.EntityNotExistException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.TransactionHistoryRepository;
import com.backing.service.service.impl.TransactionHistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionHistoryServiceUnitTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private TransactionHistoryServiceImpl transactionHistoryService;

    private final long bankAccountId = 1L;

    @Test
    void getByBankAccountIdSuccessful() {
        int pageNumber = 1;
        int itemsOnPage = 10;
        LocalDateTime date = LocalDateTime.of(2023, 12, 12, 15, 5);
        List<TransactionHistoryResponseDto> transactionHistoryResponseDtos = List.of(
                new TransactionHistoryResponseDto(1L, 1L, BigDecimal.valueOf(400L), TransactionType.DEPOSIT, date),
                new TransactionHistoryResponseDto(2L, 1L, BigDecimal.valueOf(300L), TransactionType.WITHDRAW, date),
                new TransactionHistoryResponseDto(3L, 1L, BigDecimal.valueOf(700L), TransactionType.TRANSFER, 2L, date),
                new TransactionHistoryResponseDto(4L, 1L, BigDecimal.valueOf(800L), TransactionType.DEPOSIT, date),
                new TransactionHistoryResponseDto(5L, 1L, BigDecimal.ZERO, TransactionType.WITHDRAW, date)
        );
        Page<TransactionHistoryResponseDto> page = new PageImpl<>(transactionHistoryResponseDtos);

        when(bankAccountRepository.existsById(bankAccountId)).thenReturn(true);
        when(transactionHistoryRepository.getByBankAccountId(bankAccountId, PageRequest.of(pageNumber - 1, itemsOnPage)))
                .thenReturn(page);

        Page<TransactionHistoryResponseDto> result = transactionHistoryService.getByBankAccountId(bankAccountId, pageNumber, itemsOnPage);

        assertEquals(result.getTotalElements(), transactionHistoryResponseDtos.size());
        TransactionHistoryResponseDto content = result.getContent().get(0);
        assertEquals(content, transactionHistoryResponseDtos.get(0));

        TransactionHistoryResponseDto content1 = result.getContent().get(1);
        assertEquals(content1, transactionHistoryResponseDtos.get(1));

        TransactionHistoryResponseDto content2 = result.getContent().get(2);
        assertEquals(content2, transactionHistoryResponseDtos.get(2));

        TransactionHistoryResponseDto content3 = result.getContent().get(3);
        assertEquals(content3, transactionHistoryResponseDtos.get(3));

        TransactionHistoryResponseDto content4 = result.getContent().get(4);
        assertEquals(content4, transactionHistoryResponseDtos.get(4));
    }

    @Test
    void getByBankAccountId_notFoundBankAccountExceptional() {
        int pageNumber = 1;
        int itemsOnPage = 10;
        when(bankAccountRepository.existsById(any())).thenReturn(false);

        EntityNotExistException e = assertThrows(
                EntityNotExistException.class,
                () -> transactionHistoryService.getByBankAccountId(bankAccountId, pageNumber, itemsOnPage),
                "EntityNotExistException was expected"
        );
        assertEquals(String.format("BankAccount by id = %d not found", bankAccountId), e.getMessage());
    }
}