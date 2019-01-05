package view;


import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.MalformedURLException;

public class GifStarter implements Runnable {

    private HBox hb;
    private MainView view = new MainView();

    public void run() {

    }

    private void showGif() {
        File file = new File("/Users/andreimironov/Desktop/cat-preloader.gif");

        String localUrl = null;
        try {
            localUrl = file.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image image = new Image(localUrl, 200,200, false, true);
        ImageView imageView = new ImageView(image);

        hb = new HBox();

        hb.setStyle("-fx-background-color: lightgrey");
        hb.setOpacity(0.7);
        hb.getChildren().add(imageView);
        HBox.setMargin(imageView, new Insets(300, 100, 60, 200));
        BorderPane.setMargin(hb, new Insets(0, 0,600, 0));
        view.pane.setCenter(hb);

    }

    private void closeGif() {
        view.pane.getChildren().remove(hb);
    }

}
