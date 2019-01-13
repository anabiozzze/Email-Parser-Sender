package model.emails;

import model.emails.classes4hibernate.Email;
import model.emails.classes4hibernate.EmailStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailCreator {
    // этот класс формирует новые письма по заданному образцу

    private static final Logger logger = LoggerFactory.getLogger(MailCreator.class.getName());

    // настройки для отправки писем:
    private Email recipient = null; // получатель
    private String sender = "mail.java@yandex.ru"; // отправитель
    private String host = "smtp.yandex.ru"; // почтовый сервер
    private static Properties properties = System.getProperties(); // шаблон настроек для соединения
    private static Session session; // сессия, которая будет обеспечивать соединение

    // авторизация в почте-отправителе; пароль вводится вручную
    private String username = "mail.java";
    private String password = null;

    // без этой штуки ничего не работает. Она и дальше тоже встречается. Исходя из названия, это
    // фабрика для создания сокетов, защищенных SSL-шифрованием. Зачем? Неясно.
    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

    public MailCreator() {
    }

    public MailCreator(Email recipient, String sender, String host) {

        logger.debug("New MailCreator created with recipient: " + recipient + " sender: " + sender + " host: " + host + "\n");

        this.recipient = recipient;
        this.sender = sender;
        this.host = host;
    }

    public MailCreator(Email recipient) {

        logger.debug("New MailCreator created with recipient: " + recipient + "\n");

        this.recipient = recipient;
    }

    // главный метод класса - создали сессию, написали письмо, отправили письмо
    public void makeAndSend(String letter, String subject, String pass) {

        logger.debug("Method makeAndSend() started with parameters: " +
                "letter: " + letter + " subject: " + subject);

        getSession(pass);
        sendEmail(createMail(letter, subject));

        logger.debug("Method makeAndSend() finished;"  + "\n");
    }

    // для каждого письма создается отдельная сессия, включающая все необходимые настройки
    private Session getSession(String pass) {

        logger.debug("Method getSession() started;");

        properties.setProperty("mail.smtp.host", host); // прописываем наш сервер
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY); // указываем непонятную фабрику
        properties.setProperty("mail.smtp.port", "465"); // меняем порт, по умолчанию был 25й, на нем не взлетело
        properties.setProperty("mail.smtp.socketFactory.port", "465"); // и в фабрике тоже
        properties.put("mail.smtp.auth", "true"); // просим у сервера авторизацию; без нее логин\пароль не примет
        properties.put("mail.debug", "true"); // включаем дебаггер - вывод в консоль всей инфы по подключению

        password = pass;
        session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);}});

        logger.debug("Method getSession() finished;");
        return session;
    }

    // здесь мы создаем само сообщение для отправки
    private Message createMail(String letter, String subject) {
        // формируем шаблон сообщения по стандарту передачи MIME - т.е. несколько объектов
        // в рамках одного сообщения, возможность вложенности одного объекта в другой и тд.

        logger.debug("Method createMail() started with parameters: " +
                "letter: " + letter + " subject: " + subject);

        Message message = null;
        try {
            message = new MimeMessage(session);

            // задаем отправителя и получателя
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getAddress()));

            // указываем тему и тело письма
            message.setSubject(subject);
            ((MimeMessage) message).setText(letter);

        } catch (MessagingException e) {
            logger.error("Error creating message: " + e);
        }

        logger.debug("Method createMail() finished;");
        return message;
    }

    // этот метод принимает готовое сообщение и отправляет его адресату
    // попутно меняем статус емайла в БД - в зависимости от результата отправки
    private boolean sendEmail(Message msg) {

        logger.debug("Method sendEmail() started;");

        try {
            Transport.send(msg);
            recipient.setStatus(EmailStatus.Successful);

            logger.debug("Method sendEmail() finished;");
            return true;

        } catch (MessagingException e) {
            recipient.setStatus(EmailStatus.Error);
            logger.error("Error sending message to address " + recipient + e);
            return false;
        }
    }

    public Email getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getHost() {
        return host;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setRecipient(Email recipient) {
        this.recipient = recipient;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
