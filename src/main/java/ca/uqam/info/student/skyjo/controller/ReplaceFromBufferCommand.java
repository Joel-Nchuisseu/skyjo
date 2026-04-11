package ca.uqam.info.student.skyjo.controller;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.ModelAccessException;
import ca.uqam.info.max.skyjo.model.SkyjoModel;

/**
 * Cette classe permet de remplacer une carte de la matrice par la carte dans le buffer.
 */
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
    //model.revealPlayerCard(joueur, colonne, ligne);
    // verifier s'il y a des lignes ou des colonnes à supprimer
    elimination();
    // verifier si le joueur a reveler toutes ses cartes
    gameEnder();
  }

  @SuppressWarnings("checkstyle:EmptyCatchBlock")
  private void elimination() {
    // Vérifier toutes les lignes
    for (int y = model.getCurrentDimensionsY(joueur) - 1; y >= 0; y--) {
      try {
        pushInDiscard(model.eliminateRow(joueur, y));
      } catch (ModelAccessException ignored) {
      }
    }
    // Vérifier toutes les colonnes
    for (int x = model.getCurrentDimensionsX(joueur) - 1; x >= 0; x--) {
      try {
        pushInDiscard(model.eliminateColumn(joueur, x));
      } catch (ModelAccessException ignored) {
      }
    }
  }

  private void gameEnder() {
    // Si la matrice est vide après éliminations, c'est aussi une fin de jeu
    if (model.getCurrentDimensionsX(joueur) == 0
        || model.getCurrentDimensionsY(joueur) == 0) {
      if (!model.isGameOverInitialized()) {
        model.setGameEnder();
      }
      return;
    }

    if (model.isAllPlayerCardsRevealed(joueur) && !model.isGameOverInitialized()) {
      model.setGameEnder();
    }
  }

  private void pushInDiscard(Card[] cards) {
    for (Card c : cards) {
      model.pushDiscardPile(c);
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
