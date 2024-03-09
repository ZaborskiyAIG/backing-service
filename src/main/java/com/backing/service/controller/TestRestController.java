package com.backing.service.controller;

import com.backing.service.client.FeignClientTest;
import com.backing.service.client.Producer;
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
@RequestMapping("/test")
@Validated
@AllArgsConstructor
@Slf4j
public class TestRestController {

    private final FeignClientTest feignClientTest;
    private final Producer producer;

    @Operation(summary = "Далем запрос на другой сервис")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получилось"),
            @ApiResponse(responseCode = "400", description = "облажался")
    })
    @GetMapping
    public ResponseEntity<String> getService() {
        log.info("отправили запрос");
        return ResponseEntity.ok(feignClientTest.getPing());
    }

    @Operation(summary = "Далем запрос на другой сервис")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получилось"),
            @ApiResponse(responseCode = "400", description = "облажался")
    })
    @GetMapping
    public ResponseEntity<String> getServiceV2() {
        log.info("отправили запрос");
        return ResponseEntity.ok(producer.getPing());
    }
}
