package com.sumit.accounts.dto;

import lombok.Data;

@Data
public class CustomerDetailDTO {

    private String name;
    private String email;
    private String mobileNumber;
    private AccountDTO accountDto;
    private LoanDTO loanDto;
    private CardDTO cardDto;


}