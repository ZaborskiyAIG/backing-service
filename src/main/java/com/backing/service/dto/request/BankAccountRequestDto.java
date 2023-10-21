package com.backing.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankAccountRequestDto {

    @NotBlank(message = "username не должен быть пустым")
    private String username;


    @NotBlank(message = "пин-код не должен быть пустым")
    @Pattern(regexp="[\\d]{4}", message = "пин-код должен состоять из 4 цифр")
    private String pinCode;
}