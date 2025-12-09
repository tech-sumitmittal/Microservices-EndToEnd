package com.sumit.accounts.service.feignClient;

import com.sumit.accounts.dto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanFallback implements LoanFeignClient{

    @Override
    public ResponseEntity<LoanDto> fetchLoanDetails(String traceId, String mobileNumber) {
        return null;
    }

}