package com.backing.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
@Validated
@AllArgsConstructor
@Slf4j
public class TestRestController {


    @Operation(summary = "Далем запрос на другой сервис")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получилось"),
            @ApiResponse(responseCode = "400", description = "облажался")
    })
    @GetMapping
    public String getPing() {
        log.info("приняли запрос");
        return "Заработало";
    }
}
