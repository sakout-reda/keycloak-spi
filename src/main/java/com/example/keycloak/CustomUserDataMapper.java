package com.example.keycloak;

import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.protocol.oidc.mappers.OIDCIDTokenMapper;
import org.keycloak.protocol.oidc.mappers.UserInfoTokenMapper;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.protocol.oidc.mappers.OIDCIDTokenMapper;
import org.keycloak.protocol.oidc.mappers.UserInfoTokenMapper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomUserDataMapper extends AbstractOIDCProtocolMapper implements OIDCIDTokenMapper, UserInfoTokenMapper {

    private static final String USER_PROFILE_CLAIM_NAME = "user_profile";

    // Inner class for User Data Transfer Object
    public static class UserDTO {
        private String id;
        private String username;
        private String email;

        public UserDTO(String id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        // Getters and setters (optional, but good practice)
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }


    // Placeholder for fetching user data and adding it to the token

    @Override
    public String getDisplayCategory() {
        return "Token mapper";
    }

    @Override
    public String getDisplayType() {
        return "Custom User Data Mapper";
    }

    @Override
    public String getHelpText() {
        return "Adds custom user data to the token";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return new ArrayList<>();
    }

    @Override
    public String getId() {
        return "custom-user-data-mapper";
    }

    @Override
    public IDToken transformIdToken(IDToken token, UserSessionModel userSession) {
        String userId = userSession.getUser().getId(); // Extract user ID

        // Simulate fetching user data from a repository
        // In a real scenario, this would involve a database call or API request
        // UserDTO userProfile = userRepository.findById(userId);
        UserDTO userProfile = new UserDTO(userId, "user_" + userId, userId + "@example.com");

        // Add custom claim to the token
        Map<String, Object> claims = token.getOtherClaims();
        if (claims == null) {
            claims = new HashMap<>();
        }
        claims.put(USER_PROFILE_CLAIM_NAME, userProfile);
        token.setOtherClaims(claims);

        return token;
    }

    @Override
    public IDToken transformUserInfoToken(IDToken token, UserSessionModel userSession) {
        String userId = userSession.getUser().getId(); // Extract user ID

        // Simulate fetching user data from a repository
        // In a real scenario, this would involve a database call or API request
        // UserDTO userProfile = userRepository.findById(userId);
        UserDTO userProfile = new UserDTO(userId, "user_" + userId, userId + "@example.com");

        // Add custom claim to the token
        Map<String, Object> claims = token.getOtherClaims();
        if (claims == null) {
            claims = new HashMap<>();
        }
        claims.put(USER_PROFILE_CLAIM_NAME, userProfile);
        token.setOtherClaims(claims);
        return token;
    }
}
