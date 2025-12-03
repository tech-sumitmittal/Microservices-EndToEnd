package com.sumit.accounts.dto;

import lombok.Data;

@Data
public class CustomerDetailDto {

    private String name;
    private String email;
    private String mobileNumber;
    private AccountDto accountDto;
    private LoanDto loanDto;
    private CardDto cardDto;


}