package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;

/**
 * Commande permettant d'éliminer une ligne complète de la matrice d'un joueur
 * lorsque toutes ses cartes sont révélées et possèdent la même valeur numérique.
 * Les cartes éliminées sont déposées sur la pile de défausse.
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 **/
public class ReduceRowCommand implements Command {

  private final SkyjoModel model;
  private final int joueur;
  private final int ligne;

  /**
   * Constructeur de la command permettant d'éliminer une ligne.
   *
   * @param model  le modèle du jeu en accès écriture
   * @param joueur l'indice du joueur
   * @param ligne  l'indice de la ligne
   */
  public ReduceRowCommand(SkyjoModelReadOnly model, int joueur, int ligne) {
    this.model = (SkyjoModel) model;
    this.joueur = joueur;
    this.ligne = ligne;
  }

  @Override
  public boolean isSpaceholder() {
    return false;
  }

  @Override
  public void execute() {
    pushInDiscard(model.eliminateRow(joueur, ligne));
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
    return "Reduce (row " + ligne + ")";
  }
}
