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
    public void setHeight(int height) {
        this.height = height;
    }
    public void setBlock(char[][] block) {
        this.block = block;
    }
    public ShapeState getState() {
        return state;
    }
    public void setState(ShapeState state) {
        this.state = state;
    }

}