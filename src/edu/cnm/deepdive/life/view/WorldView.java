package edu.cnm.deepdive.life.view;

import edu.cnm.deepdive.ca.model.Cell;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WorldView extends Canvas {

  private Color color = Color.GREEN;
  private Shape shape = Shape.ELLIPSE;

  public void draw(Cell[][] terrain) {
    GraphicsContext context = getGraphicsContext2D();
    double cellSize = Math.min(getWidth() / terrain[0].length, getHeight() / terrain.length);
    context.clearRect(0, 0, getWidth(), getHeight());
    context.setFill(color);
    for (int row = 0; row < terrain.length; row++) {
      for (int col = 0; col < terrain[row].length; col++) {
        if (terrain[row][col] == Cell.ALIVE) {
          shape.filler().fill(context, col * cellSize, row * cellSize, cellSize, cellSize);
        }
      }
    }
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Shape getShape() {
    return shape;
  }

  public void setShape(Shape shape) {
    this.shape = shape;
  }

  private interface ShapeFiller {

    void fill(GraphicsContext context, double left, double top, double width, double height);

  }

  public enum Shape {
    RECTANGLE(GraphicsContext::fillRect),
    ELLIPSE(GraphicsContext::fillOval),
    ROUNDED_RECTANGLE((context, x, y, w, h) -> context.fillRoundRect(x, y, w, h, w / 3, h / 3));

    private final ShapeFiller filler;

    Shape(ShapeFiller filler) {
      this.filler = filler;
    }

    public ShapeFiller filler() {
      return filler;
    }

  }

}
