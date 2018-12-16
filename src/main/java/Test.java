import model.UrlParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static String mainUrl = "http://www.vodokanal.spb.ru/o_kompanii/kontakty/";

    public static void main(String[] args) {


        try {
            Document doc = Jsoup.connect(mainUrl).get();
            Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(doc.toString());

            while (m.find()) {

                System.out.println(11212);
                String mail = m.group();

                System.out.println(mail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



// https://www.wildberries.ru/
//  http://www.vodokanal.spb.ru/o_kompanii/kontakty/
//  https://www.bspb.ru/career/jobs/spb/contactnji-center/
//  http://www.nbcompany.ru/


