package com.example.keycloak;

import com.example.keycloak.entity.User;
import com.example.keycloak.repositories.UserRepository;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CustomUserDataMapper extends AbstractOIDCProtocolMapper
        implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDataMapper.class);
    public static final String PROVIDER_ID = "custom-protocol-mapper";
    public static final String DISPLAY_TYPE = "Custom Token Mapper";
    public static final String DISPLAY_CATEGORY = "Token Mapper";
    public static final String HELP_TEXT = "Adds user details (name, lastname, country) to the claim";

    private static final String USER_DTO_CLAIM = "userDTO";
    private static final String NAME_CLAIM = "name";
    private static final String LASTNAME_CLAIM = "lastname";
    private static final String COUNTRY_CLAIM = "country";

    private static final UserRepository userRepository = new UserRepository();
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, CustomUserDataMapper.class);
    }

    @Override
    public String getDisplayCategory() {
        return DISPLAY_CATEGORY;
    }

    @Override
    public String getDisplayType() {
        return DISPLAY_TYPE;
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.unmodifiableList(configProperties);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    protected void setClaim(IDToken token, ProtocolMapperModel mappingModel,
                            UserSessionModel userSession, KeycloakSession keycloakSession,
                            ClientSessionContext clientSessionCtx) {

        String lastName = userSession.getUser().getLastName();
        logger.debug("Attempting to map claims for user with last name: {}", lastName);

        userRepository.findByLastName(lastName).ifPresent(user -> {
            logUserDetails(user);
            addUserClaimsToToken(token, user);
        });
    }

    private void logUserDetails(User user) {
        logger.debug("Mapping user details - firstName: {}, lastName: {}, country: {}",
                user.getFirstName(),
                user.getLastName(),
                user.getCountry());
    }

    private void addUserClaimsToToken(IDToken token, User user) {
        Map<String, Object> userClaims = new HashMap<>();
        userClaims.put(NAME_CLAIM, user.getFirstName());
        userClaims.put(LASTNAME_CLAIM, user.getLastName());
        userClaims.put(COUNTRY_CLAIM, user.getCountry());

        token.getOtherClaims().put(USER_DTO_CLAIM, Collections.unmodifiableMap(userClaims));
    }
}
