package com.backing.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "producer", url = "/ping")
public interface FeignClientTest {


    @GetMapping
    String getPing();
}
