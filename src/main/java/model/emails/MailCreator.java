package model.emails;

import model.emails.classes4hibernate.Email;
import model.emails.classes4hibernate.EmailStatus;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class MailCreator {

    // этот класс формирует новые письма по заданному образцу

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
        this.recipient = recipient;
        this.sender = sender;
        this.host = host;
    }

    public MailCreator(Email recipient) {
        this.recipient = recipient;
    }

    // главный метод класса - создали сессию, написали письмо, отправили письмо
    public void makeAndSend(String letter, String topic, String pass) {
        getSession(pass);
        sendEmail(createMail(letter, topic));
    }

    // для каждого письма создается отдельная сессия, включающая все необходимые настройки
    private Session getSession(String pass) {
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

        return session;
    }

    // здесь мы создаем само сообщение для отправки
    private Message createMail(String letter, String topic) {
        // формируем шаблон сообщения по стандарту передачи MIME - т.е. несколько объектов
        // в рамках одного сообщения, возможность вложенности одного объекта в другой и тд.
        Message message = null;
        try {
            message = new MimeMessage(session);

            // задаем отправителя и получателя
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getAddress()));

            // указываем тему и тело письма
            message.setSubject(topic);
            ((MimeMessage) message).setText(letter);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    // этот метод принимает готовое сообщение и отправляет его адресату
    // попутно меняем статус емайла в БД - в зависимости от результата отправки
    private boolean sendEmail(Message msg) {

        try {
            Transport.send(msg);
            System.out.println("\nСообщение на адрес "+ recipient + " успешно отправлено.");
            recipient.setStatus(EmailStatus.Successful);
            return true;

        } catch (MessagingException e) {
            System.out.println("\nОшибка при отправке сообщения на адрес " + recipient);
            recipient.setStatus(EmailStatus.Error);
            e.printStackTrace();
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
