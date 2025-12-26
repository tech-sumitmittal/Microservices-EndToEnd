package com.sumit.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    public Optional<String> getCurrentAuditor() {
        // TODO need to be dynamic when we will implement the spring security
        return Optional.of("ACCOUNTS_MS");
    }

}