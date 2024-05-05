package sk.tuke.gamestudio.game.Core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ShapeQueue {
    private final List<Shape> shapeQueue;
    @Getter
    private int index;

    public ShapeQueue() {
        this.shapeQueue = new ArrayList<>();
        generateShapeQueue();
    }

    private void generateShapeQueue() {
        for (int i = 0; i < 4; i++) {
            Shape newShape = createRandomShape();
            shapeQueue.add(newShape);
        }
    }

    private Shape createRandomShape() {
        int random = ThreadLocalRandom.current().nextInt(0, 7);
        return switch (random) {
            case 0 -> new OShape();
            case 1 -> new IShape();
            case 2 -> new ZShape();
            case 3 -> new LShape();
            case 4 -> new JShape();
            case 5 -> new SShape();
            case 6 -> new TShape();
            default -> throw new IllegalArgumentException("Invalid shape index");
        };
    }

    public Shape getNextShape() {
        Shape nextShape = shapeQueue.get(index);
        index++;
        shapeQueue.add(createRandomShape());
        return nextShape;
    }

    public List<Shape> getShapeQueue() {
        return new ArrayList<>(shapeQueue);
    }

    public void reset() {
        index = 0;
        shapeQueue.clear();
        generateShapeQueue();
    }
}
