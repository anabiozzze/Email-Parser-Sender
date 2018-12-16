package model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParser {

    // этот класс отвечает за парсинг страниц, он же проверяет собранные мейлы через класс-помощник

    List<String> urls = new ArrayList();

    public UrlParser(String ... urls) {
    }

    public UrlParser() {
    }

    public List getUrls() {
        return urls;
    }

    public void setUrls(List urls) {
        this.urls = urls;
    }


    public static List<String> getEmails(String str) throws IOException {

        Document doc = Jsoup.connect(str).get();

        List<String> emails = new ArrayList<String>();
        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(doc.toString());

        while (m.find()) {

            String mail = m.group();
//            System.out.println(mail);

            if (!emails.contains(mail)) {
                emails.add(mail);
            }
        }

//        System.out.println("---------------------");

        for (String string : emails) {
            System.out.println(string);
        }

        return emails;
    }
}


// https://www.wildberries.ru/
//  http://www.vodokanal.spb.ru/o_kompanii/kontakty/
//  https://www.bspb.ru/career/jobs/spb/contactnji-center/
//  http://www.nbcompany.ru/

