package com.sumit.accounts.service.feignClient;

import com.sumit.accounts.dto.CardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardFallback implements CardFeignClient{

    @Override
    public ResponseEntity<CardDto> fetchCardDetails(String traceId, String mobileNumber) {
        return null;
    }

}