package com.backing.service.controller;

import com.backing.service.dto.request.TransactionAmountRqDto;
import com.backing.service.dto.response.TransactionAmountRsDto;
import com.backing.service.service.impl.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@Validated
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionAmountRsDto> transactionHandler(@RequestBody List<TransactionAmountRqDto> transactionDto) {
        return ResponseEntity.ok(transactionService.transactionHandler(transactionDto));
    }
}