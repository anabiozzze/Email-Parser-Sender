package controller;

import model.UrlParser;
import model.emails.classes4hibernate.Email;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {
    // проводим основные операции - парсинг ссылок, формирование и отправка писем, вывод списка емэйлов
    // контроллер работает только с MainView и UrlParser

    private static final Logger logger = LoggerFactory.getLogger(Controller.class.getName());
    private UrlParser parser = new UrlParser();

    public Controller() {
    }

    public void startParse(String url) {
        parser.startParse(url);
    }

    public List<String> getAllEmails(){

        logger.debug("Method getAllEmails() started;");

        List<Email> emails = parser.getEmails();
        List<String> result = new ArrayList<String>();

        for (Email email : emails) {
            result.add(email.getAddress());
        }

        logger.debug("Method getAllEmails() finished with result: " + result);
        return result;
    }

    public void sendMail(String letter, String subject, String pass){

        parser.sendMail(letter, subject, pass);
    }

}
