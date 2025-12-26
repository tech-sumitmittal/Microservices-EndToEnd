package com.sumit.accounts.service.feignClient;

import com.sumit.accounts.dto.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="cards", url = "http://cards:8081", fallback = CardFallback.class)
public interface CardFeignClient {

    @GetMapping(value = "/api/v1/fetch",consumes = "application/json")
    ResponseEntity<CardDTO> fetchCardDetails(@RequestHeader("sumitbank-trace-id") String traceId, @RequestParam String mobileNumber);

}