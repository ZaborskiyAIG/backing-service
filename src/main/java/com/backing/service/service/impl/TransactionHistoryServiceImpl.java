package com.backing.service.service.impl;

import com.backing.service.dto.response.TransactionHistoryResponseDto;
import com.backing.service.exception.EntityNotExistException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.TransactionHistoryRepository;
import com.backing.service.service.TransactionHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public Page<TransactionHistoryResponseDto> getByBankAccountId(Long bankAccountId, Integer pageNumber, Integer itemsOnPage) {
        if (!bankAccountRepository.existsById(bankAccountId)) {
            throw new EntityNotExistException(String.format("BankAccount by id = %d not found", bankAccountId));
        }
        return transactionHistoryRepository.getByBankAccountId(bankAccountId, PageRequest.of(pageNumber - 1, itemsOnPage));
    }
}