package sk.tuke.gamestudio.game.Core;


public class JShape extends Shape {
    public JShape() {
        super(3, 2, new Tile[][]{
                       { new Tile('J', true, false),
                         new Tile(' ', false, false),
                         new Tile(' ', false, false) },

                       { new Tile('J', true, false),
                         new Tile('J', true, false),
                         new Tile('J', true, false) }
        }, 'J');
    }
}
