package com.sumit.cards.controller;

import com.sumit.cards.dto.CardDTO;
import com.sumit.cards.dto.ContactInfoDTO;
import com.sumit.cards.dto.ResponseDTO;
import com.sumit.cards.service.CardService;
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
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardService cardService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private ContactInfoDTO contactInfoDto;


    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createCard(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Entry CardController.createCard : mobileNumber = {}", mobileNumber);
        cardService.createCard(mobileNumber);
        log.info("Exit CardController.createCard !!!");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }
    
    @GetMapping("/fetch")
    public ResponseEntity<CardDTO> fetchCardDetails(
            @RequestHeader("sumitbank-trace-id") String traceId,
            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        //logger.debug("trace-id : {}", traceId);
        log.info("Entry CardController.fetchCardDetails : mobileNumber = {}", mobileNumber);
        CardDTO cardDto = cardService.fetchCard(mobileNumber);
        log.info("Exit CardController.fetchCardDetails : cardDto = {} !!!", cardDto);
        return ResponseEntity.status(HttpStatus.OK).body(cardDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateCardDetails(@Valid @RequestBody CardDTO cardDto) {
        log.info("Entry CardController.updateCardDetails : cardDto = {}", cardDto);
        boolean isUpdated = cardService.updateCard(cardDto);
        log.info("Exit CardController.updateCardDetails : isUpdated = {} !!!", isUpdated);
        if(isUpdated)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDTO(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.getReasonPhrase()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteCardDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        log.info("Entry CardController.deleteCardDetails : mobileNumber = {}", mobileNumber);
        boolean isDeleted = cardService.deleteCard(mobileNumber);
        log.info("Exit CardController.deleteCardDetails : isDeleted = {} !!!", isDeleted);
        if(isDeleted)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDTO(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.getReasonPhrase()));
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        log.info("Entry CardController.getBuildInfo.");
        return ResponseEntity.status(HttpStatus.OK)
                             .body(buildVersion);
    }

    @GetMapping("/contact-info")
    public ResponseEntity<ContactInfoDTO> getContactInfo() {
        log.info("Entry CardController.getContactInfo.");
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactInfoDto);
    }


}