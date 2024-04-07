package sk.tuke.gamestudio.game.Core;


public class TShape extends Shape {
    public TShape() {
        super(3, 2, new Tile[][]{
                {new Tile(' ', false), new Tile('T', true),
                        new Tile(' ', false)},
                {new Tile('T', true), new Tile('T', true),
                        new Tile('T', true)}
        });
    }
}
