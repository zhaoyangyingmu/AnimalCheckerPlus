package entity.animal.type;

import entity.tile.Tile;
import entity.animal.Animal;
import entity.Board;
import controller.GameController;
import exception.GameWinException;
import exception.InvalidActionException;

/**
 * Created by 谢东方xdf on 2017/1/3.
 */
public abstract class AnimalWithoutSkill extends Animal {

    @Override
    public void act(GameController.Direction direction, boolean simulate) throws InvalidActionException, GameWinException {
        Tile currentTile = Board.getInstance().getTileByAnimal(this);
        Tile nextTile = Board.getInstance().getTileByDirection(currentTile, direction);

        checkBoundary(nextTile);
        checkRiver(nextTile);
        move(currentTile, nextTile, simulate);
        if (!simulate) {
            checkWin(nextTile);
        }
    }



    private void checkRiver(Tile nextTile) throws InvalidActionException {
        if (nextTile.getType() == Tile.TileType.RIVER) {
            throw new InvalidActionException(this.getName() + "不能下水");
        }
    }
}