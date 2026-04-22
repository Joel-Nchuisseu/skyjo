package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.ModelAccessException;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;

/**
 * Commande permettant de remplacer directement une carte du joueur
 * par la carte au sommet de la pile de défausse.
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 **/
public class ReplaceFromDiscardCommand implements Command {
  private final boolean isUndoAble;
  private final SkyjoModel model;
  private final int joueur;
  private final int colonne;
  private final int ligne;

  /**
   * Constructeur de la commande permettant de remplacer une carte par celle dans la défausse.
   *
   * @param model   le model.
   * @param joueur  l'indice du joueur.
   * @param colonne la colonne.
   * @param ligne   la ligne.
   */
  public ReplaceFromDiscardCommand(SkyjoModelReadOnly model, int joueur, int colonne, int ligne) {
    this.model = (SkyjoModel) model;
    this.joueur = joueur;
    this.colonne = colonne;
    this.ligne = ligne;
    isUndoAble = model.isPlayerCardAtPositionRevealed(joueur, this.colonne, this.ligne);
  }

  @Override
  public void execute() {
    model.pushDiscardPile(model.replacePlayerCard(joueur, colonne, ligne, model.popDiscardPile()));
    if (!model.isPlayerCardAtPositionRevealed(joueur, colonne, ligne)) {
      model.revealPlayerCard(joueur, colonne, ligne);
    }
  }

  @Override
  public Command[] getFollowUpCommands() {
    return new Command[] {new EndTurnCommand(model)};
  }

  @Override
  public boolean isSpaceholder() {
    return false;
  }

  @Override
  public boolean isUndoable() {
    return isUndoAble;
  }

  @Override
  public void undo() {
    if (isUndoAble) {
      execute();
    } else {
      throw new UnsupportedOperationException(
          "Impossible. Une nouvelle information a été révélée.");
    }
  }

  @Override
  public String toString() {
    return "Replace" + " (" + colonne + "/" + ligne + ")";
  }
}