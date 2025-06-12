package com.backing.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "producerv2", url = "my-java-service:8080")
public interface ProducerTest {


    @GetMapping("/ping")
    String getPing();
}
