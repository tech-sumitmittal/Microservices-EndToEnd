package com.sumit.accounts.service.feignClient;

import com.sumit.accounts.dto.LoanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanFallback implements LoanFeignClient{

    @Override
    public ResponseEntity<LoanDTO> fetchLoanDetails(String traceId, String mobileNumber) {
        return null;
    }

}