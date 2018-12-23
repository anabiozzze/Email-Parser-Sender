package model.emails;

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
    private String recipient = "justme7053@gmail.com"; // получатель
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

    public MailCreator(String recipient, String sender, String host) {
        this.recipient = recipient;
        this.sender = sender;
        this.host = host;
    }

    // главный метод класса - создали сессию, написали письмо, отправили письмо
    public void makeAndSend() {
        getSession();
        sendEmail(createMail());
    }

    // для каждого письма создается отдельная сессия, включающая все необходимые настройки
    private Session getSession() {
        properties.setProperty("mail.smtp.host", host); // прописываем наш сервер
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY); // указываем непонятную фабрику
        properties.setProperty("mail.smtp.port", "465"); // меняем порт, по умолчанию был 25й, на нем не взлетело
        properties.setProperty("mail.smtp.socketFactory.port", "465"); // и в фабрике тоже
        properties.put("mail.smtp.auth", "true"); // просим у сервера авторизацию; без нее логин\пароль не примет
        properties.put("mail.debug", "true"); // включаем дебаггер - вывод в консоль всей инфы по подключению

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите пароль вашей учетной записи:");
            password = reader.readLine();
            session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);}});

            return session;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return session;
    }

    // здесь мы создаем само сообщение для отправки
    private Message createMail() {
        // формируем шаблон сообщения по стандарту передачи MIME - т.е. несколько объектов
        // в рамках одного сообщения, возможность вложенности одного объекта в другой и тд.
        Message message = null;
        try {
            message = new MimeMessage(session);

            // задаем отправителя и получателя
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // указываем тему и тело письма
            message.setSubject("You winned 2 tickets to Fhloston paradise!");
            ((MimeMessage) message).setText("Mr. Korben Dallas! \n" + "Congratulations, " +
                    "you have just become the winner of our lottery! Two tickets to Fhloston paradise " +
                    "are waiting for you!\n");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    // этот метод принимает готовое сообщение и отправляет его адресату
    private boolean sendEmail(Message msg) {

        try {
            Transport.send(msg);
            System.out.println("\nСообщение на адрес "+ recipient + " успешно отправлено.");

            return true;

        } catch (MessagingException e) {
            System.out.println("\nОшибка при отправке сообщения на адрес " + recipient);
            e.printStackTrace();
            return false;
        }
    }

    public String getRecipient() {
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

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
