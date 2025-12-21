package com.sumit.accounts.service.feignClient;

import com.sumit.accounts.dto.LoanDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="loans", fallback = LoanFallback.class)
public interface LoanFeignClient {

    @GetMapping(value = "/api/v1/fetch",consumes = "application/json")
    ResponseEntity<LoanDTO> fetchLoanDetails(@RequestHeader("sumitbank-trace-id") String traceId, @RequestParam String mobileNumber);

}