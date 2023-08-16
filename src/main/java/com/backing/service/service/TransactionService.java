package com.backing.service.service;

import com.backing.service.dto.request.TransactionRequestDto;
import com.backing.service.entity.enums.TransactionType;

public interface TransactionService {

    void transaction(Long bankAccountId, TransactionRequestDto dto);

    TransactionType getType();
}
