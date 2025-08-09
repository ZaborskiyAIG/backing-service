package com.backing.service.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionAmountRsDto {
    private List<String> processed = new ArrayList<>();
    private List<String> skipped = new ArrayList<>();
}
