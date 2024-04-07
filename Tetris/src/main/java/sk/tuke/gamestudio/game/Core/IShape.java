package sk.tuke.gamestudio.game.Core;


public class IShape extends Shape {
    public IShape() {
        super(4, 1, new Tile[][]{
                {new Tile('I', true), new Tile('I', true),
                        new Tile('I', true), new Tile('I', true)}
        });
    }
}
