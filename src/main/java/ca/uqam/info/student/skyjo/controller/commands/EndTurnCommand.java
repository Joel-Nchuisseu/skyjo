package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;

/**
 * Commande permettant de céder le tour au joueur suivant.
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 **/
public class EndTurnCommand implements Command {
  private final SkyjoModel model;

  /**
   * Constructeur de la commande qui permet de passer au joueur suivant.
   *
   * @param model le model du jen.
   */
  public EndTurnCommand(SkyjoModelReadOnly model) {
    this.model = (SkyjoModel) model;
  }

  @Override
  public boolean isSpaceholder() {
    return false;
  }

  @Override
  public void execute() {
    model.advancePlayer();

  }

  @Override
  public void undo() {
    throw new UnsupportedOperationException("Undo non supporté pour RevealCommand.");
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
    return "End turn.";
  }
}
