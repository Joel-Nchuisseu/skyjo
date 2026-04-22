package ca.uqam.info.student.skyjo.controller.commands;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.ModelAccessException;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;

/**
 * Commande permettant de rejeter la carte en buffer sur la pile de défausse
 * et de révéler une carte cachée du joueur à la position spécifiée.
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 **/
public class RejectAndRevealCommand implements Command {
  private final SkyjoModel model;
  private final int joueur;
  private final int ligne;
  private final int colonne;

  /**
   * Constructeur qui permet de rejeter la carte dans le buffer et révéler une
   * carte de la matrice.
   *
   * @param model   le model du jeu.
   * @param joueur  l'indice du joueur.
   * @param colonne l'indice de la colonne.
   * @param ligne   l'indice de la ligne.
   */
  public RejectAndRevealCommand(SkyjoModelReadOnly model, int joueur, int colonne, int ligne) {
    this.model = (SkyjoModel) model;
    this.joueur = joueur;
    this.colonne = colonne;
    this.ligne = ligne;
  }

  @Override
  public boolean isSpaceholder() {
    return model.isPlayerCardAtPositionRevealed(joueur, colonne, ligne);
  }

  @Override
  public void execute() {
    // mettre la carte du buffer dans la défausse
    model.pushDiscardPile(model.popBufferCard());
    // reveler une carte
    model.revealPlayerCard(joueur, colonne, ligne);
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
    return new Command[] {new EndTurnCommand(model)};
  }

  @Override
  public String toString() {
    return "Rej&Rev (" + colonne + "/" + ligne + ")";
  }
}
