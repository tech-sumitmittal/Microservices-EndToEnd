package com.sumit.accounts.service;

import com.sumit.accounts.dto.AccountDTO;
import com.sumit.accounts.dto.CardDTO;
import com.sumit.accounts.dto.CustomerDetailDTO;
import com.sumit.accounts.dto.LoanDTO;
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


    public CustomerDetailDTO fetchCustomerDetails(String traceId, String mobileNumber) {
        //logger.debug("trace-id : {}", traceId);
        logger.debug("Entry CustomerService.fetchCustomerDetails");

        // customer and account information
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailDTO customerDetailDto = CustomerMapper.mapToCustomerDetailDto(customer, new CustomerDetailDTO());
        customerDetailDto.setAccountDto(AccountMapper.mapToAccountDTO(account, new AccountDTO()));

        // Card information from
        ResponseEntity<CardDTO> cardDtoResponseEntity = cardFeignClient.fetchCardDetails(traceId, mobileNumber);
        if(cardDtoResponseEntity != null)
            customerDetailDto.setCardDto(cardDtoResponseEntity.getBody());

        // Loan information
        ResponseEntity<LoanDTO> loanDtoResponseEntity = loanFeignClient.fetchLoanDetails(traceId, mobileNumber);
        if(loanDtoResponseEntity != null)
            customerDetailDto.setLoanDto(loanDtoResponseEntity.getBody());

        logger.debug("Exit CustomerService.fetchCustomerDetails");
        return customerDetailDto;
    }


}