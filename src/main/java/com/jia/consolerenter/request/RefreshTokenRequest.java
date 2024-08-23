package com.jia.consolerenter.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
