package com.sumit.cards.service;

import com.sumit.cards.constants.AppConstant;
import com.sumit.cards.dto.CardDTO;
import com.sumit.cards.entity.Card;
import com.sumit.cards.exception.CardAlreadyExistsException;
import com.sumit.cards.exception.ResourceNotFoundException;
import com.sumit.cards.mapper.CardMapper;
import com.sumit.cards.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;
    
    public void createCard(String mobileNumber) {
        Optional<Card> optionalCard= cardRepository.findByMobileNumber(mobileNumber);
        if(optionalCard.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }

        // create new card and save in DB
        Card newCard = new Card();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(AppConstant.CREDIT_CARD);
        newCard.setTotalLimit(AppConstant.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(AppConstant.NEW_CARD_LIMIT);
        cardRepository.save(newCard);
    }
    
    public CardDTO fetchCard(String mobileNumber) {
        logger.debug("Entry CardService.fetchCard");
        Card card = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        logger.debug("Exit CardService.fetchCard");
        return CardMapper.mapToCardDto(card, new CardDTO());
    }
    
    public boolean updateCard(CardDTO CardDto) {
        Card card = cardRepository.findByCardNumber(CardDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "CardNumber", CardDto.getCardNumber()));
        CardMapper.mapToCard(CardDto, card);
        cardRepository.save(card);
        return  true;
    }
    
    public boolean deleteCard(String mobileNumber) {
        Card card = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardRepository.deleteById(card.getCardId());
        return true;
    }

}