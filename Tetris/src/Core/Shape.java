//Abstract class representing specific shapes
//Shape is composed by char[][] block representing its graphics
//Shape also has attributes as width, height and ShapeState(Representing current state in the game)
package Core;

public abstract class Shape {
    protected int width;
    protected int height;
    protected char[][] block;
    private ShapeState state = ShapeState.NOT_INITIALIZED;

    public Shape(int width, int height, char[][] block) {
        this.width = width;
        this.height = height;
        this.block = block;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setBlock(char[][] block) {
        this.block = block;
    }
    public char[][] getBlock(){ return block; }
    public ShapeState getState() {
        return state;
    }
    public void setState(ShapeState state) {
        this.state = state;
    }

}