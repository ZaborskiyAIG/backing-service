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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionWithdrawServiceImpl implements TransactionService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Override
    //TODO есть подозрение, что тут как раз таки нужен REPEATABLE_READ,
    // т.к. прочитанные данные все равно могут быть изменены паралельной транзакцией (тобишь проблема неповторяющегося чтения)
    // попробовать решить эту проблему оптимистичной и пессимистичной блокировкой и разобраться как написать тесты
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transaction(Long bankAccountId, TransactionRequestDto dto) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new EntityNotExistException(
                String.format("BankAccount by id = %d not found", bankAccountId)
        ));
        BankAccountValidation.equalsPinCode(bankAccount.getPinCode(), dto.getPinCode());
        BankAccountValidation.checkBalance(bankAccount.getBalance(), dto.getPrice());

        //TODO нужна ли тут транзакция? можем ли мы где то паралельно снять деньги и уйти в минус
        //надо проверить этот код с разными уровнями транзакций
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