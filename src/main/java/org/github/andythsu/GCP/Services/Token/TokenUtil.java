package org.github.andythsu.GCP.Services.Token;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;

public class TokenUtil {
    private static int TOKEN_LENGTH = 24;
    public static AuthToken acqureToken(){
        String token = RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);
        return new AuthToken(token);
    }
    public static boolean isTokenExpired(AuthToken token){
        return token.getExpiredAtTime().after(new Date());
    }
}
