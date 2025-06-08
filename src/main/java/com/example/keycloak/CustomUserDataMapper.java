package com.example.keycloak;

import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CustomUserDataMapper extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper,
        OIDCIDTokenMapper, UserInfoTokenMapper {

    public static final String PROVIDER_ID = "custom-protocol-mapper";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();
    private static final Random random = new Random();
    private static final String[] NAMES = {"John", "Emma", "Michael", "Sophia", "William", "Olivia"};
    private static final String[] LASTNAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia"};
    private static final String[] COUNTRIES = {"USA", "Canada", "UK", "Australia", "Germany", "France"};

    static {
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, CustomUserDataMapper.class);
    }

    @Override
    public String getDisplayCategory() {
        return "Token Mapper";
    }

    @Override
    public String getDisplayType() {
        return "Custom Token Mapper";
    }

    @Override
    public String getHelpText() {
        return "Adds random user details (name, lastname, country) to the claim";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    protected void setClaim(IDToken token, ProtocolMapperModel mappingModel,
                            UserSessionModel userSession, KeycloakSession keycloakSession,
                            ClientSessionContext clientSessionCtx) {

        // Create a userDTO with random values
        Map<String, String> userDTO = new HashMap<>();
        userDTO.put("name", getRandomValue(NAMES));
        userDTO.put("lastname", getRandomValue(LASTNAMES));
        userDTO.put("country", getRandomValue(COUNTRIES));

        // Map the userDTO to the claim
        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, userDTO);
    }

    private String getRandomValue(String[] array) {
        return array[random.nextInt(array.length)];
    }
}
