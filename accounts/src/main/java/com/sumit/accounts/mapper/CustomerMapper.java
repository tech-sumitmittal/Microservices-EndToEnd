package com.sumit.accounts.mapper;

import com.sumit.accounts.dto.CustomerDetailDto;
import com.sumit.accounts.dto.CustomerDto;
import com.sumit.accounts.entity.Customer;

public class CustomerMapper {

    public static CustomerDto mapToCustomerDTO(Customer customer, CustomerDto customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }

    public static CustomerDetailDto mapToCustomerDetailDto(Customer customer, CustomerDetailDto customerDetailsDto) {
        customerDetailsDto.setName(customer.getName());
        customerDetailsDto.setEmail(customer.getEmail());
        customerDetailsDto.setMobileNumber(customer.getMobileNumber());
        return customerDetailsDto;
    }

}