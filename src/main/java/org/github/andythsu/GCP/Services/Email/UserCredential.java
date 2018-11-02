package org.github.andythsu.GCP.Services.Email;

/**
 * @author: Andy Su
 * @Date: 11/2/2018
 */
public class UserCredential {
    private final String userName;
    private final String password;

    public UserCredential(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
