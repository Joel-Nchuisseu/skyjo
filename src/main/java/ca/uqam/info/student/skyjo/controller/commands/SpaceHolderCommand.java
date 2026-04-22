package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;

/**
 * Commande décorative sans action associée, utilisée pour l'alignement visuel
 * de la grille de commandes dans l'interface textuelle "---".
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 **/
public class SpaceHolderCommand implements Command {
  /**
   * Constructeur du space holder.
   */
  public SpaceHolderCommand() {
  }

  @Override
  public boolean isSpaceholder() {
    return true;
  }

  @Override
  public void execute() {
  }

  @Override
  public void undo() {
    throw new UnsupportedOperationException("Undo non supporté pour SpaceHolderCommand.");
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
    return "---";
  }
}
