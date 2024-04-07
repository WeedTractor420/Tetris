package sk.tuke.gamestudio.game.Core;

public class ZShape extends Shape {
    public ZShape() {
        super(3, 2, new Tile[][]{
                {new Tile('Z', true), new Tile('Z', true),
                        new Tile(' ', false)},
                {new Tile(' ', false), new Tile('Z', true),
                        new Tile('Z', true)}
        });
    }
}
