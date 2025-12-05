package com.sumit.loans.controller;

import com.sumit.loans.dto.ContactInfoDto;
import com.sumit.loans.dto.LoanDto;
import com.sumit.loans.dto.ResponseDto;
import com.sumit.loans.service.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private ContactInfoDto contactInfoDto;


    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        loanService.createLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoanDto> fetchLoanDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        LoanDto loanDto = loanService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loanDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoanDto loanDto) {
        boolean isUpdated = loanService.updateLoan(loanDto);
        if(isUpdated)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoanDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        boolean isDeleted = loanService.deleteLoan(mobileNumber);
        if(isDeleted)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(buildVersion);
    }

    @GetMapping("/contact-info")
    public ResponseEntity<ContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactInfoDto);
    }


}