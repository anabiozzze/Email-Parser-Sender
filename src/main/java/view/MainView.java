package view;

import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainView extends Application {
    /* Класс отвечвет за все элементы интерфейса программы и их расположение
     *  Класс не обращается напрямую к БД или другим классам, делая это через конроллер */

    private static final Logger logger = LoggerFactory.getLogger(MainView.class.getName());


    private Controller controller = new Controller();
    private Confirmer confirmer;

    private Scene scene; // наше окно, на котором будут располагаться все прочие элементы
    private Group group; // основной контейнер с координатами 0.0 по размеру окна
    protected volatile static BorderPane pane; // второй контейнер - в нем будут лежать все графические элементы

    private TextField fieldUrls; // текстовое поле для ввода ссылок
    private HTMLEditor fieldLetter; // текстовое поле для ввода текста письма
    private TextField fieldSubject; // текстовое поле для темы письма
    private VBox vBox;
    private HBox hBox;
    private static HBox hb;

    protected volatile static boolean isStarted = false;


    // нам потребуются:
    // малое текстовое поле для ввода ссылок
    // большое текстовое поле для ввода текста письма
    // кнопка "найти почтовые адреса"
    // кнопка "отправить письма"
    // анимация загрузки

    @Override
    public void start(Stage primaryStage) throws Exception {

        logger.debug("Method 'start' started;");

        // задаем размер и цвет окна и основной области, на которой будем располагать элементы
        group = new Group();
        scene = new Scene(group, 620, 400);
        pane = new BorderPane();
        pane.setMinSize(620, 400);
        pane.setStyle("-fx-background-color: lightgrey");

        // привяжем размеры основной области к размерам окна программы, чтобы все масштабировалось вместе
        pane.prefHeightProperty().bind(scene.heightProperty());
        pane.prefWidthProperty().bind(scene.widthProperty());

        // пока отключим масшитабирование окна программы
        primaryStage.setResizable(false);

        // присваиваем окну заголовок и запускаем
        primaryStage.setTitle("Auto Mailer Supreme Turbo");
        primaryStage.setScene(scene);

        addTextFields();
        addButtons();
        placeContainers();

        group.getChildren().add(pane);

        primaryStage.show();

        /*
        этот процесс всегда следит за состоянием гифки, которая открывается во время ожидания
        выполнения основных операций. Его задача - закрывать гифку, когда ожидание окончено.
        таймера в 2 секунды достаточно для любой операции программы.
        */

        Runnable task = () -> {

            while (true) {
                if (Confirmer.isDone) {
                    System.out.println("confirm");

                    Platform.runLater(() ->
                            closeGif());
                    Confirmer.isDone = false;
                }

                 if (isStarted){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(() ->
                            closeGif());
                }
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        logger.debug("Method 'start' finished: GUI is ready;");
    }

    private void addTextFields() {
        logger.debug("Method 'addTextFields' started;");

        fieldUrls = new TextField();
        fieldUrls.setMinSize(600, 50);
        fieldUrls.setStyle("-fx-background-color: snow");

        fieldUrls.setPromptText("Введите ссылки для поиска почтовых адресов:");

        fieldSubject = new TextField();
        fieldSubject.setMinSize(600, 30);
        fieldSubject.setStyle("-fx-background-color: snow");

        fieldSubject.setPromptText("Введите тему письма:");

        fieldLetter = new HTMLEditor();
        fieldLetter.setMinSize(600, 200);
        fieldLetter.setMaxSize(600, 200);
        fieldLetter.setStyle("-fx-background-color: lightblue");

        vBox = new VBox();
        vBox.getChildren().addAll(fieldUrls, fieldSubject, fieldLetter);

        logger.debug("Method 'addTextFields' finished: TextFields is ready;");
    }

    private void addButtons() {
        logger.debug("Method 'addButtons' started;");

        Button btn_find = new Button("найти почтовые адреса");
        Button btn_look = new Button("посмотреть найденные адреса");
        Button btn_send = new Button("отправить письма");
        btn_find.setMinSize(187, 40);
        btn_look.setMinSize(187, 40);
        btn_send.setMinSize(186, 40);

        btn_find.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // отправляет введенную пользователем ссылку в обработку, показывает гифку ожидания

                logger.debug("btn_find is clicked;");

                Platform.runLater(() -> {
                    showGif();
                    isStarted = true;
                    });

                Platform.runLater(() -> {
                        String line = fieldUrls.getText();
                        List<String> urls = Arrays.asList(line.split("\n"));

                        for (String str : urls) {
                            controller.startParse(str);
                            System.out.println(str);
                        }
                    });
            }

        });

        btn_look.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // весь список найденных адресов нужно вывести в окне
                logger.debug("btn_look is clicked;");

                Informer informer = new Informer();
                informer.showWindow();

            }
        });

        btn_send.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // сперва должно быть окно подтверждения

                Platform.runLater(() -> {
                    showGif();
                });

                logger.debug("btn_send is clicked;");

                confirmer = new Confirmer(getText(), fieldSubject.getText());
                confirmer.showWindow();
            }
        });

        hBox = new HBox();
        hBox.getChildren().addAll(btn_find, btn_look, btn_send);

        logger.debug("Method 'addButtons' finished: buttons is ready;");
    }


    private void placeContainers() {
        logger.debug("Method 'placeContainers' started;");

        vBox.setSpacing(10);
        BorderPane.setMargin(vBox, new Insets(10, 10,10, 10));
        pane.setTop(vBox);

        hBox.setSpacing(20);
        BorderPane.setMargin(hBox, new Insets(0, 10,10, 10));
        pane.setBottom(hBox);

        logger.debug("Method 'placeContainers' finished: all containers on the pane;");
    }

    protected void showGif() {
        logger.debug("Method 'showGif' started;");

        System.out.println("opening GIF...");

        File file = new File("/Users/andreimironov/Desktop/cat-preloader.gif");

        String localUrl = null;
        try {
            localUrl = file.toURI().toURL().toString();

        } catch (Exception e) {
            logger.error("Error creating url: " + e);
        }


        Image image = new Image(localUrl, 200,200, false, true, true);
        ImageView imageView = new ImageView(image);

        hb = new HBox();

        hb.setStyle("-fx-background-color: lightgrey");
        hb.setOpacity(0.7);
        hb.getChildren().add(imageView);
        HBox.setMargin(imageView, new Insets(300, 100, 60, 200));
        BorderPane.setMargin(hb, new Insets(0, 0,600, 0));

        System.out.println("setting the pane");
        pane.setCenter(hb);
        System.out.println("GIF started");

        logger.debug("GIF started;");
    }

    protected static void closeGif() {

        Platform.runLater(() -> {

        System.out.println("close gif");

        pane.getChildren().remove(hb);

        isStarted = false;
        System.out.println("gif closed");
        logger.debug("Method 'showGif' finished: GIF closed.");

        });
    }


    // этот метод забирает из HTMLEditor'a введенный текст
    private String getText() {
        logger.debug("Method 'getText' get started;");

        String htmlText = fieldLetter.getHtmlText();

        String result = "";

        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(htmlText);
        final StringBuffer text = new StringBuffer(htmlText.length());

        while (matcher.find()) {
            matcher.appendReplacement(
                    text,
                    " ");
        }

        matcher.appendTail(text);

        result = text.toString().trim();

        logger.debug("Method 'getText' finished;");

        return result;
    }

}



// https://parsertest.ru.gg/