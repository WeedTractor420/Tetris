//Abstract class representing specific shapes
//Shape also has attributes as width, height and ShapeState(Representing current state in the game)
package sk.tuke.gamestudio.game.Core;

import lombok.Getter;
import lombok.Setter;

public abstract class Shape {
    @Getter
    protected int width;
    @Getter
    protected int height;
    @Getter
    protected Tile[][] block;
    @Getter
    protected char shapeSymbol;
    @Getter
    @Setter
    private ShapeState state = ShapeState.NOT_INITIALIZED;

    public Shape(int width, int height, Tile[][] block, char shapeSymbol) {
        this.width = width;
        this.height = height;
        this.block = block;
        this.shapeSymbol = shapeSymbol;
    }

    public void rotateShape(boolean clockwise) {
        // Rotate the shape 90 degrees
        int newHeight = width;
        int newWidth = height;
        Tile[][] newBlock = new Tile[newHeight][newWidth];

        if (clockwise) {
            // Rotate clockwise
            for (int r = 0; r < newHeight; r++) {
                for (int c = 0; c < newWidth; c++) {
                    newBlock[r][c] = block[newWidth - 1 - c][r];
                }
            }
        } else {
            // Rotate counterclockwise
            for (int r = 0; r < newHeight; r++) {
                for (int c = 0; c < newWidth; c++) {
                    newBlock[r][c] = block[c][newHeight - 1 - r];
                }
            }
        }

        // Update the shape's properties
        block = newBlock;
        height = newHeight;
        width = newWidth;
    }

}