package model;

import controller.EmailService;
import model.emails.MailCreator;
import model.emails.classes4hibernate.Email;
import model.emails.classes4hibernate.EmailStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlParser {
    // этот класс отвечает за парсинг страниц, он ищет ссылки и eмэйлы на странице и всех её "дочерних" страницах

    private static final Logger logger = LoggerFactory.getLogger(UrlParser.class.getName());

    public static String mainUrl = null;
    public static int delRepeats;

    private static EmailService service = new EmailService();

    // дочерние ссылки, найденные парсером на изначальной странице
    public static List<String> urls = new ArrayList();
    // найденные на странице почтовые адреса
    public static List<Email> emails = new ArrayList();

    public UrlParser(String url) {

        logger.debug("New UrlParser created with url: " + url + ";" + "\n");

        mainUrl = checkUrl(url);
    }

    public UrlParser() {
        logger.debug("New UrlParser created without parameters;" + "\n");

    }

    public static List<Email> getEmails() {
        return emails;
    }

    public static void startParse(String url) {

        logger.debug("Method startParse() started with url: " + url + ";");

        mainUrl = checkUrl(url);
        getUrls();
        findEmails();
        uploadToDB(emails);

        logger.debug("Method startParse() finished;" + "\n");
    }


    // этот метод любую "основную" ссылку (введенную пользователем) приводит к домашней странице, чтобы обеспечить
    // в дальнейшем корректную работу с подразделами и в любом случае начинать поиск ссылок с домашней страницы
    // например http://www.vodokanal.spb.ru/o_kompanii/kontakty/
    // превращается в http://www.vodokanal.spb.ru
    private static String checkUrl(String check_url) {

        logger.debug("Method checkUrl() started with url for checking: " + check_url + ";");

        Pattern pattern = Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");
        Matcher matcher = pattern.matcher(check_url);

        if (matcher.find()) {
            URL url = null;
            try {
                url = new URL(check_url);
            } catch (MalformedURLException e) {
                logger.error("Error creating url: " + e);
            }

            logger.info("The link entered is valid;");
            return url.getProtocol() + "://" + url.getHost();

        } else {
            logger.error("Incorrect link entered: " + check_url);
        }


        logger.debug("Method checkUrl() finished;"  + "\n");
        return null;
    }


    // этот метод ищет любые ссылки на web-странице
    private static List<String> getUrls(){

        logger.debug("Method getUrls() started;");

        Element body = null;
        try {
            Document doc = Jsoup.connect(mainUrl).get();
            body = doc.body();
        } catch (IOException e) {
            logger.error("Error creating doc: " + e);
        }

        Elements elements = body.getElementsByTag("a");

        for(Element url : elements) {
            String href = url.attr("href");

            // если ссылка начинается с "/" - добавляем к ней доменное имя и забираем в список
            if (href.startsWith("/")) {
                href = mainUrl + href;
                urls.add(href);

                // если c любого другого символа - проверяем корректность по регулярке и забираем в список
            } else if (href.startsWith("http")) {
                Pattern pattern = Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)" +
                        "?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");
                Matcher matcher = pattern.matcher(mainUrl);

                if (matcher.matches()) {
                    urls.add(href);
                }
            }
        }

        // удаляем дубликаты, если они есть
        Set set = new LinkedHashSet(urls);
        urls = new ArrayList<String>(set);

        logger.info("Displaying child links: ");

        for (String s : urls) {
            logger.debug(s);
        }

        logger.debug("\n" + "Method getUrls() finished;" + "\n");

        return urls;
    }


    // здесь собираем все емайлы из основной страницы и её дочерних ссылок и складываем мэйлы в список
    private static List<Email> findEmails() {

        logger.debug("Method findEmails() started;");

        // проходимся по всем найденным на странице ссылкам...
        for (String str : urls) {

            try {
                Document doc = Jsoup.connect(str).get();
                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(doc.toString());

                // если нашли емайл - кладем его в список и сразу в БД
                // класс емайла сам поставит ему соответствующй статус и дату создания
                while (m.find()) {

                    String mail = m.group();
                    Email email = new Email(mail, mainUrl);
                    emails.add(email);
                }

            } catch (IOException e) {
                logger.error("Error creating doc: " + e);
            }

            // удаляем дубликаты, если они есть
            deleteRepeats();
        }

        logger.debug("Method findEmails() finished; emails collected." + "\n");
        return emails;
    }

    public static void showEmails() {

        logger.debug("Method showEmails() started;");

        logger.info("Displaying collected emails: ");
            for (Email eml : emails) {
            System.out.println(eml.getAddress());
        }

        logger.debug("Method showEmails() finished;" + "\n");
    }

    // метод ищет дубликаты емэйлов в соответсвующем списке и удаляет их
    public static void deleteRepeats() {

        logger.debug("Method deleteRepeats() started;");

        for (int i=0; i<emails.size(); i++) {
            for (int j = i+1; j<emails.size(); j++) {

                if (emails.get(i).getAddress().equals(emails.get(j).getAddress())) {
                    emails.remove(j);
                    delRepeats++;

                }
            }
        }

        logger.debug("Method deleteRepeats() finished;");
        logger.info("Removed " + delRepeats + " repeats." + "\n");
    }

    public static void uploadToDB(List<Email> emails) {
        logger.debug("\n" + "Method uploadToDB() started;");
        logger.info("Uploading emails to the database...");

        for (Email eml : emails) {
            service.saveEntry(eml);
        }

        logger.info("Uploading is done.");
        logger.debug("Method uploadToDB() finished;"  + "\n");
    }

    public static void sendMail(String letter, String subject, String pass) {

        logger.debug("Method sendMail() started with parameters: " +
                "letter: " + letter + " subject: " + subject + "\n");
        logger.info("\nStart sending...");

        for (Email eml : emails) {
            eml.setStatus(EmailStatus.Sending);
            MailCreator creator = new MailCreator(eml);
            creator.makeAndSend(letter, subject, pass);
        }

        logger.info("\nSending is done.");
        logger.debug("Method sendMail() finished;\n");
    }
}


