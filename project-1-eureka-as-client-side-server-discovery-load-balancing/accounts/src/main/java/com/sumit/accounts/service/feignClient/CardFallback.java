package com.sumit.accounts.service.feignClient;

import com.sumit.accounts.dto.CardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardFallback implements CardFeignClient{

    @Override
    public ResponseEntity<CardDTO> fetchCardDetails(String traceId, String mobileNumber) {
        return null;
    }

}