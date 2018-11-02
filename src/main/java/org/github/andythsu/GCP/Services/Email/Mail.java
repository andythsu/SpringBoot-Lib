package org.github.andythsu.GCP.Services.Email;

import com.google.cloud.datastore.Entity;
import org.github.andythsu.GCP.Services.DatastoreService;
import org.github.andythsu.GCP.Services.Error.MessageKey;
import org.github.andythsu.GCP.Services.Error.WebRequestException;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author: Andy Su
 * @Date: 11/1/2018
 */
public class Mail {

    private static class UserCredential{
        private final String userName;
        private final String password;
        public UserCredential(String userName, String password){
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

    public static void sendEmail(MailContent mailContent){
        UserCredential credential = initUser();
        if (credential == null) throw new WebRequestException(MessageKey.UNAUTHORIZED);
        String userName = credential.getUserName();
        String password = credential.getPassword();
        String recipient = "f2280c@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(mailContent.getSubject());
            message.setText(mailContent.getBody());

            Transport.send(message);

        } catch (MessagingException e) {
            throw new WebRequestException(new MessageKey(
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    MessageKey.MessageKeyTags.RUN_TIME_ERROR,
                    e.getMessage()
            ));
        }
    }

    private static UserCredential initUser(){
        Iterator<Entity> data = DatastoreService.getLastUpdatedByKind("credential");
        String user_col = "Username";
        String pwd_col = "Password";
        UserCredential credential = null;
        while (data.hasNext()){
            Entity en = data.next();
            credential = new UserCredential(en.getString(user_col), en.getString(pwd_col));
        }
        return credential;
    }

}
