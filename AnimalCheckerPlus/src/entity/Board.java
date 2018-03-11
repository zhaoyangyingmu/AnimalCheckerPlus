package entity;

import controller.history.GameHistory;
import entity.animal.Animal;
import controller.GameController;
import entity.tile.Tile;
import exception.FileWrongFormatException;
import exception.GameWinException;
import exception.InvalidActionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by 谢东方xdf on 2017/1/3.
 */
public class Board {

    private static Board board;

    private final Tile[][] tiles = new Tile[7][9];
    private final Animal[] animalLeft = new Animal[8];
    private final Animal[] animalRight = new Animal[8];

    public Tile[][] getTiles() {
        return tiles;
    }

    private Board(File tileFile, File animalFile) {
        try {
            loadTiles(tileFile);
            loadAnimals(animalFile);
        } catch (FileWrongFormatException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static Board getInstance() {
        if (board == null) {
            board = new Board(new File("tile.txt"), new File("animal.txt"));
        }
        return board;
    }

    public void restart() {
        board = null;
        GameHistory.restart();

    }

    public void animalMove(Tile from, Tile to) {
        GameHistory.getInstance().addRecord(from, to);
        to.setAnimal(from.getAnimal());
        from.setAnimal(null);
    }

    public Animal getAnimal(GameController.Side turn, int power) {
        switch (turn) {
            case LEFT:
                return animalLeft[power - 1];
            case RIGHT:
                return animalRight[power - 1];
            default:
                return null;
        }
    }

    public Tile getTileByDirection(Tile tile, GameController.Direction direction) {
        try {
            switch (direction) {
                case UP:
                    return tiles[tile.getX() - 1][tile.getY()];
                case DOWN:
                    return tiles[tile.getX() + 1][tile.getY()];
                case LEFT:
                    return tiles[tile.getX()][tile.getY() - 1];
                case RIGHT:
                    return tiles[tile.getX()][tile.getY() + 1];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public Tile getTileByAnimal(Animal animal) throws InvalidActionException {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (animal.equals(tile.getAnimal())) {
                    return tile;
                }
            }
        }
        throw new InvalidActionException(animal.toString() + "已被消灭");
    }

    public boolean animalCannotMove(GameController.Side side) {
        Animal[] animals;
        if (side == GameController.Side.LEFT) {
            animals = animalRight;
        } else {
            animals = animalLeft;
        }
        for (Animal animal : animals) {
            for (GameController.Direction direction : GameController.Direction.values()) {
                try {
                    animal.act(direction, true);
//                    System.out.println(animal.toString() + "还能动。");
                    return false;
                } catch (Exception ignored) {

                }
            }
        }
        return true;
    }

    public boolean animalAllDead(GameController.Side attackSide){
        Animal[] animals;
        if (attackSide == GameController.Side.RIGHT) {
            animals = animalLeft;
        } else {
            animals = animalRight;
        }
        for (Animal animal : animals) {
            try {
                if (getTileByAnimal(animal) != null) {
//                    System.out.println(animal.toString() + "还活着。");
                    return false;
                }
            } catch (InvalidActionException ignored) {
                continue;
            }
        }
        return true;
    }

    public void printBoard() {
        System.out.println();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.getAnimal() != null) {
                    System.out.print(tile.getAnimal());
                } else {
                    System.out.print(tile);
                }
            }
            System.out.println();
        }
    }

    private void loadTiles(File file) throws FileNotFoundException, FileWrongFormatException {
        Scanner scanner = new Scanner(file);
        for (int i = 0; i < tiles.length; i++) {
            String line = scanner.nextLine();
            if (line.length() == tiles[0].length) {
                for (int j = 0; j < tiles[0].length; j++) {
                    int number = line.charAt(j) - '0';
                    if (number >= 0 && number < Tile.TileType.SIZE) {
                        tiles[i][j] = new Tile(i, j, Tile.TileType.values()[number]);
                    } else {
                        throw new FileWrongFormatException(file.getName() + ": Number " + number + " is not valid");
                    }
                }
            } else {
                throw new FileWrongFormatException(file.getName() + ": The width of the board is wrong");
            }
        }
    }

    private void loadAnimals(File file) throws FileNotFoundException, FileWrongFormatException {
        Scanner scanner = new Scanner(file);
        for (int i = 0; i < tiles.length; i++) {
            String line = scanner.nextLine();
            if (line.length() == tiles[0].length) {
                for (int j = 0; j < tiles[0].length; j++) {
                    int power = line.charAt(j) - '0';
                    Animal animal = Animal.newAnimal(power, j);
                    tiles[i][j].setAnimal(animal);
                    if (power != 0) {
                        int index = power - 1;
                        if (j < 3) {
                            animalLeft[index] = animal;
                        }
                        if (j > 5) {
                            animalRight[index] = animal;
                        }
                    }
                }
            } else {
                throw new FileWrongFormatException("The format of " + file.getName() + " is wrong.");
            }
        }

    }
}
