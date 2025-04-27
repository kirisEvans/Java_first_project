package controller;

import model.Direction;
import model.MapModel;
import view.game.BoxComponent;
import view.game.GameFrame;
import view.game.GamePanel;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    private MapModel model;
    private GameFrame gameFrame;
    private boolean be_success = false;

    public GameController(GameFrame gameFrame, GamePanel view, MapModel model) {
        this.gameFrame = gameFrame;
        this.view = view;
        this.model = model;
        view.setController(this);
    }

    public void restartGame() {
        System.out.println("Do restart game here");
    }

    public boolean doMove(int row, int col, Direction direction) {
        boolean be_executed = false;
        if (model.getId(row, col) == 1) {
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol)) {
                if (model.getId(nextRow, nextCol) == 0) {
                    model.getMatrix()[row][col] = 0;
                    model.getMatrix()[nextRow][nextCol] = 1;
                    BoxComponent box = view.getSelectedBox();
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setLocation(box.getCol() * view.getGrid_size() + 2, box.getRow() * view.getGrid_size() + 2);
                    return true;
                }
            }
        }
        if (model.getId(row, col) == 2) {
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol)) {
                if (direction == Direction.LEFT) {
                    if (model.getId(nextRow, nextCol) == 0) {
                        model.getMatrix()[row][col+1] = 0;
                        model.getMatrix()[nextRow][nextCol] = 2;
                        be_executed = true;
                    }
                }
                else if (direction == Direction.RIGHT && model.checkInWidthSize(nextCol+1)) {
                    if (model.getId(nextRow, nextCol+1) == 0) {
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[nextRow][nextCol+1] = 2;
                        be_executed = true;
                    }
                }
                else if (direction == Direction.UP || direction == Direction.DOWN) {
                    if (model.getId(nextRow, nextCol) == 0 && model.getId(nextRow, nextCol+1) == 0) {
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[row][col+1] = 0;
                        model.getMatrix()[nextRow][nextCol] = 2;
                        model.getMatrix()[nextRow][nextCol+1] = 2;
                        be_executed = true;
                    }
                }
                if (be_executed) {
                    BoxComponent box = view.getSelectedBox();
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setLocation(box.getCol() * view.getGrid_size() + 2, box.getRow() * view.getGrid_size() + 2);
                    return true;
                }
            }
        }
        if (model.getId(row, col) == 3) {
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol)) {
                if (direction == Direction.UP) {
                    if (model.getId(nextRow, nextCol) == 0) {
                        model.getMatrix()[row+1][col] = 0;
                        model.getMatrix()[nextRow][nextCol] = 3;
                        be_executed = true;
                    }
                }
                else if (direction == Direction.DOWN && model.checkInHeightSize(nextRow+1)) {
                    if (model.getId(nextRow+1, nextCol) == 0) {
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[nextRow+1][nextCol] = 3;
                        be_executed = true;
                    }
                }
                else if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    if (model.getId(nextRow, nextCol) == 0 && model.getId(nextRow+1, nextCol) == 0) {
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[row+1][col] = 0;
                        model.getMatrix()[nextRow][nextCol] = 3;
                        model.getMatrix()[nextRow+1][nextCol] = 3;
                        be_executed = true;
                    }
                }
                if (be_executed) {
                    BoxComponent box = view.getSelectedBox();
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setLocation(box.getCol() * view.getGrid_size() + 2, box.getRow() * view.getGrid_size() + 2);
                    return true;
                }
            }
        }
        if (model.getId(row, col) == 4) {
            int nextRow = row + direction.getRow();
            int nextCol = col + direction.getCol();
            if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol)) {
                if (direction == Direction.UP) {
                    if (model.getId(nextRow, nextCol) == 0 && model.getId(nextRow, nextCol+1) == 0) {
                        model.getMatrix()[row+1][col] = 0;
                        model.getMatrix()[row+1][col+1] = 0;
                        model.getMatrix()[nextRow][nextCol] = 4;
                        model.getMatrix()[nextRow][nextCol+1] = 4;
                        be_executed = true;
                    }
                }
                else if (direction == Direction.DOWN && model.checkInHeightSize(nextRow+1)) {
                    if (model.getId(nextRow+1, nextCol) == 0 && model.getId(nextRow+1, nextCol+1) == 0) {
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[row][col+1] = 0;
                        model.getMatrix()[nextRow+1][nextCol] = 4;
                        model.getMatrix()[nextRow+1][nextCol+1] = 4;
                        be_executed = true;
                    }
                }
                else if (direction == Direction.LEFT) {
                    if (model.getId(nextRow, nextCol) == 0 && model.getId(nextRow+1, nextCol) == 0) {
                        model.getMatrix()[row][col+1] = 0;
                        model.getMatrix()[row+1][col+1] = 0;
                        model.getMatrix()[nextRow][nextCol] = 4;
                        model.getMatrix()[nextRow+1][nextCol] = 4;
                        be_executed = true;
                    }
                }
                else if (direction == Direction.RIGHT && model.checkInWidthSize(nextCol+1)) {
                    if (model.getId(nextRow, nextCol+1) == 0 && model.getId(nextRow+1, nextCol+1) == 0) {
                        model.getMatrix()[row][col] = 0;
                        model.getMatrix()[row+1][col] = 0;
                        model.getMatrix()[nextRow][nextCol+1] = 4;
                        model.getMatrix()[nextRow+1][nextCol+1] = 4;
                        be_executed = true;
                    }
                }
                if (be_executed) {
                    BoxComponent box = view.getSelectedBox();
                    box.setRow(nextRow);
                    box.setCol(nextCol);
                    box.setLocation(box.getCol() * view.getGrid_size() + 2, box.getRow() * view.getGrid_size() + 2);
                    if (model.getMatrix()[model.getSuccess_condition()[0]][model.getSuccess_condition()[1]] == 4
                    && model.getMatrix()[model.getSuccess_condition()[0] + 1][model.getSuccess_condition()[1] + 1] == 4) {
                        be_success = true;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBe_success() {
        return be_success;
    }

    public void setBe_success(boolean be_success) {
        this.be_success = be_success;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    //todo: add other methods such as loadGame, saveGame...

}
