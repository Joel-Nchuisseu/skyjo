package ca.uqam.info.student.skyjo.controller;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelFactory;
import ca.uqam.info.max.skyjo.controller.ModelObserver;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Classe implémentant l'interface Controller.
 */
public class ControllerImpl implements Controller {
  private final Deque<Command> historiqueCommandes = new ArrayDeque<>();
  private SkyjoModel model;
  private final List<ModelObserver> observers = new ArrayList<>();
  private final ModelFactory factory = new ModelFactoryImpl();

  /**
   * Constructeur par default.
   */
  public ControllerImpl() {
  }

  @Override
  public void initializeModel(ModelPreset preset, String[] players, Random seed) {
    model = factory.createModel(preset, players, seed);
    notifyObservers();
  }

  @Override
  public SkyjoModelReadOnly getModel() {
    return model;
  }

  @Override
  public void addModelObserver(ModelObserver observer) {
    observers.add(observer);
    observer.refresh(); // notification immédiate
  }

  private void notifyObservers() {
    for (ModelObserver o : observers) {
      o.refresh();
    }
  }

  @Override
  public Command[] getCurrentPlayerCommands() {
    int joueur = model.getCurrentPlayerIndex();
    int nbLignes = model.getCurrentDimensionsY(joueur);
    int nbColonnes = model.getCurrentDimensionsX(joueur);
    // si le jeu est terminé pas de commande possible
    if (model.isGameOver()) {
      return new Command[0];
    }
    // si le joueur a fini son coup
    if (aFiniSonCoup()) {
      return new Command[] {new EndTurnCommand(model)};
    }
    // si le buffer n'est pas vide
    // les options possibles sont reveal deck card + 12 replace from discard
    if (!model.isBufferCardPresent()) {
      return getCommandsEmptyBuffer(nbLignes, nbColonnes, joueur);
    }
    // si le buffer est vide
    // les options possibles sont reject and reveal et replace from buffer
    return new RevealDeckCardCommand(model).getFollowUpCommands();
  }

  @Override
  public void doCommand(int commandIndex) {
    Command[] options = getCurrentPlayerCommands();
    List<Command> actionable = new ArrayList<>();
    for (Command c : options) {
      if (!c.isSpaceholder()) {
        actionable.add(c);
      }
    }
    validerCommandIndex(commandIndex, actionable);
    actionable.get(commandIndex).execute();
    notifyObservers();
    historiqueCommandes.push(actionable.get(commandIndex));
  }

  @Override
  public boolean isUndoAvailable() {
    if (historiqueCommandes.isEmpty()) {
      return false;
    } else {
      return historiqueCommandes.peek().isUndoable();
    }
  }

  @Override
  public void undoLastCommand() {
    // récupérer la dernière commande
    Command cmd = historiqueCommandes.peek();
    assert cmd != null;
    if (cmd.isUndoable()) {
      cmd.undo();
      historiqueCommandes.pop();
    } else {
      throw new RuntimeException("Impossible ! la commande n'est pas annulable.");
    }
  }

  private static void validerCommandIndex(int commandIndex, List<Command> actionable) {
    if (commandIndex < 0 || commandIndex >= actionable.size()) {
      throw new IllegalArgumentException("Index de commande invalide: " + commandIndex);
    }
  }

  private Command[] getCommandsEmptyBuffer(int nbLignes, int nbColonnes, int joueur) {
    ArrayList<Command> tmp = new ArrayList<>();
    tmp.add(new RevealDeckCardCommand(model));
    for (int i = 0; i < nbColonnes - 1; i++) {
      tmp.add(new SpaceHolderCommand());
    }
    for (int y = 0; y < nbLignes; y++) {
      for (int x = 0; x < nbColonnes; x++) {
        tmp.add(new ReplaceFromDiscardCommand(model, joueur, x, y));
      }
    }
    return tmp.toArray(new Command[0]);
  }

  @SuppressWarnings("checkstyle:MethodName")
  private boolean aFiniSonCoup() {
    if (historiqueCommandes.isEmpty()) {
      return false;
    } else {
      return historiqueCommandes.peek() instanceof ReplaceFromBufferCommand
          || historiqueCommandes.peek() instanceof ReplaceFromDiscardCommand
          || historiqueCommandes.peek() instanceof RejectAndRevealCommand;
    }
  }
}