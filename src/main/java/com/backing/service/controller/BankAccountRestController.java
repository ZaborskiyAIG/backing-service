package com.backing.service.controller;

import com.backing.service.dto.request.BankAccountRequestDto;
import com.backing.service.dto.response.BeneficiaryBankAccountResponseDto;
import com.backing.service.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank-accounts")
@Validated
@AllArgsConstructor
public class BankAccountRestController {

    private final BankAccountService bankAccountService;

    @Operation(summary = "Создание счета")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректный пин-код"),
            @ApiResponse(responseCode = "400", description = "Пользователя с таким username не существует")
    })
    @PostMapping
    public ResponseEntity<Void> createBankAccount(@Valid @RequestBody BankAccountRequestDto bankAccountDto) {
        bankAccountService.createBankAccount(bankAccountDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение всех пользователей с их счетами")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователи успешно получены",
                    content = @Content(schema = @Schema(implementation = BeneficiaryBankAccountResponseDto.class))
            )
    })
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Page<BeneficiaryBankAccountResponseDto>> getAllBankAccounts(
            @PathVariable Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer itemsOnPage) {
        return ResponseEntity.ok(bankAccountService.getBeneficiaryBankAccountResponseDto(pageNumber, itemsOnPage));
    }
}