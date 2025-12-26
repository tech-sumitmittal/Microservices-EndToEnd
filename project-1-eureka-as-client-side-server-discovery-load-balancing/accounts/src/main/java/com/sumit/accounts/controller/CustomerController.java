package com.sumit.accounts.controller;

import com.sumit.accounts.dto.CustomerDetailDTO;
import com.sumit.accounts.service.CustomerService;
import jakarta.validation.constraints.Pattern;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/customers/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDetailDTO> fetchCustomerDetails(
            @RequestHeader("sumitbank-trace-id") String traceId,
            @RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
        //logger.debug("trace-id : {}", traceId);
        logger.debug("Entry CustomerController.fetchCustomerDetails");
        CustomerDetailDTO customerDetailsDto = customerService.fetchCustomerDetails(traceId, mobileNumber);
        logger.debug("Exit CustomerController.fetchCustomerDetails");
        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
    }


}