package theGUIPart;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Stack;


/**
 * Created by 谢东方xdf on 2017/1/1.
 */
public class Front {
    public static StackPane getInstance() {
        StackPane pane = new StackPane();
        ImageView front = new ImageView("file:src/theGUIPart/pic/front.png");
        front.setFitWidth(1000);
        front.setFitHeight(700);
        pane.getChildren().add(front);
        Label label = new Label("按下任何键开始！");
        label.setStyle("-fx-text-fill:white;-fx-font-size:20;-fx-font-family:STXingkai;");
        label.setMaxSize(200 , 200);
        label.setMinSize(200 , 200);
        label.setAlignment(Pos.BASELINE_CENTER);
        pane.getChildren().add(label);
        EventHandler<ActionEvent> eventHandler = e -> {
            if ( label.getFont().getSize() == 20) {
                label.setStyle("-fx-text-fill:white;-fx-font-size:25;-fx-font-family:STXingkai;");
            }
            else {
                label.setStyle("-fx-text-fill:white;-fx-font-size:20;-fx-font-family:STXingkai;");
            }
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000) , eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        return pane;
    }
}
