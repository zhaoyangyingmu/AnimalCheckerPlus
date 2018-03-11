package controller;

import controller.history.GameHistory;
import entity.Board;
import entity.animal.Animal;
import entity.tile.Tile;
import exception.CannotRedoException;
import exception.CannotUndoException;
import exception.GameWinException;
import exception.InvalidActionException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 谢东方xdf on 2017/1/3.
 */
public class GameController {

    private static char[][] left = new char[7][9];
    private static char[][] right = new char[7][9];
    private static Side turn = Side.LEFT;


    public static char[][] getLeft() {
        updateLeftAndRight();
//        for (int i = 0 ; i < 7 ;i++) {
//            for (int j = 0 ; j < 9 ;j++) {
//                System.out.print(left[i][j]);
//            }
//            System.out.println();
//        }
        return left;
    }

    public static char[][] getRight() {
        updateLeftAndRight();
//        for (int i = 0 ; i < 7 ;i++) {
//            for (int j = 0 ; j < 9 ;j++) {
//                System.out.print(right[i][j]);
//            }
//            System.out.println();
//        }
        return right;
    }

    public static Side getTurn() {
        return turn;
    }

    public static void undo() throws CannotUndoException {
            GameHistory.getInstance().undo();
            nextTurn();
            Board.getInstance().printBoard();
    }

    public static void redo() throws CannotRedoException {
            GameHistory.getInstance().redo();
            nextTurn();
            Board.getInstance().printBoard();
    }

    public static void restart() {
        turn = Side.LEFT;
        Board.getInstance().restart();
        Board.getInstance().printBoard();
    }

    public static void setTurn(Side turn) {
        GameController.turn = turn;
    }

    private static String parse(int[] from , int[] to ) {
        String command = "";
        char powerChar;
        char direction;
        //需要找到power；
        if (Board.getInstance().getTiles()[from[0]][from[1]].getAnimal() != null) {
            if (Board.getInstance().getTiles()[from[0]][from[1]].getAnimal().getSide() == turn) {
                int power = Board.getInstance().getTiles()[from[0]][from[1]].getAnimal().getPower();
                System.out.print(power);
                powerChar = (char) (power + 48);
            }
            else {
                powerChar = '0';
            }
        }

        else {
            powerChar = '0';
        }
        if (from[0] == to[0]) {
            if ((powerChar == '6' || powerChar == '7' ) && from[1] - to[1] == 4) {
                direction = 'a';
            }
            else if ((powerChar == '6' || powerChar == '7' ) && from[1] - to[1] == -4) {
                direction = 'd';
            }
            else if (from[1] - to[1] == 1) {
                direction = 'a';
            }
            else if (from[1] - to[1] == -1 ) {
                direction = 'd';
            }
            else {
                direction = '0';
            }
        }
        else if (from[1] == to[1] ) {
            if ((powerChar == '6' || powerChar == '7' ) && from[0] - to[0] == 3) {
                direction = 'w';
            }
            else if ((powerChar == '6' || powerChar == '7' ) && from[0] - to[0] == -3) {
                direction = 's';
            }
            else if (from[0] - to[0] == 1) {
                direction = 'w';
            }
            else if (from[0] - to[0] == -1) {
                direction = 's';
            }
            else {
                direction = '0';
            }
        }
        else {
            direction = '0';
        }
        command = powerChar + "" + direction;
        return command;
    }

    private static void updateLeftAndRight() {
        Tile[][] tiles = Board.getInstance().getTiles();
        for (int i = 0 ; i < 7 ; i++) {
            for (int j = 0 ; j < 9 ; j++ ) {
                if (tiles[i][j].getAnimal() == null) {
                    left[i][j] = '0';
                    right[i][j] = '0';
                }
                else {
                    if (tiles[i][j].getAnimal().getSide() == Side.LEFT) {
                        left[i][j] = (char) (tiles[i][j].getAnimal().getPower() + 48 ) ;
                        right[i][j] = '0';
                    }
                    else if (tiles[i][j].getAnimal().getSide() == Side.RIGHT){
                        right[i][j] = (char) ( tiles[i][j].getAnimal().getPower() + 48 ) ;
                        left[i][j] = '0';
                    }
                    else {
                        left[i][j] = '0';
                        right[i][j] = '0';
                    }
                }
            }
        }
    }

