package com.sumit.accounts.service;

import com.sumit.accounts.constant.AppConstant;
import com.sumit.accounts.dto.AccountDto;
import com.sumit.accounts.dto.CustomerDto;
import com.sumit.accounts.entity.Account;
import com.sumit.accounts.entity.Customer;
import com.sumit.accounts.exception.CustomerAlreadyExistsException;
import com.sumit.accounts.exception.ResourceNotFoundException;
import com.sumit.accounts.mapper.AccountMapper;
import com.sumit.accounts.mapper.CustomerMapper;
import com.sumit.accounts.repository.AccountRepository;
import com.sumit.accounts.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public void createAccount(CustomerDto customerDto) {
        // check if any customer exists with same mobile no
        Optional<Customer> dbCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(dbCustomer.isPresent())
            throw new CustomerAlreadyExistsException("Customer already exists with mobile no "+customerDto.getMobileNumber());

        // create customer object
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);

        // create account object
        Long accountNum = 1000000000L + new Random().nextInt(900000000);
        Account account = new Account();
        account.setCustomerId(savedCustomer.getCustomerId());
        account.setAccountNumber(accountNum);
        account.setAccountType(AppConstant.DEFAULT_ACCOUNT_TYPE_SAVING);
        account.setBranchAddress(AppConstant.DEFAULT_BANK_BRANCH_ADDRESS);
        //account.setCustomer(customer);            TODO
        accountRepository.save(account);
    }

    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDTO(customer, new CustomerDto());
        customerDto.setAccountDto(AccountMapper.mapToAccountDTO(account, new AccountDto()));
        return customerDto;
    }

    public boolean updateAccount(@Valid CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountDto accountDto = customerDto.getAccountDto();
        if(accountDto !=null ){

            // update account details in DB
            Account account = accountRepository.findById(accountDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccount(accountDto, account);
            account = accountRepository.save(account);

            // update customer details in DB
            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }


}