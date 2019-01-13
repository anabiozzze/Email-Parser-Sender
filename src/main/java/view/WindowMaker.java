package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class WindowMaker {
    // класс с общей логикой формирования нового диалогового окна для классов Confirmer и  Informer

    public static Controller controller = new Controller();

        public void showWindow() {
            Stage stage = new Stage();
            Group group = new Group();
            BorderPane pane = new BorderPane();
            Scene scene = new Scene(group, 200, 70);

            stage.setScene(scene);
            stage.setResizable(false);

            pane.prefHeightProperty().bind(scene.heightProperty());
            pane.prefWidthProperty().bind(scene.widthProperty());
            pane.setStyle("-fx-background-color: lightgrey");

            Button btn_yes = new Button("текст1");
            final Button btn_no = new Button("текст2");

            btn_yes.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    // любое действие
                }
            });

            btn_no.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    // любое действие
                }
            });

            HBox hBox = new HBox();
            hBox.getChildren().addAll(btn_yes, btn_no);
            hBox.setSpacing(20);

            Text text = new Text("Основное сообщение окна");
            pane.setCenter(text);
            pane.setBottom(hBox);
            BorderPane.setMargin(hBox, new Insets(0, 0,10,5));
            group.getChildren().add(pane);

            stage.show();
        }

}
