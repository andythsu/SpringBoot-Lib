package org.github.andythsu.GCP.Services.Email;

public class MailContent {
    private String subject = "";
    private String body = "";
    public MailContent subject(String subject){
        this.subject = subject;
        return this;
    }
    public MailContent body(String body){
        this.body = body;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
