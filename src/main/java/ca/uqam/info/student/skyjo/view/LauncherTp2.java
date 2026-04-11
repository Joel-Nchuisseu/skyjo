package ca.uqam.info.student.skyjo.view;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import ca.uqam.info.max.skyjo.view.TextualCommandSelector;
import ca.uqam.info.max.skyjo.view.TextualVisualizer;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;

/**
 * Launcher for a textual / TTY session with all physical players sharing one keyboard / screen.
 *
 * @author Maximilian Schiedermeier
 */
public class LauncherTp2 {

  /**
   * Default constructor, as imposed by javadoc.
   */
  public LauncherTp2() {
  }

  /**
   * Replace command selector by robot players to obtain an automated game (also used for
   * integration testing).
   */
  private static CommandSelector commandSelector;

  /**
   * Starts game by creating a new controller (which in turn creates a new model). Then keeps
   * prompting players for choices until game end is reached.
   *
   * @param args not used.
   */
  public static void main(String[] args) {

    // Register UI to automatically refresh on model updates
    boolean useTtyColours = false;

    // Create a model, using your model constructor.
    // Make sure your model implements the provided model readonly interface
    String[] playerNames = new String[] {"Max", "Ryan", "Maram", "Quentin"};

    // Initialize a new game, using YOUR controller implementation.
    Controller controller = new ControllerImpl();

    // Register UI to automatically refresh on model updates
    controller.initializeModel(ModelPreset.DEFAULT, playerNames, null);
    controller.addModelObserver(new TextualVisualizer(controller.getModel(), useTtyColours));

    // Print initial commands, as determined by YOUR controller implementation.
    Command[] options = controller.getCurrentPlayerCommands();
    TextualCommandSelector commandSelector = new TextualCommandSelector(useTtyColours, false);
    commandSelector.visualizeCommands(options, false, useTtyColours);
  }
}