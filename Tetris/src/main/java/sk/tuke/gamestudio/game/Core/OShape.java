package sk.tuke.gamestudio.game.Core;


public class OShape extends Shape {
    public OShape() {
        super(2, 2, new Tile[][]{
                {new Tile('O', true), new Tile('O', true)},
                {new Tile('O', true), new Tile('O', true)}
        });
    }
}
