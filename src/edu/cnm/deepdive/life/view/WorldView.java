package edu.cnm.deepdive.life.view;

import edu.cnm.deepdive.ca.model.Cell;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WorldView extends Canvas {

  private static final StyleablePropertyFactory<WorldView> FACTORY =
      new StyleablePropertyFactory<>(Canvas.getClassCssMetaData());
  private static final CssMetaData<WorldView, Color> COLOR =
      FACTORY.createColorCssMetaData("-cell-color", s -> s.color, Color.GREEN);
  private static final CssMetaData<WorldView, Shape> SHAPE =
      FACTORY.createEnumCssMetaData(Shape.class, "-cell-shape", s -> s.shape, Shape.ELLIPSE);

  private final StyleableProperty<Shape> shape =
      new SimpleStyleableObjectProperty<Shape>(SHAPE, this, "shape", Shape.ELLIPSE);
  private final StyleableProperty<Color> color =
      new SimpleStyleableObjectProperty<Color>(COLOR, this, "color", Color.GREEN);

  public WorldView() {
    getStyleClass().add("world-view");
  }

  public WorldView(double width, double height) {
    super(width, height);
    getStyleClass().add("world-view");
  }

  public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
    return FACTORY.getCssMetaData();
  }

  @Override
  public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
    return getClassCssMetaData();
  }

  public ObservableValue<Color> colorProperty() {
    return (ObservableValue<Color>) color;
  }

  public Color getColor() {
    return color.getValue();
  }

  public void setColor(Color color) {
    this.color.setValue(color);
  }

  public ObservableValue<Shape> shapeProperty() {
    return (ObservableValue<Shape>) shape;
  }

  public Shape getShape() {
    return shape.getValue();
  }

  public void setShape(Shape shape) {
    this.shape.setValue(shape);
  }

  public void draw(Cell[][] terrain) {
    GraphicsContext context = getGraphicsContext2D();
    double cellSize = Math.min(getWidth() / terrain[0].length, getHeight() / terrain.length);
    ShapeFiller filler = (cellSize < 3) ? Shape.RECTANGLE.filler() : shape.getValue().filler();
    context.clearRect(0, 0, getWidth(), getHeight());
    context.setFill(color.getValue());
    for (int row = 0; row < terrain.length; row++) {
      for (int col = 0; col < terrain[row].length; col++) {
        if (terrain[row][col] == Cell.ALIVE) {
          filler.fill(context, col * cellSize, row * cellSize, cellSize, cellSize);
        }
      }
    }
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
