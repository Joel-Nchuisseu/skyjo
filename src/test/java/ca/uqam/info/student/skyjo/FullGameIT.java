package ca.uqam.info.student.skyjo;

import ca.uqam.info.max.skyjo.ai.Keksli;
import ca.uqam.info.max.skyjo.ai.MadMax;
import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import ca.uqam.info.max.skyjo.view.TextualVisualizer;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Cette classe permet de jouer plusieurs parties avec des joueurs robotiques.
 */
public class FullGameIT {
  private Controller controller;
  private Controller controller2;
  private Controller controller3;

  /**
   * Obtenir un controller.
   *
   * @return le controller
   */
  private Controller getController() {
    return new ControllerImpl();
  }

  /**
   * Initialiser le controller.
   */
  @BeforeEach
  public void initializeControllerWithDefaultModel() {
    controller = getController();
    String[] players = new String[] {"Max", "Ryan", "Maram", "Quentin"};
    controller.initializeModel(ModelPreset.DEFAULT, players, null);
  }

  /**
   * Initialiser le controller avec seed 0.
   */
  @BeforeEach
  public void initializeControllerWithSeed0() {
    controller2 = getController();
    String[] players = new String[] {"Max", "Ryan", "Maram", "Quentin"};
    controller2.initializeModel(ModelPreset.DEFAULT, players, new Random(0));
  }

  /**
   * Initialiser le controller avec seed 42.
   */
  @BeforeEach
  public void initializeControllerWithSeed42() {
    controller3 = getController();
    String[] players = new String[] {"Max", "Ryan", "Maram", "Quentin"};
    controller3.initializeModel(ModelPreset.DEFAULT, players, new Random(42));
  }

  // =============
  // TEST FULLGAME
  // =============

  @Test
  public void fullGame1KekSli() {
    CommandSelector robot = new Keksli();
    // keksli seed null
    // Register UI to automatically refresh on model updates
    controller.addModelObserver(new TextualVisualizer(controller.getModel(), true));
    Command[] options = controller.getCurrentPlayerCommands();
    while (options.length > 0) {
      controller.doCommand(robot.selectCommand(options, false));
      options = controller.getCurrentPlayerCommands();
    }
  }

  @Test
  public void fullGame2MadMax() {
    // madmax 0 seed null
    CommandSelector robot = new MadMax(0);
    // Register UI to automatically refresh on model updates
    controller.addModelObserver(new TextualVisualizer(controller.getModel(), true));
    Command[] options = controller.getCurrentPlayerCommands();
    while (options.length > 0) {
      controller.doCommand(robot.selectCommand(options, false));
      options = controller.getCurrentPlayerCommands();
    }
  }

  @Test
  public void fullGame3KekSli() {
    // keksli seed 0
    CommandSelector robot = new Keksli();
    // Register UI to automatically refresh on model updates
    controller2.addModelObserver(new TextualVisualizer(controller2.getModel(), true));
    Command[] options = controller2.getCurrentPlayerCommands();
    while (options.length > 0) {
      controller2.doCommand(robot.selectCommand(options, false));
      options = controller2.getCurrentPlayerCommands();
    }
  }

  @Test
  public void fullGame4MadMax() {
    // madmax 0 seed 0
    CommandSelector robot = new MadMax(0);
    // Register UI to automatically refresh on model updates
    controller2.addModelObserver(new TextualVisualizer(controller2.getModel(), true));
    Command[] options = controller2.getCurrentPlayerCommands();
    while (options.length > 0) {
      controller2.doCommand(robot.selectCommand(options, false));
      options = controller2.getCurrentPlayerCommands();
    }
  }

  @Test
  public void fullGame5MadMax() {
    // madmax 42 seed 0
    CommandSelector robot = new MadMax(42);
    // Register UI to automatically refresh on model updates
    controller2.addModelObserver(new TextualVisualizer(controller2.getModel(), true));
    Command[] options = controller2.getCurrentPlayerCommands();
    while (options.length > 0) {
      controller2.doCommand(robot.selectCommand(options, false));
      options = controller2.getCurrentPlayerCommands();
    }
  }

  @Test
  public void fullGame6KekSli() {
    CommandSelector robot = new Keksli();
    // keksli seed 42
    // Register UI to automatically refresh on model updates
    controller3.addModelObserver(new TextualVisualizer(controller3.getModel(), true));
    Command[] options = controller3.getCurrentPlayerCommands();
    while (options.length > 0) {
      controller3.doCommand(robot.selectCommand(options, false));
      options = controller3.getCurrentPlayerCommands();
    }
  }


}
