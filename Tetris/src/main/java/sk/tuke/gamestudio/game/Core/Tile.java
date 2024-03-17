package main.java.sk.tuke.gamestudio.game.Core;


public class Tile {
    private char symbol;
    private boolean isOccupied;

    public Tile(char symbol, boolean isOccupied) {
        this.symbol = symbol;
        this.isOccupied = isOccupied;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isOccupied() { return isOccupied; }

    public void setOccupied(boolean occupied) { isOccupied = occupied; }
}