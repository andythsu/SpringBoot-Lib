package org.github.andythsu.GCP.Services.Token;

import com.google.cloud.Timestamp;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthToken {
    private String token;
    private Timestamp createdAt;
    private Timestamp expiredAt;
    private int expire_in_mins = 30;
    public AuthToken(String token){
        this.token = token;
        Date d = new Date();
        this.createdAt = Timestamp.of(d);
        this.expiredAt = Timestamp.of(DateUtils.addMinutes(d, expire_in_mins));
    }

    public String getToken() {
        return token;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public Date getExpiredAtTime(){
        return expiredAt.toDate();
    }
}
