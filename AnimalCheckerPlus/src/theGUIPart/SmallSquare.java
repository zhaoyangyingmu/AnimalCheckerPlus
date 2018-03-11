package theGUIPart;

import controller.GameController;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import javax.print.attribute.standard.Media;

/**
 * Created by 谢东方xdf on 2017/1/3.
 */
public class SmallSquare {
    private MediaPlayer mediaPlayer;
    private StackPane smallPane;
    private ImageView imageView;
    private String leftString = "file:src/theGUIPart/pic/animals/left/";
    private String rightString = "file:src/theGUIPart/pic/animals/right/";
    private String endString = ".png";
    private int width = 80;
    private int height = 83;

    public SmallSquare(GameController.Side side , char power ) {
        if (side == GameController.Side.LEFT) {
            imageView = new ImageView(leftString + power + endString);

        }
        else {
            imageView = new ImageView(rightString + power + endString);
        }
        mediaPlayer = new MediaPlayer(new javafx.scene.media.Media(getClass().getResource("audio/" + power + ".mp3").toString()));
        smallPane = new StackPane();
        smallPane.setBackground(new Background(new BackgroundFill(Color.rgb(0 , 0 , 0 , 0.0) , new CornerRadii(0.0) , Insets.EMPTY)));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        smallPane.getChildren().add(imageView);
        imageView.setOnMouseClicked(e -> {
            mediaPlayer.play();
        });
    }

    public SmallSquare() {
        imageView = new ImageView(leftString + '0' + endString);
        imageView.setOpacity(0.0);
        mediaPlayer = null;
        smallPane = new StackPane();
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        smallPane.getChildren().add(imageView);
    }

    public StackPane getSmallPane() {
        return smallPane;
    }
}
