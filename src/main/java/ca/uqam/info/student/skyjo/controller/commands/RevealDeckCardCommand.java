package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;
import java.util.ArrayList;

/**
 * Cette classe permet de prendre la carte au-dessus de la pile.
 */
public class RevealDeckCardCommand implements Command {
  private final SkyjoModel model;

  /**
   * La commande permettant de prendre la carte au-dessus de la pile.
   *
   * @param model le model.
   */
  public RevealDeckCardCommand(SkyjoModelReadOnly model) {
    this.model = (SkyjoModel) model;
  }

  @Override
  public void execute() {
    // a) Reveal deck card : piocher du deck et mettre en buffer
    model.setBufferCard(model.popDeck());
  }

  //
  @Override
  public Command[] getFollowUpCommands() {
    int joueur = model.getCurrentPlayerIndex();
    int nbLignes = model.getCurrentDimensionsY(joueur);
    int nbColonnes = model.getCurrentDimensionsX(joueur);
    // apres avoir pris la carte de la pile
    // il est possible de sois la rejeter et reveler une carte
    // ou la replacer avec une carte de la matrice
    return getCommands(nbLignes, nbColonnes, joueur);
  }

  private Command[] getCommands(int nbLignes, int nbColonnes, int joueur) {
    Command[] commands;
    ArrayList<Command> tmp = new ArrayList<>();
    // ajouter les reject and replace
    addRejectAndRevealCommands(nbLignes, nbColonnes, joueur, tmp);
    // ajouter les commandes ReplaceFromBufferCommand
    addReplaceFromBufferCommands(nbLignes, nbColonnes, joueur, tmp);
    // recopier
    commands = tmp.toArray(Command[]::new);
    return commands;
  }

  private void addReplaceFromBufferCommands(int nbLignes, int nbColonnes, int joueur,
                                            ArrayList<Command> tmp) {
    for (int y = 0; y < nbLignes; y++) {
      for (int x = 0; x < nbColonnes; x++) {
        tmp.add(new ReplaceFromBufferCommand(model, joueur, x, y));
      }
    }
  }

  private void addRejectAndRevealCommands(int nbLignes, int nbColonnes, int joueur,
                                          ArrayList<Command> tmp) {
    for (int y = 0; y < nbLignes; y++) {
      for (int x = 0; x < nbColonnes; x++) {
        Command tmpCommand = new RejectAndRevealCommand(model, joueur, x, y);
        if (!tmpCommand.isSpaceholder()) {
          tmp.add(tmpCommand);
        } else {
          tmp.add(new SpaceHolderCommand());
        }
      }
    }
  }

  @Override
  public boolean isSpaceholder() {
    return false;
  }

  @Override
  public boolean isUndoable() {
    return false;
  }

  @Override
  public void undo() {
    throw new UnsupportedOperationException("Undo non supporté pour RevealCommand.");
  }

  @Override
  public String toString() {
    return "Reveal deck card";
  }
}