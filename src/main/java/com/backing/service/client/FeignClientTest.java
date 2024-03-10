package com.backing.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "producer", url = "${PRODUCER_SVC_SERVICE_HOST:100}")
public interface FeignClientTest {


    @GetMapping("/ping")
    String getPing();
}
