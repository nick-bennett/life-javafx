package edu.cnm.deepdive.life.view;

import edu.cnm.deepdive.ca.model.Cell;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WorldView extends Canvas {

  public void draw(Cell[][] terrain) {
    GraphicsContext context = getGraphicsContext2D();
    double cellSize = Math.min(
        (double) getWidth() / terrain[0].length, (double) getHeight() / terrain.length);
    context.clearRect(0, 0, getWidth(), getHeight());
    context.setFill(Color.GREEN);
    for (int row = 0; row < terrain.length; row++) {
      for (int col = 0; col < terrain[row].length; col++) {
        if (terrain[row][col] == Cell.ALIVE) {
          context.fillOval(col * cellSize, row * cellSize, cellSize, cellSize);
        }
      }
    }
  }

}
