package entity.tile;

import entity.animal.Animal;
import controller.GameController;

/**
 * Created by 谢东方xdf on 2017/1/3.
 */
public class Tile {

    private final int x;
    private final int y;
    private final TileType type;
    private Animal animal;
    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public boolean isHome() {
        return this.type == TileType.HOME_RIGHT || this.type == TileType.HOME_LEFT;
    }

    public int compareSideWithAnimal(Animal animal) {
        int tileSide;
        switch (this.type) {
            case TRAP_LEFT:
            case HOME_LEFT:
                tileSide = -1;
                break;
            case TRAP_RIGHT:
            case HOME_RIGHT:
                tileSide = 1;
                break;
            default:
                tileSide = 0;
        }
        int animalSide = animal.getSide() == GameController.Side.LEFT ? -1 : 1;
        return animalSide * tileSide;
    }

    @Override
    public String toString() {
        return " " + this.type.getPrintWord() + " ";
    }

    public enum TileType {
        LAND("　"), RIVER("水"),
        TRAP_LEFT("陷"), HOME_LEFT("家"),
        TRAP_RIGHT("陷"), HOME_RIGHT("家");

        public static final int SIZE = TileType.values().length;

        private final String printWord;

        TileType(String printWord) {
            this.printWord = printWord;
        }

        public String getPrintWord() {
            return printWord;
        }
    }
}
