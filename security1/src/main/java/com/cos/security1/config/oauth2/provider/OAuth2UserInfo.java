package com.cos.security1.config.oauth2.provider;

public interface OAuth2UserInfo {
    String getProviderId();     // pk
    String getProvider();       // google, facebook, etc
    String getEmail();
    String getName();
}
