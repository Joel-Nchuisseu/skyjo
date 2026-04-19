package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;

/**
 * Cette classe est la commande qui permet de terminer le jeu.
 */
public class EndGameCommand implements Command {
  private final SkyjoModel model;

  /**
   * Constructeur de la commande qui permet de terminer le jeu.
   *
   * @param model le model du jeu en accès écriture.
   */
  public EndGameCommand(SkyjoModelReadOnly model) {
    this.model = (SkyjoModel) model;
  }

  @Override
  public boolean isSpaceholder() {
    return false;
  }

  @Override
  public void execute() {
    model.endGame();
  }

  @Override
  public void undo() {
    throw new UnsupportedOperationException("Undo n'est pas supporté pour la commande EndGame");
  }

  @Override
  public boolean isUndoable() {
    return false;
  }

  @Override
  public Command[] getFollowUpCommands() {
    return new Command[0];
  }

  @Override
  public String toString() {
    return "";
  }
}
