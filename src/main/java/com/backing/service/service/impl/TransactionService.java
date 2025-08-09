package com.backing.service.service.impl;

import com.backing.service.dto.request.TransactionAmountRqDto;
import com.backing.service.dto.response.TransactionAmountRsDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TransactionService {

    private final ConcurrentHashMap<String, TransactionAmountRqDto> inMemoryCash = new ConcurrentHashMap<>();

    private final Deque<String> logs = new ArrayDeque<>();

    private final BlockingQueue<TransactionAmountRqDto> queue = new LinkedBlockingQueue<>();


    @SneakyThrows
    public TransactionAmountRsDto transactionHandler(List<TransactionAmountRqDto> transactionDto) {
        TransactionAmountRsDto transactionAmountRsDto = new TransactionAmountRsDto();
        for (TransactionAmountRqDto transactionAmountRqDto : transactionDto) {
            if (transactionAmountRqDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                transactionAmountRsDto.getSkipped().add(transactionAmountRqDto.getTransactionId());
                continue;
            }

            TransactionAmountRqDto result = inMemoryCash.putIfAbsent(transactionAmountRqDto.getTransactionId(), transactionAmountRqDto);
            if (result == null) {
                transactionAmountRsDto.getProcessed().add(transactionAmountRqDto.getTransactionId());
            } else {
                transactionAmountRsDto.getSkipped().add(transactionAmountRqDto.getTransactionId());
            }

            logTransaction(String.format("Processed tx %s amount %.2f %s",
                    transactionAmountRqDto.getTransactionId(), transactionAmountRqDto.getAmount(), transactionAmountRqDto.getCurrency()));
            queue.offer(transactionAmountRqDto);
        }
        return transactionAmountRsDto;
    }

    public synchronized void logTransaction(String entry) {
        if (logs.size() >= 10_000) {
            logs.removeFirst(); // удаляем старую запись
        }
        logs.addLast(entry);
    }
}