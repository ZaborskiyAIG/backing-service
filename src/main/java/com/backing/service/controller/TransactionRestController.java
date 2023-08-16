package com.backing.service.controller;

import com.backing.service.dto.request.TransactionRequestDto;
import com.backing.service.dto.response.TransactionHistoryResponseDto;
import com.backing.service.entity.enums.TransactionType;
import com.backing.service.service.TransactionHistoryService;
import com.backing.service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/bank-accounts/{bankAccountId}")
@Validated
@AllArgsConstructor
public class TransactionRestController {

    private final TransactionHistoryService transactionHistoryService;
    private final Map<TransactionType, TransactionService> transactionServiceMap;

    @Operation(summary = "Получение всей истории транзакций по id конкретного счет")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение истории транзакций",
                    content = @Content(schema = @Schema(implementation = TransactionHistoryResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Счета с переданным id не существует")
    })
    @GetMapping("/page/{pageNumber}/history")
    public ResponseEntity<Page<TransactionHistoryResponseDto>> getTransactionHistoryByBankAccountId(
            @PathVariable Long bankAccountId,
            @PathVariable Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer itemsOnPage) {
        return ResponseEntity.ok(transactionHistoryService.getByBankAccountId(bankAccountId, pageNumber, itemsOnPage));
    }

    @Operation(summary = "Пополнение средств")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное пополнение средств"),
            @ApiResponse(responseCode = "400", description = "Счета с переданным id не существует"),
            @ApiResponse(responseCode = "400", description = "Передан не корректный пин-код")
    })
    @PutMapping("/deposit")
    public ResponseEntity<Void> depositBalance(@PathVariable Long bankAccountId,
                                               @RequestBody TransactionRequestDto transactionRequestDto) {
        transactionServiceMap.get(TransactionType.DEPOSIT).transaction(bankAccountId, transactionRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Снятие средств")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное снятие средств"),
            @ApiResponse(responseCode = "400", description = "Счета с переданным id не существует"),
            @ApiResponse(responseCode = "400", description = "Передан не корректный пин-код"),
            @ApiResponse(responseCode = "400", description = "На счету не хватает средств")
    })
    @PutMapping("/withdraw")
    public ResponseEntity<Void> withdrawBalance(@PathVariable Long bankAccountId,
                                                @RequestBody TransactionRequestDto transactionRequestDto) {
        transactionServiceMap.get(TransactionType.WITHDRAW).transaction(bankAccountId, transactionRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Перевод средств на другой счет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный перевод средств"),
            @ApiResponse(responseCode = "400", description = "Счета с переданным id не существует"),
            @ApiResponse(responseCode = "400", description = "Счета на перевод с переданным номером не существует"),
            @ApiResponse(responseCode = "400", description = "Передан не корректный пин-код"),
            @ApiResponse(responseCode = "400", description = "На счету не хватает средств")
    })
    @PutMapping("/transfer")
    public ResponseEntity<Void> transferBalance(@PathVariable Long bankAccountId,
                                                @RequestBody TransactionRequestDto transactionRequestDto) {
        transactionServiceMap.get(TransactionType.TRANSFER).transaction(bankAccountId, transactionRequestDto);
        return ResponseEntity.ok().build();
    }
}