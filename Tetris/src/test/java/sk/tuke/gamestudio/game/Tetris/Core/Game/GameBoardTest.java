package sk.tuke.gamestudio.game.Tetris.Core.Game;

import sk.tuke.gamestudio.game.Core.GameBoard;
import sk.tuke.gamestudio.game.Core.OShape;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    @Test
    public void testAddOShapeToBoard() {
        GameBoard board = new GameBoard(10, 10);
        OShape oShape = new OShape();

        board.addShapeToBoard(oShape, 0, 0);

        // Check if the shape was added correctly
        for (int i = 0; i < oShape.getHeight(); i++) {
            for (int j = 0; j < oShape.getWidth(); j++) {
                assertTrue(board.getBoard()[i][j].isOccupied());
                assertEquals(oShape.getBlock()[i][j].getSymbol(), board.getBoard()[i][j].getSymbol());
            }
        }
    }

    @Test
    public void testInitializeBoard() {
        GameBoard board = new GameBoard(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 4) {
                    assertEquals('_', board.getBoard()[i][j].getSymbol());
                    assertTrue(board.getBoard()[i][j].isOccupied());
                } else if (j == 0 || j == 4) {
                    assertEquals('|', board.getBoard()[i][j].getSymbol());
                    assertTrue(board.getBoard()[i][j].isOccupied());
                } else {
                    assertEquals(' ', board.getBoard()[i][j].getSymbol());
                    assertFalse(board.getBoard()[i][j].isOccupied());
                }
            }
        }
    }

    @Test
    public void testAddShapeToBoard() {
        GameBoard board = new GameBoard(5, 5);
        OShape shape = new OShape();
        board.addShapeToBoard(shape, 1, 1);
        for (int i = 0; i < shape.getHeight(); i++) {
            for (int j = 0; j < shape.getWidth(); j++) {
                if (shape.getBlock()[i][j].isOccupied()) {
                    assertEquals(shape.getBlock()[i][j].getSymbol(), board.getBoard()[i + 1][j + 1].getSymbol());
                    assertTrue(board.getBoard()[i + 1][j + 1].isOccupied());
                }
            }
        }
    }

    @Test
    public void testCanPlaceShapeOnBoard() {
        GameBoard board = new GameBoard(5, 5);
        OShape shape = new OShape();
        assertTrue(board.canPlaceShapeOnBoard(shape, 1, 1));
        board.addShapeToBoard(shape, 1, 1);
        assertFalse(board.canPlaceShapeOnBoard(shape, 1, 1));
    }

    @Test
    public void testDeleteShapeFromBoard() {
        GameBoard board = new GameBoard(5, 5);
        OShape shape = new OShape();
        board.addShapeToBoard(shape, 1, 1);
        board.deleteShapeFromTheBoard(shape, 1, 1);
        for (int i = 0; i < shape.getHeight(); i++) {
            for (int j = 0; j < shape.getWidth(); j++) {
                if (shape.getBlock()[i][j].isOccupied()) {
                    assertEquals(' ', board.getBoard()[i + 1][j + 1].getSymbol());
                    assertFalse(board.getBoard()[i + 1][j + 1].isOccupied());
                }
            }
        }
    }

    @Test
    public void testUpdateBoard() {
        GameBoard gameBoard = new GameBoard(20, 10);
        // Add a complete row to the board
        for (int j = 1; j < gameBoard.getColCount() - 1; j++) {
            gameBoard.getBoard()[0][j].setSymbol('X');
            gameBoard.getBoard()[0][j].setOccupied(true);

            gameBoard.getBoard()[2][j].setSymbol('X');
            gameBoard.getBoard()[2][j].setOccupied(true);
        }

        gameBoard.getBoard()[1][1].setSymbol('X');
        gameBoard.getBoard()[1][1].setOccupied(true);

        // Update the board and check if a row was deleted
        int deletedRows = gameBoard.updateBoard().size();
        assertEquals(2, deletedRows);

        // Check if the first row is now empty
        for (int j = 1; j < gameBoard.getColCount() - 1; j++) {
            assertEquals(' ', gameBoard.getBoard()[0][j].getSymbol());
            assertFalse(gameBoard.getBoard()[0][j].isOccupied());
            assertEquals(' ', gameBoard.getBoard()[2][j].getSymbol());
            assertFalse(gameBoard.getBoard()[2][j].isOccupied());
        }

        assertEquals( 'X',gameBoard.getBoard()[3][1].getSymbol());
    }
}
