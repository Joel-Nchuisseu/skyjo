package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;

/**
 * Cette classe crée un space holder "---" pour indiquer qu'il n'y a pas de commande a cette option.
 */
public class SpaceHolderCommand implements Command {
  @Override
  public boolean isSpaceholder() {
    return true;
  }

  /**
   * Constructeur par default du space holder.
   */
  public SpaceHolderCommand() {
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
