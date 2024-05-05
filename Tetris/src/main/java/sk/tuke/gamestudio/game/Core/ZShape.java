package sk.tuke.gamestudio.game.Core;

public class ZShape extends Shape {
    public ZShape() {
        super(3, 2, new Tile[][]{
                        {new Tile('Z', true, false),
                        new Tile('Z', true, false),
                        new Tile(' ', false, false)},

                        {new Tile(' ', false, false),
                        new Tile('Z', true, false),
                        new Tile('Z', true, false)}
        }, 'Z');
    }
}
