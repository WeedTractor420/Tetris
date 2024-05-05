package sk.tuke.gamestudio.game.Core;


import lombok.Getter;
import lombok.Setter;

public class Tile {
    @Setter
    @Getter
    private char symbol;
    private boolean isOccupied;
    private boolean isShadow;

    public Tile(char symbol, boolean isOccupied, boolean isShadow) {
        this.symbol = symbol;
        this.isOccupied = isOccupied;
        this.isShadow = isShadow;
    }

    public boolean isOccupied() { return isOccupied; }

    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public boolean isShadow() {
        return isShadow;
    }

    public void setShadow(boolean shadow) {
        isShadow = shadow;
    }
}