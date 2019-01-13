import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;

public class Test extends Application {


    private static final int WIDTH = 230;
    private static final int HEIGHT = 200;

    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();

        // Label to show count down
        Label showCount = new Label();
        showCount.setPrefSize(WIDTH, HEIGHT);
        showCount.setAlignment(Pos.CENTER);
        root.getChildren().add(showCount);

        // Load GIF
        // "http://bestanimations.com/Science/Gears/loadinggears/loading-gears-animation-10.gif"
        InputStream fis = new FileInputStream("/Users/andreimironov/Desktop/cat-preloader.gif");
        ImageView waitGifView = new ImageView(new Image(fis));
        waitGifView.setOpacity(.5);
        root.getChildren().add(waitGifView);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();

        Runnable task = () -> {

            // Emulate a long task
            // Use Platform.runLater()
            for (int i = 1; i <= 5; i++) {
                String count = String.valueOf(i);
                Platform.runLater(() -> showCount.setText(count));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Stop count down and remove the GIF
            Platform.runLater(() -> {
                showCount.setText("Done");
                root.getChildren().remove(waitGifView);
            });
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}






