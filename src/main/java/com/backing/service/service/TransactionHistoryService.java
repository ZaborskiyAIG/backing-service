package com.backing.service.service;

import com.backing.service.dto.response.TransactionHistoryResponseDto;
import org.springframework.data.domain.Page;

public interface TransactionHistoryService {

    Page<TransactionHistoryResponseDto> getByBankAccountId(Long bankAccountId, Integer pageNumber, Integer itemsOnPage);
}
