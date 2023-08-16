package com.backing.service.service.impl.transaction;

import com.backing.service.dto.request.TransactionRequestDto;
import com.backing.service.entity.BankAccount;
import com.backing.service.entity.TransactionHistory;
import com.backing.service.entity.enums.TransactionType;
import com.backing.service.exception.EntityNotExistException;
import com.backing.service.repository.BankAccountRepository;
import com.backing.service.repository.TransactionHistoryRepository;
import com.backing.service.service.TransactionService;
import com.backing.service.util.BankAccountValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionWithdrawServiceImpl implements TransactionService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Override
    @Transactional
    public void transaction(Long bankAccountId, TransactionRequestDto dto) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new EntityNotExistException(
                String.format("BankAccount by id = %d not found", bankAccountId)
        ));
        BankAccountValidation.equalsPinCode(bankAccount.getPinCode(), dto.getPinCode());
        BankAccountValidation.checkBalance(bankAccount.getBalance(), dto.getPrice());

        bankAccount.minusBalance(dto.getPrice());

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .transactionType(TransactionType.WITHDRAW)
                .price(dto.getPrice())
                .bankAccount(bankAccount)
                .date(LocalDateTime.now())
                .build();
        bankAccountRepository.save(bankAccount);
        transactionHistoryRepository.save(transactionHistory);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAW;
    }
}