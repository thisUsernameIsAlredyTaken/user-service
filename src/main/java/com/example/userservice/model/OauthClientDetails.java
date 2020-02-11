package com.example.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class OauthClientDetails {

    @Id
    private String clientId;

    private String resourceIds = "";

    private String clientSecret;

    private String scope = "";

    private String authorizedGrantTypes = "";

    private String webServerRedirectUri = "";

    private String authorities = "";

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String additionalInformation = "{}";

    private String autoapprove = "";

    public String getClientSecret() {
        return null;
    }
}
