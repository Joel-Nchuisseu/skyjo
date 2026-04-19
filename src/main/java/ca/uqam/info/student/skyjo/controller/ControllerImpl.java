package ca.uqam.info.student.skyjo.controller;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelFactory;
import ca.uqam.info.max.skyjo.controller.ModelObserver;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;
import ca.uqam.info.student.skyjo.controller.commands.EndGameCommand;
import ca.uqam.info.student.skyjo.controller.commands.EndTurnCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReduceColumnCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReduceRowCommand;
import ca.uqam.info.student.skyjo.controller.commands.RejectAndRevealCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReplaceFromBufferCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReplaceFromDiscardCommand;
import ca.uqam.info.student.skyjo.controller.commands.RevealDeckCardCommand;
import ca.uqam.info.student.skyjo.controller.commands.SpaceHolderCommand;
import ca.uqam.info.student.skyjo.model.SkyjoModelImpl;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Classe implémentant l'interface Controller.
 */
public class ControllerImpl implements Controller {
  private int gameEnder = -1;
  private int lastRound = -1;
  private boolean isGameOverAlreadyInitialized = false;
  private final Deque<Command> historiqueCommandes = new ArrayDeque<>();
  private SkyjoModelImpl model;
  private final List<ModelObserver> observers = new ArrayList<>();
  private final ModelFactory factory = new ModelFactoryImpl();

  /**
   * Constructeur par default.
   */
  public ControllerImpl() {
  }

  @Override
  public void initializeModel(ModelPreset preset, String[] players, Random seed) {
    model = (SkyjoModelImpl) factory.createModel(preset, players, seed);
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
    gameOverInitialization();
    return getCommands(nbLignes, nbColonnes, joueur);
  }

  private Command[] getCommands(int nbLignes, int nbColonnes, int joueur) {
    if (model.getCurrentPlayerIndex() != gameEnder || model.getRound() != lastRound) {
      // si le joueur a fini son coup
      if (aFiniSonCoup()) {
        return cmds();
      }
      // si le buffer n'est pas vide
      // les options possibles sont reveal deck card + 12 replace from discard
      if (!model.isBufferCardPresent()) {
        return getCommandsEmptyBuffer(nbLignes, nbColonnes, joueur);
      }
      // si le buffer est vide
      // les options possibles sont reject and reveal et replace from buffer
      return new RevealDeckCardCommand(model).getFollowUpCommands();
    } else {
      Command endGame = new EndGameCommand(model);
      endGame.execute();
      notifyObservers();
      return new Command[0];
    }
  }

  private void gameOverInitialization() {
    if (model.isGameOverInitialized() && !isGameOverAlreadyInitialized) {
      gameEnder = model.getGameEnder();
      lastRound = model.getRound() + 1;
      isGameOverAlreadyInitialized = true;
    }
  }

  @Override
  public void doCommand(int commandIndex) {
    Command[] options = getCurrentPlayerCommands();
    if (options.length != 0) {
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
          || historiqueCommandes.peek() instanceof RejectAndRevealCommand
          || historiqueCommandes.peek() instanceof ReduceRowCommand
          || historiqueCommandes.peek() instanceof ReduceColumnCommand;
    }
  }

  private Command[] eliminerLigne() {
    ArrayList<ReduceRowCommand> tmp = new ArrayList<>();
    int joueur = model.getCurrentPlayerIndex();
    int nbLignes = model.getCurrentDimensionsY(joueur);
    /*
    if (nbLignes == 1) {
      return new Command[0];
    }
    */
    for (int y = 0; y < nbLignes; y++) {
      if (model.ligneSupprimable(joueur, y)) {
        tmp.add(new ReduceRowCommand(model, joueur, y));
      }
    }
    return tmp.toArray(new Command[0]);
  }

  private Command[] eliminerColonne() {
    ArrayList<ReduceColumnCommand> tmp = new ArrayList<>();
    int joueur = model.getCurrentPlayerIndex();
    int nbColonnes = model.getCurrentDimensionsX(joueur);
    int nbLignes = model.getCurrentDimensionsY(joueur);
    /*
    if (nbLignes == 1 && nbColonnes == 1) {
      return new Command[0];
    }
    */
    for (int x = 0; x < nbColonnes; x++) {
      if (model.colonneSupprimable(joueur, x)) {
        tmp.add(new ReduceColumnCommand(model, joueur, x));
      }
    }
    return tmp.toArray(new Command[0]);
  }

  private Command[] cmds() {
    ArrayList<Command> tmp = new ArrayList<>();
    if (historiqueCommandes.peek() instanceof ReduceColumnCommand
        || historiqueCommandes.peek() instanceof ReduceRowCommand) {
      return new Command[] {new EndTurnCommand(model)};
    }
    Command[] cmdsReduceRow = eliminerLigne();
    Command[] cmdsReduceCol = eliminerColonne();
    tmp.addAll(Arrays.asList(cmdsReduceCol));
    tmp.addAll(Arrays.asList(cmdsReduceRow));
    tmp.add(new EndTurnCommand(model));
    return tmp.toArray(new Command[0]);
  }
}