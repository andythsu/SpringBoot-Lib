package org.github.andythsu.GCP.Services.Token;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import org.apache.commons.lang.RandomStringUtils;
import org.github.andythsu.GCP.Services.Datastore.DatastoreData;
import org.github.andythsu.GCP.Services.Datastore.DatastoreService;
import org.github.andythsu.GCP.Services.Error.MessageKey;
import org.github.andythsu.GCP.Services.Error.WebRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;

@Component
public class TokenUtil {
    private Logger log = LoggerFactory.getLogger(TokenUtil.class);

    private static int TOKEN_LENGTH = 24;

    @Autowired
    private TokenSession tokenSession;

    public AuthToken acqureToken(){
        String token = generateToken();
        AuthToken authToken = findToken(token);
        // keep looping until new token is generated
        while(authToken != null){
            log.info("{} is already in use. Regenerating...", authToken.getToken());
            token = generateToken();
            authToken = findToken(token);
        }
        authToken = new AuthToken(token);
        tokenSession.setSession(token, authToken);
        return authToken;
    }
    public String generateToken(){
        return RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);
    }
    public boolean isTokenExpired(AuthToken token){
        if (token == null) throw new WebRequestException(MessageKey.INVALID_TOKEN);
        return token.getExpiredAtTime().before(new Date());
    }
    public AuthToken findToken(String token){
        AuthToken sessionToken = findTokenInSession(token);
        if (sessionToken != null) return sessionToken;
        AuthToken dbToken = findTokenInDB(token);
        if (dbToken != null) return dbToken;
        return null;
    }
    public AuthToken findTokenInSession(String token){
        return tokenSession.getSession(token);
    }
    public AuthToken findTokenInDB(String token){
        DatastoreData dd = new DatastoreData();
        dd.put(DatastoreService.DatastoreColumns.TOKEN, token);
        Iterator<Entity> en = DatastoreService.getAllByKindAndDataEq(DatastoreService.DatastoreKinds.AUTH, dd);
        AuthToken authToken = null;
        while (en.hasNext()){
            Entity e = en.next();
            String db_token = e.getString(DatastoreService.DatastoreColumns.TOKEN);
            Timestamp db_createdAt = e.getTimestamp(DatastoreService.DatastoreColumns.CREATEDAT);
            Timestamp db_expiredAt = e.getTimestamp(DatastoreService.DatastoreColumns.EXPIREDAT);
            authToken = new AuthToken(db_token, db_createdAt, db_expiredAt);
        }
        return authToken;
    }
    public void validateToken(String token){
        AuthToken authToken = tokenSession.getSession(token);
        if (authToken == null){
            // go to db to see if there's token
            authToken = findTokenInDB(token);
            if (authToken == null){
                throw new WebRequestException(MessageKey.INVALID_TOKEN);
            }else{
                log.info("Token is found in DB. Setting session...");
                tokenSession.setSession(authToken.getToken(), authToken);
            }
        }

        if (isTokenExpired(authToken)){
            throw new WebRequestException(MessageKey.EXPIRED_TOKEN);
        }
    }
}
