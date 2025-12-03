package com.sumit.accounts.controller;

import com.sumit.accounts.dto.CustomerDetailDto;
import com.sumit.accounts.service.CustomerService;
import jakarta.validation.constraints.Pattern;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/customers/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDetailDto> fetchCustomerDetails(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
        CustomerDetailDto customerDetailsDto = customerService.fetchCustomerDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
    }


}