    public static void update(int[] from , int[] to ) throws GameWinException {

        Scanner scanner = new Scanner(System.in);
        //改掉这部分；
        //要保证棋盘的问题；

        Pattern animalMovePattern = Pattern.compile("^([1-8])([wasd])$");

        Board.getInstance().printBoard();

        if (from != null && to != null) {
            //不能在这里一直循环；

            if (turn == Side.LEFT) {
                System.out.print("左方玩家行动: ");
            } else {
                System.out.print("右方玩家行动: ");
            }
            System.out.println(from);
            System.out.println(to);
            String input = parse(from , to);
            Matcher matcher = animalMovePattern.matcher(input);
            if (input.equals("exit")) {
                //这里要改成在GUI里面控制；
                System.exit(0);
            } else if (input.equals("restart")) {
                //GUI
                turn = Side.LEFT;
                Board.getInstance().restart();
                Board.getInstance().printBoard();
            } else if (input.equals("help")) {
                //GUI
            } else if (input.equals("undo")) {
                //要单独updateLeftAndRight;
                try {
                    GameHistory.getInstance().undo();
                    nextTurn();
                    Board.getInstance().printBoard();
                } catch (CannotUndoException e) {
                    System.out.println("已经退回到开局,不能再悔棋了!");
                }
            } else if (input.equals("redo")) {
                try {
                    GameHistory.getInstance().redo();
                    nextTurn();
                    Board.getInstance().printBoard();
                } catch (CannotRedoException e) {
                    System.out.println("已经回到最后的记录,不能再取消悔棋了!");

                }
            } else if (matcher.find()) {

                int power = Integer.parseInt(matcher.group(1));
                Direction direction = Direction.parseDirection(matcher.group(2));

                try {

                    Animal animal = Board.getInstance().getAnimal(turn, power);

                    /**
                     * act是核心的动物移动/攻击方法，第二个参数表示动物是否真的要动，在无子可动的检查中，第二个参数是true
                     * act方法的定义在Animal类中，具体实现在AnimalCanJumpRiver, AnimalCanSwim 和 AnimalWithoutSkill 类中。
                     */
                    animal.act(direction, false);

                    nextTurn();
                    Board.getInstance().printBoard();

                } catch (InvalidActionException e) {
                    System.out.println(e.getMessage());
                }
//                catch (GameWinException e) {
//                    Board.getInstance().printBoard();
//                    if (e.getSide() == Side.LEFT) {
//                        System.out.println("左方玩家胜利:" + e.getMessage());
//                    } else {
//                        System.out.println("右方玩家胜利:" + e.getMessage());
//                    }
//                    System.out.println("输入 \"restart\" 重新开始");
//                    while (!scanner.nextLine().equals("restart")) {
//                        System.out.println("输入 \"restart\" 重新开始");
//                    }
//                    turn = Side.LEFT;
//                    Board.getInstance().restart();
//                    Board.getInstance().printBoard();
//                }
            } else {
                System.out.println("不能识别指令\"" + input + "\", 请重新输入");
            }
        }
    }

    private static void nextTurn() {
        if (turn == Side.LEFT) {
            turn = Side.RIGHT;
        } else {
            turn = Side.LEFT;
        }
    }

    public enum Side {
        LEFT, RIGHT
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public static Direction parseDirection(String direction) {
            switch (direction) {
                case "w":
                    return UP;
                case "s":
                    return DOWN;
                case "a":
                    return LEFT;
                case "d":
                    return RIGHT;
            }
            return null;
        }
    }
}
