package com.br.esc.login.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenOAuth {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;
}
