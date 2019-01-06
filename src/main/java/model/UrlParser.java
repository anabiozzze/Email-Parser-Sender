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

public class UrlParser {
    // этот класс отвечает за парсинг страниц, он ищет ссылки и eмэйлы на странице и всех её "дочерних" страницах

    public static String mainUrl = null;
    public static int delRepeats;

    private static EmailService service = new EmailService();

    // дочерние ссылки, найденные парсером на изначальной странице
    public static List<String> urls = new ArrayList();
    // найденные на странице почтовые адреса
    public static List<Email> emails = new ArrayList();

    public UrlParser(String url) {
        mainUrl = checkUrl(url);
    }

    public UrlParser() {
    }

    public static List<Email> getEmails() {
        return emails;
    }

    public static void startParse(String url) {
        mainUrl = checkUrl(url);
        getUrls();
        findEmails();
        uploadToDB(emails);
    }


    // этот метод любую "основную" ссылку (введенную пользователем) приводит к домашней странице, чтобы обеспечить
    // в дальнейшем корректную работу с подразделами и в любом случае начинать поиск ссылок с домашней страницы
    // например http://www.vodokanal.spb.ru/o_kompanii/kontakty/
    // превращается в http://www.vodokanal.spb.ru
    private static String checkUrl(String str) {

        Pattern pattern = Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            URL url = null;
            try {
                url = new URL(str);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            System.out.println("Основная ссылка корректна. Вывожу дочерние ссылки... \n");
            return url.getProtocol() + "://" + url.getHost();

        } else {
            System.out.println("Введена некорректная ссылка");
        }

        return null;
    }


    // этот метод ищет любые ссылки на web-странице
    private static List<String> getUrls(){

        Element body = null;
        try {
            Document doc = Jsoup.connect(mainUrl).get();
            body = doc.body();
        } catch (IOException e) {
            e.printStackTrace();
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
                Pattern pattern = Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");
                Matcher matcher = pattern.matcher(mainUrl);

                if (matcher.matches()) {
                    urls.add(href);
                }
            }
        }

        // удаляем дубликаты, если они есть
        Set set = new LinkedHashSet(urls);
        urls = new ArrayList<String>(set);

        for (String s : urls) {
            System.out.println(s);
        }

        return urls;
    }


    // здесь собираем все емайлы из основной страницы и её дочерних ссылок и складываем мэйлы в список
    private static List<Email> findEmails() {

        System.out.println("\nСобираю почтовые адреса...\n");

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
                e.printStackTrace();
            }

            // удаляем дубликаты, если они есть
            deleteRepeats();
        }

        System.out.println("Удалено " + delRepeats + " повторов.\n");
        return emails;
    }

    public static void showEmails() {
            for (Email eml : emails) {
            System.out.println(eml.getAddress());
        }
    }

    // метод ищет дубликаты емэйлов в соответсвующем списке и удаляет их
    public static void deleteRepeats() {
        for (int i=0; i<emails.size(); i++) {
            for (int j = i+1; j<emails.size(); j++) {

                if (emails.get(i).getAddress().equals(emails.get(j).getAddress())) {
                    emails.remove(j);
                    delRepeats++;

                }
            }
        }
    }

    public static void uploadToDB(List<Email> emails) {
        System.out.println("\nЗагружаю почтовые адреса в базу данных...\n");

        for (Email eml : emails) {
            service.saveEntry(eml);
        }

        System.out.println("\nЗагрузка завершена.");
    }

    public static void sendMail(String letter, String topic, String pass) {
        System.out.println("\nНачинаю рассылку...");

        for (Email eml : emails) {
            eml.setStatus(EmailStatus.Sending);
            MailCreator creator = new MailCreator(eml);
            creator.makeAndSend(letter, topic, pass);
        }

        System.out.println("\nРассылка завершена.");
    }
}


// https://www.wildberries.ru/
//  http://www.vodokanal.spb.ru/o_kompanii/kontakty/
//  https://www.bspb.ru/career/jobs/spb/contactnji-center/
//  http://www.nbcompany.ru/

