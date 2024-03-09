package com.backing.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "producerv2", url = "produce-svc")
public interface Producer {


    @GetMapping("/ping")
    String getPing();
}
