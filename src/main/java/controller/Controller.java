package controller;

import model.UrlParser;
import model.emails.classes4hibernate.Email;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    // проводим основные операции - парсинг ссылок, формирование и отправка писем, вывод списка емэйлов
    // контроллер работает только с MainView и UrlParser

    private UrlParser parser = new UrlParser();

    public Controller() {
    }

    public void startParse(String url) {
        parser.startParse(url);
    }

    public List<String> getAllEmails(){
        List<Email> emails = parser.getEmails();
        List<String> result = new ArrayList<String>();

        for (Email email : emails) {
            result.add(email.getAddress());
        }

        return result;
    }

    public void sendMail(){
        parser.sendMail();
    }

}
