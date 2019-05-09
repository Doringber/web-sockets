package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.AdminUserObject;
import com.elector.Objects.Entities.SentEmailObject;
import com.elector.Objects.Entities.SentSmsObject;
import com.elector.Objects.General.EmailObject;
import com.elector.Services.GeneralManager;
import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import static com.elector.Utils.Definitions.EMPTY;
import static com.elector.Utils.Definitions.SUPER_ADMIN_OID;
import static org.springframework.util.StringUtils.hasText;

@Component
public class EmailUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private SmsUtils smsUtils;

    private boolean allowSendingEmails() {
        return ConfigUtils.getConfig(ConfigEnum.send_emails, false);
    }

    public void sendEmailViaSendgrid(EmailObject emailObject) {
        Email from = new Email(emailObject.getFrom());
        String subject = emailObject.getSubject();
        Email to = new Email(emailObject.getRecipient());
        Content content = new Content("text/plain", emailObject.getText());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            if (allowSendingEmails()) {
                Response response = sg.api(request);
                LOGGER.info(String.valueOf(response.getStatusCode()));
                LOGGER.info(String.valueOf(response.getBody()));
                LOGGER.info(String.valueOf(response.getHeaders()));
            }
            generalManager.updateObject(new SentEmailObject(emailObject, true));
        } catch (IOException ex) {
            LOGGER.error("sendEmailViaSendgrid", ex);
        }
    }

    private JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl mailSender = null;
        try {
            mailSender = new JavaMailSenderImpl();
            mailSender.setHost("mail.elector.co.il");
            mailSender.setPort(587);
            mailSender.setUsername("contact@elector.co.il");
            mailSender.setPassword("sapirnati12");
            if (!(hasText(mailSender.getPassword()) && hasText(mailSender.getUsername()))) {
                LOGGER.error(LOGGER.getName() + ", getMailSender, missing username or password");
                throw new Exception();
            }
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.ssl.trust", "*");
            mailSender.setJavaMailProperties(properties);
        } catch (Exception e) {
            LOGGER.error("getMailSender", e);
            return null;
        }
        return mailSender;
    }


    public boolean sendEmail(EmailObject email) {
        boolean success = false;
        JavaMailSender mailSender = getMailSender();
        if (mailSender != null) {
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(email.getFrom());
                String[] recipients = new String[email.getTo().size()];
                helper.setTo(email.getTo().toArray(recipients));
                helper.setReplyTo(email.getReplyTo());
                helper.setSubject(email.getSubject());
                helper.setText(email.getText());
                for (String filePath : email.getFilesPaths()) {
                    FileSystemResource file = new FileSystemResource(filePath);
                    helper.addAttachment(file.getFilename(), file);
                }
            } catch (MessagingException e) {
                throw new MailParseException(e);
            }
            if (allowSendingEmails()) {
                mailSender.send(message);
            }
            generalManager.updateObject(new SentEmailObject(email, true));
            success = true;
        } else {
            LOGGER.error(LOGGER.getName() + ", sendEmail , error initializing email error initializing email properties, email won't be sent to: " + email.getTo());
        }
        return success;
    }

    public void sendEmailViaGmail(EmailObject email) {
        boolean success = false;
        final String username = ConfigUtils.getConfig(ConfigEnum.gmail_user_name, EMPTY);
        final String password = ConfigUtils.getConfig(ConfigEnum.gmail_password, EMPTY);
        if (hasText(username) && hasText(password)) {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });
                if (allowSendingEmails()) {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(email.getFrom()));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(email.getRecipient()));
                    message.setSubject(email.getSubject());
                    message.setText(email.getText());
                    Transport.send(message);
                    success = true;
                }

            } catch (MessagingException e) {
                LOGGER.error(String.format("email failed, recipient: %s, subject: %s, text: %s", email.getRecipient(), email.getSubject(), email.getText()), e);
            } finally {
                try {
                    generalManager.updateObject(new SentEmailObject(email, success));
                } catch (Exception e) {
                    LOGGER.error("sent email was not saved to the database", e);
                }
            }
        } else {
            SentSmsObject sentSmsObject = new SentSmsObject(
                    "0504730464",
                    "0504730464",
                    "פרטי ההתחברות ל-GMAIL אינם מוגדרים במסד הנתונים." ,
                    new AdminUserObject(SUPER_ADMIN_OID),
                    new Date(),
                    false,
                    new AdminUserObject(69), null);
            smsUtils.sendSms(sentSmsObject);
        }
    }
}
