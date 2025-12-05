package com.sumit.accounts.service.feignClient;

import com.sumit.accounts.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards")
public interface CardFeignClient {

    @GetMapping(value = "/api/v1/fetch",consumes = "application/json")
    public ResponseEntity<CardDto> fetchCardDetails(@RequestParam String mobileNumber);

}