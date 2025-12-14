package com.sumit.loans.service;

import com.sumit.loans.constants.AppConstant;
import com.sumit.loans.dto.LoanDto;
import com.sumit.loans.entity.Loan;
import com.sumit.loans.exception.LoanAlreadyExistsException;
import com.sumit.loans.exception.ResourceNotFoundException;
import com.sumit.loans.mapper.LoanMapper;
import com.sumit.loans.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
public class LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    @Autowired
    private LoanRepository loanRepository;

    public void createLoan(String mobileNumber) {
        Optional<Loan> optionalLoan= loanRepository.findByMobileNumber(mobileNumber);
        if(optionalLoan.isPresent())
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber "+mobileNumber);

        // create new loan and save in DB
        Loan newLoan = new Loan();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(AppConstant.DEFAULT_LOAN_TYPE_HOME_LOAN);
        newLoan.setTotalLoan(AppConstant.DEFAULT_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(AppConstant.DEFAULT_LOAN_LIMIT);
        loanRepository.save(newLoan);
    }

    public LoanDto fetchLoan(String mobileNumber) {
        logger.debug("Entry LoanService.fetchLoan");
        Loan loan = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        logger.debug("Exit LoanService.fetchLoan");
        return LoanMapper.mapToLoanDto(loan, new LoanDto());
    }

    public boolean updateLoan(com.sumit.loans.dto.LoanDto loanDto) {
        Loan loan = loanRepository.findByLoanNumber(loanDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loanDto.getLoanNumber()));
        LoanMapper.mapToLoan(loanDto, loan);
        loanRepository.save(loan);
        return  true;
    }

    public boolean deleteLoan(String mobileNumber) {
        Loan loan = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loanRepository.deleteById(loan.getLoanId());
        return true;
    }


}