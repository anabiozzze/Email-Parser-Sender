package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class Informer extends WindowMaker {

    public Informer() {
    }

    public void showWindow() {
        Stage stage = new Stage();
        Group group = new Group();
        ScrollPane pane = new ScrollPane();
        Scene scene = new Scene(group, 400, 70);

        stage.setScene(scene);
        stage.setResizable(false);

        pane.prefHeightProperty().bind(scene.heightProperty());
        pane.prefWidthProperty().bind(scene.widthProperty());
        pane.setStyle("-fx-background-color: lightgrey");

        List<String> emailList = controller.getAllEmails();
        String allEmails = "";

        for (String str : emailList) {
            allEmails = allEmails.concat(str + "\n");
        }

        Text text = new Text(allEmails);

        pane.setContent(text);
        group.getChildren().add(pane);

        stage.show();

    }
}
