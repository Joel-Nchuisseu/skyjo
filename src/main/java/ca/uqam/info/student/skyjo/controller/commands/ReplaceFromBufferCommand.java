package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.ModelAccessException;
import ca.uqam.info.max.skyjo.model.SkyjoModel;

/**
 * Commande permettant de remplacer une carte du joueur par celle en buffer,
 * précédemment piochée depuis le deck via {@link RevealDeckCardCommand}.
 * L'ancienne carte du joueur est déposée sur la pile de défausse.
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 **/
public class ReplaceFromBufferCommand implements Command {
  private final boolean isUndoAble;
  private final SkyjoModel model;
  private final int joueur;
  private final int colonne;
  private final int ligne;

  /**
   * Constructeur de la commande qui permet de remplacer
   * une carte de la matrice par la carte dans le buffer.
   *
   * @param model   le model.
   * @param joueur  l'indice du joueur.
   * @param colonne la colonne.
   * @param ligne   la ligne.
   */
  public ReplaceFromBufferCommand(SkyjoModel model, int joueur, int colonne, int ligne) {
    this.model = model;
    this.joueur = joueur;
    this.colonne = colonne;
    this.ligne = ligne;
    this.isUndoAble = model.isPlayerCardAtPositionRevealed(joueur, colonne, ligne);
  }

  @Override
  public boolean isSpaceholder() {
    return false;
  }

  @Override
  public void execute() {
    model.pushDiscardPile(model.replacePlayerCard(joueur, colonne, ligne, model.popBufferCard()));
    if (!model.isPlayerCardAtPositionRevealed(joueur, colonne, ligne)) {
      model.revealPlayerCard(joueur, colonne, ligne);
    }
  }

  @Override
  public void undo() {
    if (isUndoAble) {
      model.setBufferCard(model.replacePlayerCard(joueur, colonne, ligne, model.popDiscardPile()));
    } else {
      throw new UnsupportedOperationException(
          "Impossible. Une nouvelle information a été révélée.");
    }
  }

  @Override
  public boolean isUndoable() {
    return isUndoAble;
  }

  @Override
  public Command[] getFollowUpCommands() {
    return new Command[] {new EndTurnCommand(model)};
  }

  @Override
  public String toString() {
    return "Replace" + " (" + colonne + "/" + ligne + ")";
  }
}
