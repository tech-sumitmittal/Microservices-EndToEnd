package com.sumit.accounts.controller;

import com.sumit.accounts.dto.ContactInfoDTO;
import com.sumit.accounts.dto.CustomerDTO;
import com.sumit.accounts.dto.ResponseDTO;
import com.sumit.accounts.service.AccountService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private ContactInfoDTO contactInfoDto;

    @Autowired
    private Environment environment;


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createAccount(@Valid @RequestBody CustomerDTO customerDto) {
        log.info("Entry AccountController.createAccount : customerDto = {}", customerDto);
        accountService.createAccount(customerDto);
        ResponseDTO responseDto = new ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
        log.info("Exit AccountController.createAccount : responseDto = {} !!!", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateAccountDetails(@Valid @RequestBody CustomerDTO customerDto) {
        log.info("Entry AccountController.updateAccountDetails : customerDto = {}", customerDto);
        boolean isUpdated = accountService.updateAccount(customerDto);
        log.info("Exit AccountController.updateAccountDetails : isUpdated = {} !!!", isUpdated);
        if(isUpdated)
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDTO> fetchAccountDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Entry AccountController.fetchAccountDetails : mobileNumber = {}", mobileNumber);
        CustomerDTO customerDto = accountService.fetchAccount(mobileNumber);
        log.info("Exit AccountController.fetchAccountDetails : customerDto = {} !!!", customerDto);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteAccountDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Entry AccountController.deleteAccountDetails : mobileNumber = {}", mobileNumber);
        boolean isDeleted = accountService.deleteAccount(mobileNumber);
        log.info("Exit AccountController.deleteAccountDetails : isDeleted = {} !!!", isDeleted);
        if(isDeleted)
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<ContactInfoDTO> getContactInfo() {
        log.info("Entry AccountController.getContactInfo.");
        return ResponseEntity.status(HttpStatus.OK)
                             .body(contactInfoDto);
    }

    // 2. Resilience4j retry with fallback mechanism
    @Retry(name = "getBuildInfo",fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        log.info("Entry AccountController.getBuildInfo.");
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildVersion);
    }

    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
        log.info("Entry AccountController.getBuildInfoFallback.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("0.9");
    }

    @RateLimiter(name= "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        log.info("Entry AccountController.getJavaVersion.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
        log.info("Entry AccountController.getJavaVersionFallback.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Java 21");
    }

}