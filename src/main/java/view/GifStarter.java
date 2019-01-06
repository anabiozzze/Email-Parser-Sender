package view;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.MalformedURLException;

public class GifStarter extends Task<Void> {

    private static HBox hb;

    @Override
    protected Void call() throws Exception {
        Platform.runLater(new Runnable() {
            public void run() {
//                showGif();
            }
        });
        return null;
    }

//    private static void showGif() {
//        System.out.println("запускаю гифку");
//
//        File file = new File("/Users/andreimironov/Desktop/cat-preloader.gif");
//
//        String localUrl = null;
//        try {
//            localUrl = file.toURI().toURL().toString();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        Image image = new Image(localUrl, 200,200, false, true);
//        ImageView imageView = new ImageView(image);
//
//        hb = new HBox();
//
//        hb.setStyle("-fx-background-color: lightgrey");
//        hb.setOpacity(0.7);
//        hb.getChildren().add(imageView);
//        HBox.setMargin(imageView, new Insets(300, 100, 60, 200));
//        BorderPane.setMargin(hb, new Insets(0, 0,600, 0));
//        MainView.pane.setCenter(hb); // здесь вылетает Not on FX application thread
//
//        System.out.println("гифка запущена");
//
//    }

    private void closeGif() {
        MainView.pane.getChildren().remove(hb);
    }

}
