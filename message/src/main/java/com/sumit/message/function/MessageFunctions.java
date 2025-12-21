package com.sumit.message.function;

import com.sumit.message.dto.AccountMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<AccountMessageDto, AccountMessageDto> email() {
        return msgDTO -> {
            // write logic to send email
            log.info("Sending email with the details : {}", msgDTO.toString());
            return msgDTO;
        };
    }

    @Bean
    public Function<AccountMessageDto,Long> sms() {
        return msgDTO -> {
            // write logic to send sms
            log.info("Sending sms with the details : {}", msgDTO.toString());
            return msgDTO.accountNumber();
        };
    }

}