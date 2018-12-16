import model.UrlParser;

public class Main {
    public static String mainUrl = "https://www.wildberries.ru/";

    /*
     * view - GUI программы, анимация ожидания
     * model - парсинг страниц, проверка мейлов, сбор мейлов в БД, формирование писем, логгер
     * controller - взаимодействие GUI и всего прочего
     * */

    public static void main(String[] args) {
        UrlParser.startParse(mainUrl);
        UrlParser.showEmails();

    }

}
