package view;

import controller.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;


public class MainView extends Application {
    /* Класс отвечвет за все элементы интерфейса программы и их расположение
     *  Класс не обращается напрямую к БД или другим классам, делая это через конроллер */

    private Controller controller = new Controller();

    private Scene scene; // наше окно, на котором будут располагаться все прочие элементы
    private Group group; // основной контейнер с координатами 0.0 по размеру окна
    protected BorderPane pane; // второй контейнер - в нем будут лежать все графические элементы

    private TextField fieldUrls; // текстовое поле для ввода ссылок
    private TextField fieldLetter; // текстовое поле для ввода текста письма
    private VBox vBox;
    private HBox hBox;

    // нам потребуются:
    // малое текстовое поле для ввода ссылок
    // большое текстовое поле для ввода текста письма
    // кнопка "найти почтовые адреса"
    // кнопка "отправить письма"
    // анимация загрузки справа


    @Override
    public void start(Stage primaryStage) throws Exception {

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


    }

    private void addTextFields() {
        fieldUrls = new TextField();
        fieldUrls.setMinSize(600, 100);
        fieldUrls.setStyle("-fx-background-color: snow");

        fieldUrls.setPromptText("Введите ссылки для поиска почтовых адресов:");

        fieldLetter = new TextField();
        fieldLetter.setMinSize(600, 200);
        fieldLetter.setStyle("-fx-background-color: lightblue");

        fieldLetter.setPromptText("Введите текс письма:");

        vBox = new VBox();
        vBox.getChildren().addAll(fieldUrls, fieldLetter);

    }

    private void addButtons() {
        Button btn_find = new Button("найти почтовые адреса");
        Button btn_look = new Button("посмотреть найденные адреса");
        Button btn_send = new Button("отправить письма");
        btn_find.setMinSize(187, 40);
        btn_look.setMinSize(187, 40);
        btn_send.setMinSize(186, 40);

        btn_find.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // сюда нужно забрать введенную пользователем ссылку

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String line = fieldUrls.getText();
                List<String> urls = Arrays.asList(line.split("\n"));


                for (String str : urls) {
                    controller.startParse(str);
                    System.out.println(str);
                }

            }
        });

        btn_look.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // весь список найденных адресов нужно вывести в окне
                Informer informer = new Informer();
                informer.showWindow();

            }
        });

        btn_send.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // сперва должно быть окно подтверждения
                Confirmer confirmer = new Confirmer();
                confirmer.showWindow();
            }
        });

        hBox = new HBox();
        hBox.getChildren().addAll(btn_find, btn_look, btn_send);
    }


    private void placeContainers() {
        vBox.setSpacing(10);
        BorderPane.setMargin(vBox, new Insets(10, 10,10, 10));
        pane.setTop(vBox);

        hBox.setSpacing(20);
        BorderPane.setMargin(hBox, new Insets(0, 10,10, 10));
        pane.setBottom(hBox);

    }
}
