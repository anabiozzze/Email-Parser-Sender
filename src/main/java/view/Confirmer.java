package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Confirmer extends WindowMaker {
    // этот класс выводит окно с подтверждением действия (начать рассылку) и передает данные дальше в контроллер

    private static final Logger logger = LoggerFactory.getLogger(Confirmer.class.getName());

    private String letter;
    private String subject;
    private String pass;
    protected static boolean isDone = false;


    public Confirmer(String letter, String subject) {
        logger.debug("New Confirmer created with parameters: " + "letter: " + letter + ", subject: " + subject);

        this.letter = letter;
        this.subject = subject;
    }

    // открываем новое окно с двумя кнопками "Да\Нет"
    public void showWindow() {

        logger.debug("Method showWindow() started;");

        Stage stage = new Stage();
        Group group = new Group();
        BorderPane pane = new BorderPane();
        Scene scene = new Scene(group, 300, 120);

        stage.setScene(scene);
        stage.setResizable(false);

        pane.prefHeightProperty().bind(scene.heightProperty());
        pane.prefWidthProperty().bind(scene.widthProperty());
        pane.setStyle("-fx-background-color: lightgrey");


        Text text = new Text("Введите пароль:");
        TextField passField = new TextField();
        passField.setMinSize(110, 40);
        passField.setMaxSize(285, 40);
        passField.setStyle("-fx-background-color: snow");

        passField.setPromptText("Введите пароль учетной записи:");

        Button btn_yes = new Button("Отправить");
        Button btn_no = new Button("Отменить");

        btn_yes.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                logger.debug("btn_yes is clicked;");

                pass = passField.getText();
                controller.sendMail(letter, subject, pass);
                Stage stage = (Stage) btn_no.getScene().getWindow();
                stage.close();

                isDone = true;
            }
        });

        btn_no.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                logger.debug("btn_no is clicked;");

                Stage stage = (Stage) btn_no.getScene().getWindow();
                stage.close();

                isDone = true;
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(btn_yes, btn_no);
        hBox.setSpacing(20);

        pane.setTop(text);
        pane.setCenter(passField);
        pane.setBottom(hBox);
        BorderPane.setMargin(hBox, new Insets(0, 0,10,5));
        BorderPane.setMargin(text, new Insets(0, 0,10,5));
        BorderPane.setMargin(passField, new Insets(0, 6,10,5));
        group.getChildren().add(pane);

        stage.show();

        logger.debug("Method showWindow() finished;");
    }

}
