package com.sumit.cards.controller;

import com.sumit.cards.dto.CardDto;
import com.sumit.cards.dto.ContactInfoDto;
import com.sumit.cards.dto.ResponseDto;
import com.sumit.cards.service.CardService;
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
@RequestMapping(value = "/cards/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CardController {

    @Autowired
    private CardService cardService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private ContactInfoDto contactInfoDto;


    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        cardService.createCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }
    
    @GetMapping("/fetch")
    public ResponseEntity<CardDto> fetchCardDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        CardDto cardDto = cardService.fetchCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardDto cardDto) {
        boolean isUpdated = cardService.updateCard(cardDto);
        if(isUpdated)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.getReasonPhrase()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") @RequestParam String mobileNumber) {
        boolean isDeleted = cardService.deleteCard(mobileNumber);
        if(isDeleted)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        else
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.getReasonPhrase()));
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