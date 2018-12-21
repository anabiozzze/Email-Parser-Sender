import model.UrlParser;

public class Main {
    public static String mainUrl = "https://www.bspb.ru/career/jobs/spb/contactnji-center/";

    /*
     * view - GUI программы, анимация ожидания
     * model - парсинг страниц, проверка мейлов, сбор мейлов в БД, формирование писем, логгер
     * controller - взаимодействие GUI и всего прочего
     * */

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        UrlParser.startParse(mainUrl);
        UrlParser.showEmails();


        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("\nВремя выполнения программы (мс): " + timeConsumedMillis);

    }

}
