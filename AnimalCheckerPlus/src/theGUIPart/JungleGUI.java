package theGUIPart;

import controller.GameController;
import entity.Board;
import exception.CannotRedoException;
import exception.CannotUndoException;
import exception.GameWinException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.FileNotFoundException;

import static javafx.geometry.Pos.CENTER;

/**
 * Created by 谢东方xdf on 2016/12/28.
 */
public class JungleGUI extends Application {
    private static char[][] left = new char[7][9];
    private static char[][] right = new char[7][9];
    private GridPane animalPane = animalsPane();
    private StackPane middlePane;
    private Scene middleScene;
    private Scene finalScene;
    private boolean someoneIsMoving = false;
    private int[] from;
    private int[] to;
    private Stage myStage = new Stage();
    private MediaPlayer bgMediaPlayer;
    private MediaPlayer startMediaPlayer;
    private Label hintLabel;
    private int middleOrFinal;


    @Override
    public void start(Stage primaryStage ) throws FileNotFoundException , GameWinException{
        hintLabel = new Label("");
        startMediaPlayer = new MediaPlayer(new javafx.scene.media.Media(getClass().getResource("audio/start.mp3").toString()));
        startMediaPlayer.setCycleCount(1);
        startMediaPlayer.play();
        bgMediaPlayer = new MediaPlayer(new javafx.scene.media.Media(getClass().getResource("audio/bgm.mp3").toString()));
        bgMediaPlayer.setCycleCount(10000);
        bgMediaPlayer.play();
        myStage.setMaxHeight(700);
        myStage.setMinHeight(700);
        myStage.setMaxWidth(1000);
        myStage.setMinWidth(1000);
        myStage.setResizable(false);
        StackPane frontPane = Front.getInstance();
        middlePane = new StackPane();
        GridPane theButtons = new GridPane();
        Button[] buttonArray = new Button[4];
        Button startButton = new Button("开始新游戏");
        buttonArray[0] = startButton;
        Button continueButton = new Button("继续游戏");
        buttonArray[1] = continueButton;
        Button helpButton = new Button("帮助与设置");
        buttonArray[2] = helpButton;
        Button exitButton = new Button("退出游戏");
        buttonArray[3] = exitButton;

        for (int i = 0 ; i < 4 ; i++) {
            theButtons.add(buttonArray[i] , 0 , i);
            buttonArray[i].setAlignment(CENTER);
            buttonArray[i].setMaxSize(300 , 50);
            buttonArray[i].setMinSize(300 , 50);
            buttonArray[i].setStyle("-fx-text-fill:black;-fx-font-size:25;-fx-font-family:STXingkai;");
            buttonArray[i].setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.2) , new CornerRadii(25) , Insets.EMPTY)));
            buttonArray[i].setOnMouseEntered(new mouseEnter(buttonArray[i]));
            buttonArray[i].setOnMouseExited(new mouseExit(buttonArray[i]));
        }

        /**帮助按钮*/
        theButtons.setVgap(20);
        ImageView middle = new ImageView("file:src/theGUIPart/pic/middle.png");
        middlePane.getChildren().add(middle);
        middlePane.getChildren().add(theButtons);
        theButtons.setAlignment(CENTER);

        update();
        StackPane finalPane = new StackPane();
        ImageView tileImage = new ImageView("file:src/theGUIPart/pic/Map3.png");
        finalPane.getChildren().add(tileImage);
        BorderPane animalPaneWithMargin = animalPaneWithMargin();
        finalPane.getChildren().add(animalPaneWithMargin);
        System.out.println("the size is " + finalPane.getWidth()+" "+finalPane.getHeight());
        buttonArray[0].setOnMouseClicked(event -> {
            GameController.restart();
            updateFinalPane();
        });

        buttonArray[1].setOnMouseClicked(event -> updateFinalPane());
        buttonArray[2].setOnMouseClicked(event -> {
            Scene helpScene = new Scene(helpPane(0));
            myStage.setScene(helpScene);
        });
        buttonArray[3].setOnAction(event -> System.exit(0));


        Scene frontScene = new Scene(frontPane);
        frontScene.setOnMouseClicked(e -> {
            middleScene = new Scene(middlePane);
            myStage.setScene(middleScene);
        });


        myStage.setTitle("斗兽争霸！");
        myStage.setScene(frontScene);
        myStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private GridPane animalsPane() {
        GridPane animalsPane = new GridPane();
        SmallSquare[][] smallSquares = new SmallSquare[7][9];
        for (int i = 0 ; i < 7 ; i++ ) {
            for (int j = 0 ; j < 9 ; j++ ){
                switch (left[i][j]) {
                    case '1':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '1');
                        break;
                    case '2':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '2');
                        break;
                    case '3':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '3');
                        break;
                    case '4':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '4');
                        break;
                    case '5':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '5');
                        break;
                    case '6':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '6');
                        break;
                    case '7':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '7');
                        break;
                    case '8':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.LEFT , '8');
                        break;
                    default:
                        break;
                }
            }
        }

        for (int i = 0 ; i < 7 ; i++ ) {
            for (int j = 0 ; j < 9 ; j++ ){
                switch (right[i][j]) {
                    case '1':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '1');
                        break;
                    case '2':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '2');
                        break;
                    case '3':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '3');
                        break;
                    case '4':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '4');
                        break;
                    case '5':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '5');
                        break;
                    case '6':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '6');
                        break;
                    case '7':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '7');
                        break;
                    case '8':
                        smallSquares[i][j] = new SmallSquare(GameController.Side.RIGHT , '8');
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0 ; i < 7 ; i++ ) {
            for (int j = 0 ; j < 9 ; j++ ) {
                if (smallSquares[i][j] == null) {
                    smallSquares[i][j] = new SmallSquare();
                }
                smallSquares[i][j].getSmallPane().setOnMouseClicked(new FromOrToHandlerClass(i , j ));
                animalsPane.add(smallSquares[i][j].getSmallPane() , j , i);
            }
        }
        return animalsPane;
    }

    private void update() throws FileNotFoundException ,GameWinException {
            GameController.update(from, to);
            left = GameController.getLeft();
            right = GameController.getRight();
            from = null;
            to = null;
    }

    private BorderPane animalPaneWithMargin() {
        BorderPane animalPaneWithMargin = new BorderPane();
        ImageView leftMargin = new ImageView("file:src/theGUIPart/pic/Margin/leftMargin.png");
        leftMargin.setOpacity(0.0);
        animalPaneWithMargin.setLeft(leftMargin);

        animalPaneWithMargin.setTop(topOfGame());
        animalPane = animalsPane();
        animalPaneWithMargin.setCenter(animalPane);
        return animalPaneWithMargin;
    }

    private StackPane topOfGame() {
        StackPane topOfGameWithLabel = new StackPane();
        BorderPane topOfGame = new BorderPane();
        Group root = new Group();
        MenuBar menuBar = new MenuBar();
        menuBar.setMaxSize(105, 50);
        menuBar.setMinSize(105 , 50);
        menuBar.getStylesheets().add("menu.css");
        menuBar.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.2) , new CornerRadii(25) , Insets.EMPTY)));
        Menu menu = new Menu("菜单");

        MenuItem undoMenuItem = new MenuItem("悔棋");
        undoMenuItem.setOnAction(e -> {
            try {
                GameController.undo();
            } catch (CannotUndoException e1) {
                hintLabel.setText("不能再悔棋喽！");
                hintLabel.setStyle("-fx-text-fill:black;-fx-font-size:35;-fx-font-family:STXingkai;");
                hintLabel.setGraphic(new ImageView("file:src/theGUIPart/pic/yaowan.jpg"));
            }
            updateFinalPane();
        });
        menu.getItems().add(undoMenuItem);

        MenuItem redoMenuItem = new MenuItem("取消悔棋");
        redoMenuItem.setOnAction(e -> {
            try {
                GameController.redo();
            } catch (CannotRedoException e1 ) {
                hintLabel.setText("不能再取消悔棋喽！");
                hintLabel.setStyle("-fx-text-fill:black;-fx-font-size:35;-fx-font-family:STXingkai;");
                hintLabel.setGraphic(new ImageView("file:src/theGUIPart/pic/yaowan.jpg"));
            }
            updateFinalPane();
        });
        menu.getItems().add(redoMenuItem);

        MenuItem restartMenuItem = new MenuItem("重新开始");
        restartMenuItem.setOnAction(e -> {
            GameController.restart();
            updateFinalPane();
        });
        menu.getItems().add(restartMenuItem);

        MenuItem helpMenuItem = new MenuItem("帮助与设置");
        helpMenuItem.setOnAction(event -> {
            Scene helpScene = new Scene(helpPane(1));
            myStage.setScene(helpScene);
        });
        menu.getItems().add(helpMenuItem);

        MenuItem fileMenuItem = new MenuItem("建立存档");
        menu.getItems().add(fileMenuItem);

        MenuItem returnMenuItem = new MenuItem("返回");
        returnMenuItem.setOnAction(event -> myStage.setScene(middleScene));
        menu.getItems().add(returnMenuItem);

        menuBar.getMenus().add(menu);
        root.getChildren().add(menuBar);
        ImageView playerLeft = new ImageView("file:src/theGUIPart/pic/Player/1.png");
        topOfGame.setLeft(playerLeft);
        ImageView playerRight = new ImageView("file:src/theGUIPart/pic/Player/2.png");
        topOfGame.setRight(playerRight);
        Label turnLabel = new Label("");
        if (GameController.getTurn() == GameController.Side.LEFT) {
            turnLabel.setText("      左方行动！                                  ");
        }
        else {
            turnLabel.setText("                               右方行动！");
        }
        turnLabel.setStyle("-fx-text-fill:rgb(13 , 241 , 147);-fx-font-size:35;-fx-font-family:STXingkai;");
        topOfGame.setCenter(root);
        topOfGameWithLabel.getChildren().add(turnLabel);
        topOfGameWithLabel.getChildren().add(topOfGame);
        turnLabel.setLayoutX(300);
        turnLabel.setLayoutY(300);
        return topOfGameWithLabel;
    }

    private void updateFinalPane() {
        left = GameController.getLeft();
        right = GameController.getRight();
        from = null;
        to = null;
        StackPane finalPane = new StackPane();
        ImageView tileImage = new ImageView("file:src/theGUIPart/pic/Map3.png");
        finalPane.getChildren().add(tileImage);
        BorderPane animalPaneWithMargin = animalPaneWithMargin();
        finalPane.getChildren().add(animalPaneWithMargin);
        finalPane.getChildren().add(hintLabel);
        finalScene = new Scene(finalPane);
        myStage.setScene(finalScene);
        someoneIsMoving = false;
        hintLabel = null;
        hintLabel = new Label("");
    }

    private BorderPane helpPane(int middleOrFinal ) {
        this.middleOrFinal = middleOrFinal;
        Label helpLabel = new Label("斗兽棋的棋盘\n" +
                "斗兽棋的棋盘横七列，纵九行，棋子放在格子中。双方底在线各有三个陷阱（作品字排）和\n" +
                "一个兽穴(于品字中间)。 棋牌中部有两片水域，称之为小河。\n" +
                "\n" +
                "斗兽棋的棋子\n" +
                "斗兽棋棋子共十六个，分为红蓝双方，双方各有八只一样的棋子（下称为：兽 或 动物），\n" +
                "按照战斗力强弱排列为：象>狮>虎>豹>狗>狼>猫>鼠。\n" +
                "\n" +
                "斗兽棋的走法\n" +
                "游戏开始时，红方先走，然后轮流走棋。每次可走动一只兽，每只兽每次走一方格，除己方\n" +
                "兽穴和小河以外，前后左右均可。但是，狮、虎、鼠还有不同走法：\n" +
                "狮虎跳河法：狮虎在小河边时，可以纵横对直跳过小河，且能把小河对岸的敌方较小的兽类\n" +
                "吃掉，但是如果对方老鼠在河里，把跳的路线阻隔就不能跳，若对岸是对方比自己战斗力前\n" +
                "的兽，也不可以跳过小河；\n" +
                "鼠游过河法：鼠是唯一可以走入小河的兽，走法同陆地上一样，每次走一格，上下左右均可，\n" +
                "而且，陆地上的其他兽不可以吃小河中的鼠，小河中的鼠也不能吃陆地上的象，鼠类互吃不\n" +
                "受小河影响。\n" +
                "\n" +
                "斗兽棋的吃法\n" +
                "斗兽棋吃法分普通吃法和特殊此法，普通吃法是按照兽的战斗力强弱，强者可以吃弱者。\n" +
                "特殊吃法如下：\n" +
                "1、鼠吃象法：八兽的吃法除按照战斗力强弱次序外，惟鼠能吃象，象不能吃鼠。\n" +
                "2、互吃法：凡同类相遇，可互相吃。\n" +
                "3、陷阱：棋盘设陷阱，专为限制敌兽的战斗力（自己的兽，不受限制），敌兽走入陷阱，即\n" +
                "失去战斗力，本方的任意兽类都可以吃去陷阱里的兽类。\n" +
                "综合普通吃法和特殊吃法，将斗兽棋此法总结如下：\n" +
                "鼠可以吃象、鼠\n" +
                "猫可以吃猫、鼠；\n" +
                "狼可以吃狼、猫、鼠；\n" +
                "狗可以吃狗、狼、猫、鼠；\n" +
                "豹可以吃豹、狗、狼、猫、鼠；\n" +
                "虎可以吃虎、豹、狗、狼、猫、鼠；\n" +
                "狮可以吃狮、虎、豹、狗、狼、猫、鼠；\n" +
                "象可以吃象、狮、虎、豹、狗、狼、猫；\n" +
                "\n" +
                "斗兽棋胜负判定:\n" +
                "1、任何一方的兽走入敌方的兽穴就算胜利（自己的兽类不可以走入自己的兽穴）；\n" +
                "2、任何一方的兽被吃光就算失败，对方获胜；\n" +
                "3、任何一方所有活着的兽被对方困住，均不可移动时，就算失败，对方获胜；\n" +
                "4、任何一方走棋时间用完，就算失败，对方获胜；\n" +
                "5、任何一方中途离开游戏，就算逃跑，对方获胜；\n" +
                "6、在双方同意的情况下可和棋；\n" +
                "7、在连续100回合内，双方均无动物被吃，就算和棋。\n");
        BorderPane helpAndSet = new BorderPane();
        GridPane helpButtons = new GridPane();
        Button[] helpButtonArray = new Button[3];
        Button returnButton = new Button("返回");
        helpButtonArray[0] = returnButton;
        Button helpButton = new Button("帮助");
        helpButtonArray[1] = helpButton;
        Button setButton = new Button("设置");
        helpButtonArray[2] = setButton;

        for (int i = 0 ; i < 3 ; i++) {
            helpButtons.add(helpButtonArray[i] , 0 , i);
            helpButtonArray[i].setAlignment(CENTER);
            helpButtonArray[i].setMaxSize(300 , 50);
            helpButtonArray[i].setMinSize(300 , 50);
            helpButtonArray[i].setStyle("-fx-text-fill:black;-fx-font-size:25;-fx-font-family:STXingkai;");
            helpButtonArray[i].setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.2) , new CornerRadii(25) , Insets.EMPTY)));
            helpButtonArray[i].setOnMouseEntered(new mouseEnter(helpButtonArray[i]));
            helpButtonArray[i].setOnMouseExited(new mouseExit(helpButtonArray[i]));
        }
        helpButtonArray[0].setOnAction(event -> {
            if (this.middleOrFinal == 0 ) {
                myStage.setScene(middleScene);
            }
            else {
                updateFinalPane();
            }
        });

        /**帮助按钮*/
        helpButtons.setVgap(20);
        helpAndSet.setLeft(helpButtons);
        helpAndSet.setCenter(helpLabel);
        helpAndSet.setBackground(new Background(new BackgroundFill(Color.rgb(27,242,10,0.2) , new CornerRadii(0.0) , Insets.EMPTY)));
        helpAndSet.setMaxSize(1000 , 700);
        helpAndSet.setMinSize(1000 ,700);

        return helpAndSet;
    }

    class FromOrToHandlerClass implements EventHandler<MouseEvent> {
        private int i;
        private int j;
        public FromOrToHandlerClass(int i , int j ) {
            this.i = i ;
            this.j = j ;
        }
        @Override
        public void handle(MouseEvent e) {
            if (someoneIsMoving) {
                to = new int[2];
                to[0] = this.i;
                to[1] = this.j;
                System.out.println(to[0] + "  " + to[1]);
                try {
                    update();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (GameWinException e1) {
                    left = GameController.getLeft();
                    right = GameController.getRight();
                    from = null;
                    to = null;
                    if (e1.getSide() == GameController.Side.LEFT) {
                        hintLabel.setText("左方玩家胜利:" + e1.getMessage());
                    } else {
                        hintLabel.setText("右方玩家胜利:" + e1.getMessage());
                    }
                    hintLabel.setStyle("-fx-text-fill:black;-fx-font-size:35;-fx-font-family:STXingkai;");
                    hintLabel.setGraphic(new ImageView("file:src/theGUIPart/pic/yeah.gif"));
                    GameController.setTurn(GameController.Side.LEFT);
                    GameController.restart();
                }
                finally {
                    StackPane finalPane = new StackPane();
                    ImageView tileImage = new ImageView("file:src/theGUIPart/pic/Map3.png");
                    finalPane.getChildren().add(tileImage);
                    BorderPane animalPaneWithMargin = animalPaneWithMargin();
                    finalPane.getChildren().add(animalPaneWithMargin);
                    finalPane.getChildren().add(hintLabel);
                    finalScene = new Scene(finalPane);
                    System.out.println("the new size is " + finalScene.getHeight() + "  " + finalScene.getWidth());
                    myStage.setScene(finalScene);
                    someoneIsMoving = false;
                    hintLabel = null;
                    hintLabel = new Label("");
                }
            }
            else {
                from = new int[2];
                from[0] = this.i;
                from[1] = this.j;
                System.out.println(from[0] + "  " + from[1]);
                if (Board.getInstance().getTiles()[from[0]][from[1]].getAnimal() != null) {
                    if (Board.getInstance().getTiles()[from[0]][from[1]].getAnimal().getSide() == GameController.getTurn()) {
                        someoneIsMoving = true;
                    }
                    else {
                        from = null;
                    }
                }
                else {
                    from = null;
                }
            }
        }
    }

    class   mouseEnter implements EventHandler<MouseEvent> {
        private Button button;
        public mouseEnter(Button button ) {
            this.button = button;
        }
        @Override
        public void handle(MouseEvent event) {
            button.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0,0.2) ,  new CornerRadii(25) , Insets.EMPTY)));
        }
    }

    class mouseExit implements EventHandler<MouseEvent> {
        private Button button;
        public mouseExit(Button button) {
            this.button = button;
        }
        @Override
        public void handle(MouseEvent event) {
            button.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.2) ,  new CornerRadii(25) , Insets.EMPTY)));

        }
    }

}