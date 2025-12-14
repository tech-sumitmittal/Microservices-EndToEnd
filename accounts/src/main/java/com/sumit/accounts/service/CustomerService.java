package com.sumit.accounts.service;

import com.sumit.accounts.dto.AccountDto;
import com.sumit.accounts.dto.CardDto;
import com.sumit.accounts.dto.CustomerDetailDto;
import com.sumit.accounts.dto.LoanDto;
import com.sumit.accounts.entity.Account;
import com.sumit.accounts.entity.Customer;
import com.sumit.accounts.exception.ResourceNotFoundException;
import com.sumit.accounts.mapper.AccountMapper;
import com.sumit.accounts.mapper.CustomerMapper;
import com.sumit.accounts.repository.AccountRepository;
import com.sumit.accounts.repository.CustomerRepository;
import com.sumit.accounts.service.feignClient.CardFeignClient;
import com.sumit.accounts.service.feignClient.LoanFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private CardFeignClient cardFeignClient;
    private LoanFeignClient loanFeignClient;


    public CustomerDetailDto fetchCustomerDetails(String traceId, String mobileNumber) {
        //logger.debug("trace-id : {}", traceId);
        logger.debug("Entry CustomerService.fetchCustomerDetails");

        // customer and account information
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailDto customerDetailDto = CustomerMapper.mapToCustomerDetailDto(customer, new CustomerDetailDto());
        customerDetailDto.setAccountDto(AccountMapper.mapToAccountDTO(account, new AccountDto()));

        // Card information from
        ResponseEntity<CardDto> cardDtoResponseEntity = cardFeignClient.fetchCardDetails(traceId, mobileNumber);
        if(cardDtoResponseEntity != null)
            customerDetailDto.setCardDto(cardDtoResponseEntity.getBody());

        // Loan information
        ResponseEntity<LoanDto> loanDtoResponseEntity = loanFeignClient.fetchLoanDetails(traceId, mobileNumber);
        if(loanDtoResponseEntity != null)
            customerDetailDto.setLoanDto(loanDtoResponseEntity.getBody());

        logger.debug("Exit CustomerService.fetchCustomerDetails");
        return customerDetailDto;
    }


}