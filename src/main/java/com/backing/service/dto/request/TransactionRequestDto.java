package com.backing.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {

    @NotBlank(message = "пин-код не должен быть пустым")
    @Pattern(regexp="[\\d]{4}", message = "пин-код должен состоять из 4 цифр")
    private String pinCode;

    @NotNull
    private BigDecimal price;

    private String transferAccountNumber;

    public TransactionRequestDto(String pinCode, BigDecimal price) {
        this.pinCode = pinCode;
        this.price = price;
    }
}