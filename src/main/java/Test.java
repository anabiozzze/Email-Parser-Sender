import controller.EmailService;
import controller.dao.DAOImpl;
import model.emails.classes4hibernate.Email;

import java.util.List;

public class Test {
    public static String mainUrl = "http://www.vodokanal.spb.ru/o_kompanii/kontakty/";

    public static void main(String[] args) {

        EmailService service = new EmailService();
        service.delAll();


//        DAOImpl dao = new DAOImpl();
//
//        List<Email> emls = dao.getAll();
//        for (Email eml:emls
//             ) {
//            System.out.println(eml.getAddress()+" :" + eml.getAddress());
//        }


//        List<String> emls = dao.getEmailsByUrl("1111wdwwdawdawd.com");
//        for (String str : emls) {
//            System.out.println(str);
//        }

//
//        dao.getAll();



//        Email email2 = new Email("2222222@adawdad.ru", "2222wdwwdawdawd.com");
//        Email email3 = new Email("3333333@adawdad.ru", "3333wdwwdawdawd.com");
//        Email email4 = new Email("4444444@adawdad.ru", "4444wdwwdawdawd.com");
//        List<Email> emails = new ArrayList<Email>();
//        emails.add(email1);
//        emails.add(email2);
//        emails.add(email3);
//        emails.add(email4);
//
//
//        for (Email eml : emails) {
//            dao.saveEntry(eml);
//        }

    }
}



// https://www.wildberries.ru/
//  http://www.vodokanal.spb.ru/o_kompanii/kontakty/
//  https://www.bspb.ru/career/jobs/spb/contactnji-center/
//  http://www.nbcompany.ru/


