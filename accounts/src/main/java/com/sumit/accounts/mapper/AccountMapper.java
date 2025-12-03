package com.sumit.accounts.mapper;

import com.sumit.accounts.dto.AccountDto;
import com.sumit.accounts.entity.Account;

public class AccountMapper {

    public static AccountDto mapToAccountDTO(Account account, AccountDto accountDto) {
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBranchAddress(account.getBranchAddress());
        return accountDto;
    }

    public static Account mapToAccount(AccountDto accountDto, Account account) {
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountType(accountDto.getAccountType());
        account.setBranchAddress(accountDto.getBranchAddress());
        return account;
    }

}