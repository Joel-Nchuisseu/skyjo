package ca.uqam.info.student.skyjo.view;

import ca.uqam.info.max.skyjo.ai.Keksli;
import ca.uqam.info.max.skyjo.ai.MadMax;
import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import ca.uqam.info.max.skyjo.view.TextualCommandSelector;
import ca.uqam.info.max.skyjo.view.TextualVisualizer;
import ca.uqam.info.student.skyjo.ai.S38;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import java.util.Objects;
import java.util.Random;

/**
 * Point d'entrée principal de l'application Skyjo.
 * Gère le démarrage d'une session de jeu en analysant les arguments
 * fournis en ligne de commande, en configurant les joueurs humains
 * et robotiques, puis en gérant la boucle de jeu.
 *
 * <p>Usage :</p>
 * <pre>
 *   java -jar Skyjo.jar [seed]? [joueur][joueur]+
 * </pre>
 *
 * <p>Les noms réservés suivants instancient automatiquement un joueur robotique :</p>
 * <ul>
 *   <li>{@code Keksli} — sélectionne toujours la première option disponible (niveau débutant)</li>
 *   <li>{@code MadMax} — sélectionne une option aléatoirement (niveau intermédiaire)</li>
 *   <li>{@code S38}    — stratégie basée sur l'analyse de l'état du modèle (niveau expert)</li>
 * </ul>
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 * @see ca.uqam.info.student.skyjo.controller.ControllerImpl
 * @see ca.uqam.info.max.skyjo.view.CommandSelector
 */
public class Launcher {
  /**
   * Instance du contrôleur gérant l'état de la session de jeu en cours.
   */
  public static Controller controller;
  private static CommandSelector[] joueurs;
  private static String[] playerNames;
  private static Integer seed;
  private static final String MSG_ERR = """
      
      Commande: java -jar Skyjo.jar [seed]? [joueur][joueur]+
      
      2 à 4 joueurs.
      Une seed optionnelle peut être fournie sous la forme d'un nombre.
      Le nom des joueurs doit être fourni.
      
      Certains noms sont réservés pour les joueurs robotiques:
         - Keksli : Choisit toujours la première option (Débutant).
         - MadMax : Choisit une option aléatoirement (Intermédiaire).
         - S38    : AI (Expert).
      
      Ex: java -jar Skyjo.jar 5 nom1 S38 MadMax Keksli
      Ex: java -jar Skyjo.jar nom1 nom2 nom3
      """;


  private Launcher() {
  }

  private static void configuration(String[] args) {
    if (args.length != 0) {
      try {
        // extraire le seed
        seed = Integer.parseInt(args[0]);
        playerNames = new String[args.length - 1];
        // lire les noms
        System.arraycopy(args, 1, playerNames, 0, args.length - 1);
        if (playerNames.length < 2 || playerNames.length > 4) {
          afficherMsgErr();
        }
      } catch (NumberFormatException e) {
        // lire les noms si pas de seed
        if (args.length >= 2 && args.length <= 4) {
          playerNames = args;
        } else {
          afficherMsgErr();
        }
      }
      // message d'erreur
    } else {
      afficherMsgErr();
    }
  }

  private static void afficherMsgErr() {
    System.out.println(MSG_ERR);
    System.exit(0);
  }

  /**
   * Point d'entrée principal de l'application.
   * Initialise la configuration à partir des arguments, crée le contrôleur,
   * configure les joueurs humains et robotiques, enregistre le visualiseur
   * textuel comme observateur du modèle, puis lance la boucle de jeu.
   *
   * @param args les arguments de la ligne de commande contenant optionnellement
   *             une graine suivie des noms des joueurs (2 à 4).
   */
  public static void main(String[] args) {

    configuration(args);

    // Register UI to automatically refresh on model updates
    boolean useTtyColours = true;
    controller = new ControllerImpl();
    // Register UI to automatically refresh on model updates
    controller.initializeModel(ModelPreset.DEFAULT, playerNames,
        new Random(Objects.requireNonNullElseGet(seed, Launcher::getRandomSeed)));
    // Register UI to automatically refresh on model updates
    controller.addModelObserver(new TextualVisualizer(controller.getModel(), useTtyColours));

    setPlayerAndRobots(controller);

    // Play the game :)
    playUntilGameEnd(controller);
  }

  private static void setPlayerAndRobots(Controller controller) {
    int nbKekSli = 1;
    int nbMadMax = 1;
    int nbS38 = 1;
    joueurs = new CommandSelector[playerNames.length];
    for (int i = 0; i < joueurs.length; i++) {
      if (playerNames[i].equalsIgnoreCase("madmax")) {
        nbMadMax = getNbMadMax(i, nbMadMax);
      } else if (playerNames[i].equalsIgnoreCase("keksli")) {
        nbKekSli = getNbKekSli(i, nbKekSli);
      } else if (playerNames[i].equalsIgnoreCase("s38")) {
        nbS38 = getNbS38(controller, i, nbS38);
      } else {
        joueurs[i] = new TextualCommandSelector(true, false);
      }
    }
  }

  private static int getNbS38(Controller controller, int i, int nbS38) {
    joueurs[i] = new S38(controller.getModel());
    playerNames[i] = nbS38 == 1 ? "s38" : "s38-" + nbS38;
    nbS38++;
    return nbS38;
  }

  private static int getNbKekSli(int i, int nbKekSli) {
    joueurs[i] = new Keksli();
    playerNames[i] = nbKekSli == 1 ? "Keksli" : "Keksli-" + nbKekSli;
    nbKekSli++;
    return nbKekSli;
  }

  private static int getNbMadMax(int i, int nbMadMax) {
    joueurs[i] = new MadMax(getRandomSeed());
    playerNames[i] = nbMadMax == 1 ? "MadMax" : "MadMax-" + nbMadMax;
    nbMadMax++;
    return nbMadMax;
  }

  private static int getRandomSeed() {
    return new Random().nextInt(100);
  }

  private static void playUntilGameEnd(Controller controller) {

    // Initialize options for game start
    Command[] options = controller.getCurrentPlayerCommands();

    // Keep playing until controller offers no more options (game end)
    while (options.length > 0) {

      controller.doCommand(
              joueurs[controller.getModel().getCurrentPlayerIndex()]
              .selectCommand(options, false));

      options = controller.getCurrentPlayerCommands();
    }
  }
}