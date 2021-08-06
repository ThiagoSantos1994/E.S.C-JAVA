package br.com.esc.login.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenOAuth {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;

    @Override
    public String toString() {
        return "TokenOAuth{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
