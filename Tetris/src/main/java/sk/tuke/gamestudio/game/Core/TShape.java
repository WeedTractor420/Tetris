package sk.tuke.gamestudio.game.Core;


public class TShape extends Shape {
    public TShape() {
        super(3, 2, new Tile[][]{
                       {new Tile(' ', false, false),
                        new Tile('T', true, false),
                        new Tile(' ', false, false)},

                       {new Tile('T', true, false),
                        new Tile('T', true, false),
                        new Tile('T', true, false)}
        }, 'T');
    }
}
