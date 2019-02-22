package edu.cnm.deepdive.life.controller;

import edu.cnm.deepdive.life.model.Cell;
import edu.cnm.deepdive.life.model.World;
import edu.cnm.deepdive.life.view.WorldView;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;

public class Life {

  private static final int DEFAULT_WORLD_SIZE = 500;
  private static final String STOP_KEY = "stop";
  private static final String GENERATION_DISPLAY_KEY = "generationDisplay";
  private static final String POPULATION_DISPLAY_KEY = "populationDisplay";
  private static final String START_KEY = "start";

  private World world;
  private Random rng;
  private Cell[][] terrain;
  private boolean running;
  private Updater updater;
  private double initialTerrainViewWidth;
  private double initialTerrainViewHeight;
  private String generationDisplayFormat;
  private String populationDisplayFormat;

  @FXML
  private Integer worldSize = DEFAULT_WORLD_SIZE;
  @FXML
  private Text generationDisplay;
  @FXML
  private Text populationDisplay;
  @FXML
  private ScrollPane viewScroller;
  @FXML
  private WorldView terrainView;
  @FXML
  private ToggleButton toggleRun;
  @FXML
  private Slider densitySlider;
  @FXML
  private Button reset;
  @FXML
  private CheckBox toggleFit;
  @FXML
  private ResourceBundle resources;

  @FXML
  private void initialize() {
    rng = new Random();
    updater = new Updater();
    terrain = new Cell[worldSize][worldSize];
    initialTerrainViewHeight = terrainView.getHeight();
    initialTerrainViewWidth = terrainView.getWidth();
    generationDisplayFormat = resources.getString(GENERATION_DISPLAY_KEY);
    populationDisplayFormat = resources.getString(POPULATION_DISPLAY_KEY);
    reset(null);
  }

  @FXML
  private void toggleRun(ActionEvent actionEvent) {
    if (toggleRun.isSelected()) {
      start();
    } else {
      stop();
    }
  }

  @FXML
  private void reset(ActionEvent actionEvent) {
    world = new World(worldSize, densitySlider.getValue() / 100, rng);
    Platform.runLater(this::updateDisplay);
  }

  @FXML
  private void toggleFit(ActionEvent actionEvent) {
    if (toggleFit.isSelected()) {
      terrainView.setWidth(viewScroller.getWidth() - 2);
      terrainView.setHeight(viewScroller.getHeight() - 2);
    } else {
      terrainView.setWidth(initialTerrainViewWidth);
      terrainView.setHeight(initialTerrainViewHeight);
    }
    if (!running) {
      updateDisplay();
    }
  }

  private void start() {
    running = true;
    toggleRun.setText(resources.getString(STOP_KEY));
    reset.setDisable(true);
    updater.start();
    new Runner().start();
  }

  public void stop() {
    running = false;
    updater.stop();
    toggleRun.setText(resources.getString(START_KEY));
    toggleRun.setSelected(false);
    reset.setDisable(false);
  }

  private void updateDisplay() {
    world.copyTerrain(terrain);
    terrainView.draw(terrain);
    generationDisplay.setText(String.format(generationDisplayFormat, world.getGeneration()));
    populationDisplay.setText(String.format(populationDisplayFormat, world.getPopulation()));
  }

  private class Runner extends Thread {

    private static final int HISTORY_LENGTH = 24;

    private Deque<Long> history = new LinkedList<>();

    @Override
    public void run() {
      while (running) {
       world.tick();
       long checksum = world.getChecksum();
       if (history.contains(checksum)) {
         Platform.runLater(Life.this::stop);
       } else {
         history.addLast(checksum);
         if (history.size() > HISTORY_LENGTH) {
           history.removeFirst();
         }
       }
      }
      Platform.runLater(Life.this::updateDisplay);
    }

  }

  public class Updater extends AnimationTimer {

    @Override
    public void handle(long now) {
      updateDisplay();
    }

  }

}
