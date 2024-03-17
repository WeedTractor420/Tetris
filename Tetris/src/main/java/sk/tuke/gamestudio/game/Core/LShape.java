package main.java.sk.tuke.gamestudio.game.Core;


public class LShape extends Shape {
    public LShape() {
        super(3, 2, new Tile[][]{
                {new Tile(' ', false), new Tile(' ', false),
                        new Tile('L', true)},
                {new Tile('L', true), new Tile('L', true),
                        new Tile('L', true)}
        });
    }
}
