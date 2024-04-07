package sk.tuke.gamestudio.game.Tetris.Core.Game;

import static org.junit.Assert.*;

import sk.tuke.gamestudio.game.Core.*;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    private Game game;
    private GameBoard gameBoard;

    @Before
    public void setUp() {
        gameBoard = new GameBoard(10, 20); // Create a 10x20 game board
        game = new Game(gameBoard);
    }

    @Test
    public void testGameInitialization() {
        assertEquals(GameState.PLAYING, game.getState());
        assertEquals(0, game.getScore());
        assertNotNull(game.getCurrentShape());
        assertNotNull(game.getShapeQueue());
    }

    @Test
    public void testRestartGame() {
        game.restartGame();
        assertEquals(GameState.PLAYING, game.getState());
        assertEquals(0, game.getScore());
        assertNotNull(game.getCurrentShape());
    }

    @Test
    public void testGameTickWithShapeAtBottom() {
        // Simulate the current shape reaching the bottom
        game.getCurrentShape().setState(ShapeState.AT_BOTTOM);
        game.gameTick();
        assertNotEquals(game.getCurrentShape(), game.getShapeQueue().getNextShape()); // Current shape should be different
    }

    @Test
    public void testGameTickWithHighScore() {
        game.setScore(10001);
        game.gameTick();
        assertEquals(GameState.WON, game.getState());
    }

    @Test
    public void testMoveCurrentShapeDown() {
        int initialRow = game.getCurrentShapeRow();
        game.moveCurrentShapeDown();
        assertEquals(initialRow + 1, game.getCurrentShapeRow());
    }

    @Test
    public void testMoveCurrentShapeRight() {
        int initialCol = game.getCurrentShapeCol();
        game.moveCurrentShape(true);
        assertEquals(initialCol + 1, game.getCurrentShapeCol());
    }

    @Test
    public void testMoveCurrentShapeLeft() {
        int initialCol = game.getCurrentShapeCol();
        game.moveCurrentShape(false);
        assertEquals(initialCol - 1, game.getCurrentShapeCol());
    }

    @Test
    public void testRotateCurrentShapeLeft() {
        Tile[][] initialBlock = game.getCurrentShape().getBlock();
        int initialHeight = game.getCurrentShape().getHeight();
        int initialWidth = game.getCurrentShape().getWidth();

        //rotate 90 degrees right
        game.rotateCurrentShape(true);
        //rotate 90 degrees left back to original shape
        game.rotateCurrentShape(false);

        assertEquals(initialHeight, game.getCurrentShape().getHeight());
        assertEquals(initialWidth, game.getCurrentShape().getWidth());
        assertEquals(initialBlock, game.getCurrentShape().getBlock());
    }
}
