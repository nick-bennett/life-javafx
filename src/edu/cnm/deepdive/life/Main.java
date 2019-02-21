package edu.cnm.deepdive.life;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  private static final String RESOURCE_DIR = "res/";
  private static final String BUNDLE_NAME = RESOURCE_DIR + "ui-strings";
  private static final String LAYOUT_NAME = RESOURCE_DIR + "life.fxml";
  private static final String WINDOW_TITLE_KEY = "title";

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource(LAYOUT_NAME), bundle);
    Parent parent = fxmlLoader.load();
    Scene scene = new Scene(parent);
    primaryStage.setScene(scene);
    primaryStage.setTitle(bundle.getString(WINDOW_TITLE_KEY));
    primaryStage.setResizable(false);
    primaryStage.show();
  }

}
