package view;

import controller.Controller;
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

public class Confirmer extends WindowMaker {
    // этот класс выводит окно с подтверждением действия (начать рассылку)

    private String letter;
    private String topic;
    private String pass;


    public Confirmer(String letter, String topic) {
        this.letter = letter;
        this.topic = topic;
    }

    // открываем новое окно с двумя кнопками "Да\Нет"
    public void showWindow() {
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
                pass = passField.getText();
                controller.sendMail(letter, topic, pass);
                Stage stage = (Stage) btn_no.getScene().getWindow();
                stage.close();
            }
        });

        btn_no.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btn_no.getScene().getWindow();
                stage.close();
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
    }

}
