import model.UrlParser;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {

        String str = "https://www.wildberries.ru/";
        try {
            UrlParser.getEmails(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


// https://www.wildberries.ru/
//  http://www.vodokanal.spb.ru/o_kompanii/kontakty/
//  https://www.bspb.ru/career/jobs/spb/contactnji-center/
//  http://www.nbcompany.ru/


