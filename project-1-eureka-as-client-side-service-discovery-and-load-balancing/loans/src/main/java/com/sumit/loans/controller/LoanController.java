package com.sumit.loans.controller;

import com.sumit.loans.dto.ContactInfoDTO;
import com.sumit.loans.dto.LoanDTO;
import com.sumit.loans.dto.ResponseDTO;
import com.sumit.loans.service.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    private LoanService loanService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private ContactInfoDTO contactInfoDto;


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createLoan(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Entry LoanController.createLoan : mobileNumber = {}", mobileNumber);
        loanService.createLoan(mobileNumber);
        log.info("Exit LoanController.createLoan !!!");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoanDTO> fetchLoanDetails(
            @RequestHeader("sumitbank-trace-id") String traceId,
            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        //logger.debug("trace-id : {}", traceId);
        log.info("Entry LoanController.fetchLoanDetails : mobileNumber = {}", mobileNumber);
        LoanDTO loanDto = loanService.fetchLoan(mobileNumber);
        log.info("Exit LoanController.fetchLoanDetails, loanDto = {} !!!", loanDto);
        return ResponseEntity.status(HttpStatus.OK).body(loanDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateLoanDetails(@Valid @RequestBody LoanDTO loanDto) {
        log.info("Entry LoanController.updateLoanDetails : loanDto = {}", loanDto);
        boolean isUpdated = loanService.updateLoan(loanDto);
        log.info("Exit LoanController.updateLoanDetails, isUpdated = {} !!!", isUpdated);
        if(isUpdated)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteLoanDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Entry LoanController.deleteLoanDetails : mobileNumber = {}", mobileNumber);
        boolean isDeleted = loanService.deleteLoan(mobileNumber);
        log.info("Exit LoanController.deleteLoanDetails, isDeleted = {} !!!", isDeleted);
        if(isDeleted)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        log.info("Entry LoanController.getBuildInfo.");
        return ResponseEntity.status(HttpStatus.OK)
                             .body(buildVersion);
    }

    @GetMapping("/contact-info")
    public ResponseEntity<ContactInfoDTO> getContactInfo() {
        log.info("Entry LoanController.getContactInfo.");
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactInfoDto);
    }


}