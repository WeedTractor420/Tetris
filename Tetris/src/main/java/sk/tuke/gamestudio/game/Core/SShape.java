package sk.tuke.gamestudio.game.Core;


public class SShape extends Shape {
    public SShape() {
        super(3, 2, new Tile[][]{
                        {new Tile(' ', false, false),
                        new Tile('S', true, false),
                        new Tile('S', true, false)},

                        {new Tile('S', true, false),
                        new Tile('S', true, false),
                        new Tile(' ', false, false)}
        }, 'S');
    }
}