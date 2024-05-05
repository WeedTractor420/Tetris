package sk.tuke.gamestudio.game.Core;


public class LShape extends Shape {
    public LShape() {
        super(3, 2, new Tile[][]{
                       {new Tile(' ', false, false),
                        new Tile(' ', false, false),
                        new Tile('L', true, false)},

                       {new Tile('L', true, false),
                        new Tile('L', true, false),
                        new Tile('L', true, false)}
        }, 'L');
    }
}
