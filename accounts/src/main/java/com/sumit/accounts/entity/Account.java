package com.sumit.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(name = "accounts")
public class Account extends BaseEntity {

    @Id
    private Long accountNumber;

    private Long customerId;

    private String accountType;

    private String branchAddress;

    //private Customer customer;

}