package com.backing.service.config;

import com.backing.service.entity.enums.TransactionType;
import com.backing.service.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ConfigurationTransactionService {

    @Bean
    public Map<TransactionType, TransactionService> getTransactionServiceMap(List<TransactionService> transactionServices) {
        Map<TransactionType, TransactionService> map = new HashMap<>();

        for (TransactionService transactionService : transactionServices) {
            map.put(transactionService.getType(), transactionService);
        }
        return map;
    }
}