package ca.uqam.info.student.skyjo.view;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import ca.uqam.info.max.skyjo.view.TextualCommandSelector;
import ca.uqam.info.max.skyjo.view.TextualVisualizer;
// TODO: Import YOUR ControllerImpl class here.
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Launcher for a textual / TTY session with all physical players sharing one keyboard / screen.
 *
 * @author Maximilian Schiedermeier
 */
public class LauncherTp3 {
  private static final String MSG_SOL_NB = "Entrez le nombre de joueurs (2-4) : ";
  private static final String MSG_ERR_NOMS = "\n *** Erreur : Il doit y avoir 2 à 4 joueurs ***\n";

  /**
   * Default constructor, as imposed by javadoc.
   */
  public LauncherTp3() {
  }

  /**
   * Replace command selector by robot players to obtain an automated game (also used for
   * integration testing).
   */
  public static CommandSelector commandSelector;

  private static int setAmountOfPlayers() {
    Scanner sc = new Scanner(System.in);
    int nb = 2;
    boolean valide = false;
    do {
      try {
        System.out.print(MSG_SOL_NB);
        nb = sc.nextInt();
        if (nb >= 2 && nb <= 4) {
          valide = true;
        } else {
          System.out.println(MSG_ERR_NOMS);
        }
      } catch (RuntimeException e) {
        System.out.println(MSG_ERR_NOMS);
        sc.nextLine();
      }
    } while (!valide);
    return nb;
  }

  private static String[] setPlayerNames(int nbJouers) {
    Scanner sc = new Scanner(System.in);
    ArrayList<String> noms = new ArrayList<>();
    for (int i = 1; i <= nbJouers; i++) {
      System.out.print("Entrez le nom du joueur (" + i + ") : ");
      noms.add(sc.nextLine());
      System.out.println();
    }
    return noms.toArray(String[]::new);
  }

  /**
   * Starts game by creating a new controller (which in turn creates a new model). Then keeps
   * prompting players for choices until game end is reached.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    // Register UI to automatically refresh on model updates
    boolean useTtyColours = true;
    int nbJoueurs;
    Random rand = new Random();
    System.out.println("\n=====\nSKYJO\n=====");
    System.out.println();
    System.out.println("Les règles du jeu : ");
    System.out.println("https://www.youtube.com/watch?v=I5tQDPW30yw");
    System.out.println();
    nbJoueurs = setAmountOfPlayers();
    System.out.println();
    int n = 100;
    int a = rand.nextInt(n);

    // Create a model, using your model constructor.
    // Make sure your model implements the provided model readonly interface
    String[] playerNames = setPlayerNames(nbJoueurs);
    System.out.println("(seed : " + a + ")");
    // TODO: Initialize a new game, using YOUR ControllerImpl class here.
    Controller controller = new ControllerImpl(); // Something like "new ControllerImpl(...)";
    // Register UI to automatically refresh on model updates
    controller.initializeModel(ModelPreset.DEFAULT, playerNames, new Random(a));
    // Register UI to automatically refresh on model updates
    controller.addModelObserver(new TextualVisualizer(controller.getModel(), useTtyColours));
    // Initialize commandSelector for interactive / TTY mode
    commandSelector = new TextualCommandSelector(useTtyColours, false);
    // Play the game :)
    playUntilGameEnd(controller);
  }

  /**
   * Note: This method is not concerned with updating model state representations, for the model
   * adheres to the observer pattern for this purpose.
   * This loop is only about retrieving user inputs until game end. The model is automatically
   * notified and re-rendered after each executed command.
   *
   * @param controller as the MVC controller allowing to progress the game command by command.
   *                   Note that the view has no direct access to the model, and can only
   *                   manipulate model state by executing commands.
   */
  private static void playUntilGameEnd(Controller controller) {
    // Initialize options for game start
    Command[] options = controller.getCurrentPlayerCommands();
    // Keep playing until controller offers no more options (game end)
    while (options.length > 0) {
      // Request a choice from human player - "undo"s have no relevance for INF2050, leave at
      // "false".
      int selectedCommand = commandSelector.selectCommand(options, false);
      // Execute choice (this implicitly re-renders the model)
      controller.doCommand(selectedCommand);
      // Update options
      options = controller.getCurrentPlayerCommands();
    }
  }
}