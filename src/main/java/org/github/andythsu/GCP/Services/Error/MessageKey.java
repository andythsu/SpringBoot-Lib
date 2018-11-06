package org.github.andythsu.GCP.Services.Error;

import java.net.HttpURLConnection;

public class MessageKey {

    private int status = 500;
    private String tag = "GENERIC-TAG";
    private String defaultMessage = "GENERIC-MESSAGE";

    public static final MessageKey INVALID_PARAM = new MessageKey(
            HttpURLConnection.HTTP_BAD_REQUEST,
            MessageKeyTags.INVALID_PARAM,
            "Invalid Parameter");

    public static final MessageKey INVALID_JSON = new MessageKey(
            HttpURLConnection.HTTP_BAD_REQUEST,
            MessageKeyTags.INVALID_JSON,
            "Invalid JSON format");

    public static final MessageKey DATA_FETCH_ERROR = new MessageKey(
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            MessageKeyTags.DATA_ERROR,
            "Error Fetching Data");

    public static final MessageKey NETWORK_ERROR = new MessageKey(
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
            MessageKeyTags.NETWORK_ERROR,
            "Network Error");

    public static final MessageKey UNAUTHORIZED = new MessageKey(
            HttpURLConnection.HTTP_UNAUTHORIZED,
            MessageKeyTags.CREDENTIAL_ERROR,
            "Invalid Credentials");

    public static final MessageKey INVALID_TOKEN = new MessageKey(
            HttpURLConnection.HTTP_UNAUTHORIZED,
            MessageKeyTags.TOKEN_ERROR,
            "Invalid Token");

    public static final MessageKey EXPIRED_TOKEN = new MessageKey(
            HttpURLConnection.HTTP_UNAUTHORIZED,
            MessageKeyTags.TOKEN_ERROR,
            "Token has expired. Request a new one");


    public static class MessageKeyTags {
        public static final String DATA_ERROR = "DATA-ERROR";
        public static final String INVALID_JSON = "INVALID-JSON";
        public static final String INVALID_PARAM = "INVALID-PARAM";
        public static final String NETWORK_ERROR = "NETWORK-ERROR";
        public static final String CREDENTIAL_ERROR = "CREDENTIAL-ERROR";
        public static final String TOKEN_ERROR = "TOKEN-ERROR";
        public static final String RUN_TIME_ERROR = "RUN-TIME-ERROR";
    }

    public MessageKey(final int status, final String tag, final String defaultMessage) {
        this.status = status;
        this.tag = tag;
        this.defaultMessage = defaultMessage;
    }


    public String getTag() {
        return tag;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public int getStatus() {
        return status;
    }

}
