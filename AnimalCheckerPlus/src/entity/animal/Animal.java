package entity.animal;

import entity.tile.Tile;
import entity.animal.instance.*;
import entity.Board;
import controller.GameController;
import exception.GameWinException;
import exception.InvalidActionException;

/**
 * Created by 谢东方xdf on 2017/1/3.
 */
public abstract class Animal {

    protected GameController.Side side;
    private int power;

    public int getPower() {
        return power;
    }

    public static Animal newAnimal(int power, int index) {
        GameController.Side side;
        if (index < 3) {
            side = GameController.Side.LEFT;
        } else {
            side = GameController.Side.RIGHT;
        }
        Animal animal = AnimalCollection.values()[power].getInstance();
        if (animal != null) {
            animal.power = power;
            animal.side = side;
            return animal;
        }
        return null;
    }

    protected abstract String getName();

    public abstract void act(GameController.Direction direction, boolean simulate) throws InvalidActionException, GameWinException;

    public GameController.Side getSide() {
        return side;
    }

    private boolean canBeat(Animal enemyAnimal) throws InvalidActionException {
        if (this instanceof Elephant && enemyAnimal instanceof Mouse) {
            return false;
        } else if (this instanceof Mouse && enemyAnimal instanceof Elephant) {
            return true;
        } else if (enemyAnimal.isInEnemyTrap()) {
            return true;
        } else {
            return this.power >= enemyAnimal.power;
        }
    }

    protected void checkBoundary(Tile nextTile) throws InvalidActionException {
        if (nextTile == null) {
            throw new InvalidActionException("不能走出边界");
        } else if (nextTile.compareSideWithAnimal(this) > 0
                && (nextTile.getType() == Tile.TileType.HOME_LEFT || nextTile.getType() == Tile.TileType.HOME_RIGHT)) {
            throw new InvalidActionException("不能走进自己家");
        }
    }

    protected void checkWin(Tile nextTile) throws GameWinException {
        if (nextTile.isHome() && nextTile.compareSideWithAnimal(this) < 0) {
            throw new GameWinException(this.side, "攻占敌方兽穴!");
        }
        if (Board.getInstance().animalAllDead(this.side)) {
            throw new GameWinException(this.side, "消灭了敌方的所有动物!");
        }
        if (Board.getInstance().animalCannotMove(this.side)) {
            throw new GameWinException(this.side, "敌方所有动物都不能移动!");
        }
    }

    protected void move(Tile currentTile, Tile nextTile, boolean simulate) throws InvalidActionException {
        Animal nextAnimal = nextTile.getAnimal();
        if (nextAnimal == null) {
            if (!simulate) {
                Board.getInstance().animalMove(currentTile, nextTile);
            }
        } else {
            if (this.side == nextAnimal.side) {
                throw new InvalidActionException("不能和友方单位重叠");
            }
            if (this.canBeat(nextAnimal)) {
                if (!simulate) {
                    Board.getInstance().animalMove(currentTile, nextTile);
                }
            } else {
                throw new InvalidActionException(this.getName() + "打不过" + nextAnimal.getName());
            }
        }
    }

    private boolean isInEnemyTrap() throws InvalidActionException {
        Tile tile = Board.getInstance().getTileByAnimal(this);
        return tile.compareSideWithAnimal(this) < 0;
    }

    @Override
    public String toString() {
        if (side == GameController.Side.LEFT) {
            return this.power + getName() + " ";
        } else {
            return " " + getName() + this.power;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Animal) && ((Animal) obj).power == this.power && ((Animal) obj).side == this.side;
    }

    private enum AnimalCollection {
        NULL(null), MOUSE(Mouse.class), CAT(Cat.class),
        WOLF(Wolf.class), DOG(Dog.class), LEOPARD(Leopard.class),
        TIGER(Tiger.class), LION(Lion.class), ELEPHANT(Elephant.class);

        private final Class animal;

        AnimalCollection(Class animal) {
            this.animal = animal;
        }

        public Animal getInstance() {
            if (this.animal == null) {
                return null;
            }
            try {
                return (Animal) this.animal.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
