package com.sumit.gatewayserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// Extracts Keycloak roles from JWT token(realm_access.roles) and convert them into spring security desired format (GrantedAuthority)
// - Reads raw JWT claims
// - Extract realm_access.roles
// - Convert ["ACCOUNTS", "CARDS", "LOANS"] → ["ROLE_ACCOUNTS", "ROLE_CARDS", "ROLE_LOANS"]
// - returns Collection<GrantedAuthority>

// SUMMARY:  Given a JWT → give me authorities

public class KeycloakRoleConverter  implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        // source.getClaims() is the payload data of the JWT token
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return returnValue;
    }

}