package com.example.keycloak;

import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapperFactory;
import org.keycloak.protocol.oidc.mappers.ProtocolMapperUtils;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDataMapperFactory extends AbstractOIDCProtocolMapperFactory {

    public static final String PROVIDER_ID = "custom-user-data-mapper";

    @Override
    public AbstractOIDCProtocolMapper create(ProtocolMapperModel mapping) {
        return new CustomUserDataMapper();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayCategory() {
        return ProtocolMapperUtils.USER_MODEL_CATEGORY;
    }

    @Override
    public String getDisplayType() {
        return "Custom User Data Mapper";
    }

    @Override
    public String getHelpText() {
        return "Adds custom user data (DTO) to the OIDC token claims.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return new ArrayList<>();
    }
}
