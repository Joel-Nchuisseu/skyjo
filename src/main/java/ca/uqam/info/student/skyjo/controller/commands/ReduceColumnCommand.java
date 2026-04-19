package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;

/**
 * Cette classe est la commande permettant d'éliminer une ligne de la matrice d'un joueur.
 */
public class ReduceColumnCommand implements Command {

  private SkyjoModel model;
  private int joueur;
  private int colonne;

  /**
   * Constructeur de la commande permettant d'éliminer une colonne.
   *
   * @param model  le model du jeu en accès écriture
   * @param joueur l'indice du joueur
   * @param colonne  l'indice de la colonne
   */
  public ReduceColumnCommand(SkyjoModelReadOnly model, int joueur, int colonne) {
    this.model = (SkyjoModel) model;
    this.joueur = joueur;
    this.colonne = colonne;
  }

  @Override
  public boolean isSpaceholder() {
    return false;
  }

  @Override
  public void execute() {
    pushInDiscard(model.eliminateColumn(joueur, colonne));
  }

  private void pushInDiscard(Card[] cards) {
    for (Card c : cards) {
      model.pushDiscardPile(c);
    }
  }

  @Override
  public void undo() {
    throw new UnsupportedOperationException("Undo n'est pas supporté pour la commande Reduce");
  }

  @Override
  public boolean isUndoable() {
    return false;
  }

  @Override
  public Command[] getFollowUpCommands() {
    return new Command[] {new EndTurnCommand(model)};
  }

  @Override
  public String toString() {
    return "Reduce (col " + colonne + ")";
  }
}
