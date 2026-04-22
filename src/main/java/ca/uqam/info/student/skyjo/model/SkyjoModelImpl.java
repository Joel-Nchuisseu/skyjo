package ca.uqam.info.student.skyjo.model;

import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.ModelAccessException;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Implémentation concrète de l'interface {@link ca.uqam.info.max.skyjo.model.SkyjoModel}.
 * Gère l'état complet d'une session de jeu Skyjo : matrices de cartes des joueurs,
 * pile de pioche, pile de défausse, carte tampon et progression des tours.
 *
 * <p>Le modèle est conçu pour être manipulé exclusivement via le contrôleur,
 * conformément au patron MVC. Toute modification d'état transite par les commandes
 * définies dans le package {@code controller.commands}.</p>
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 * @see ca.uqam.info.max.skyjo.model.SkyjoModel
 **/
public class SkyjoModelImpl implements SkyjoModel {
  // ATTRIBUTS D'INSTANCES
  // matrices
  private Card[][][] cartesJoueurs;
  private boolean[][][] cartesRevelees;
  private String[] nomsJoueurs;
  private int nbColonnes;
  private int nbLignes;
  private int nbJoueurs;
  // piles
  private Stack<Card> pile;
  private Stack<Card> pileCartesRejetees;
  // carte en main
  private Card carteCourante;
  // joueur courant
  private int joueurCourant;
  private int joueurReference;
  // round
  private int compteurRound = 0;
  // joueur lançant la fin du jeu
  private Integer gameEnder;
  private int nbCoupsJoues;

  /**
   * Constructeur pour le model du jeu.
   *
   * @param modelPrest  le mode de jeu.
   * @param playerNames les noms des joueurs.
   * @param seed        la seed.
   */
  public SkyjoModelImpl(ModelPreset modelPrest, String[] playerNames, Random seed) {
    List<Card> tmp = new ArrayList<>();
    // valider les param
    validerParam(modelPrest, playerNames);
    // initialiser
    initialiser();
    // ajouter les cartes
    tmp = ordonnerCartes(tmp);
    // distribution les cartes.
    distribution(seed, tmp);
    // définir le joueur qui commence
    setStartPlayer();
  }

  private void initialiser() {
    pile = new Stack<>();
    pileCartesRejetees = new Stack<>();
    carteCourante = null;
    compteurRound++;
    joueurCourant = 0;
    gameEnder = null;
    cartesJoueurs = new Card[nbJoueurs][nbColonnes][nbLignes];
    cartesRevelees = new boolean[nbJoueurs][nbColonnes][nbLignes];
  }

  private void validerParam(ModelPreset modelPrest, String[] playerNames) {
    if (modelPrest != null) {
      nbColonnes = modelPrest.getSizeX();
      nbLignes = modelPrest.getSizeY();
    } else {
      throw new ModelAccessException("Impossible ! Le modePrest est null");
    }
    if (playerNames != null) {
      nomsJoueurs = playerNames;
      nbJoueurs = playerNames.length;
    } else {
      throw new ModelAccessException("Impossible ! playerNames est null");
    }
  }

  private List<Card> ordonnerCartes(List<Card> tmp) {
    // ajouter 5 fois -2, 10 fois -1, 15 fois 0 et 10 fois les cartes de 1 à 12 dans la pile
    ajouterCartesDansPile(tmp, 5, -2);
    ajouterCartesDansPile(tmp, 10, -1);
    ajouterCartesDansPile(tmp, 15, 0);
    for (int carte = 1; carte <= 12; carte++) {
      ajouterCartesDansPile(tmp, 10, carte);
    }
    return tmp;
  }

  private void distribution(Random seed, List<Card> tmp) {
    if (seed != null) {
      // shuffle
      Collections.shuffle(tmp, seed);
    }
    // recopier dans pile
    recopierDansPile(tmp);
    // distribution des cartes aux joueurs et retourner les cartes top left et bottom right
    distribuerCartesJoueurs(seed);
  }

  private void distribuerCartesJoueurs(Random seed) {
    // distribution des cartes aux joueurs
    for (int joueur = 0; joueur < nbJoueurs; joueur++) {
      for (int y = 0; y < nbLignes; y++) {
        for (int x = 0; x < nbColonnes; x++) {
          cartesJoueurs[joueur][x][y] = pile.pop();
        }
      }
    }
    // reveler les cartes
    revealCard(seed);
    // mettre une carte de la pile dans la pile des cartes rejetées
    pileCartesRejetees.push(pile.pop());
  }

  private void revealCard(Random seed) {
    for (int joueur = 0; joueur < nbJoueurs; joueur++) {
      if (seed == null) {
        cartesRevelees[joueur][0][0] = true;
        cartesRevelees[joueur][nbColonnes - 1][nbLignes - 1] = true;
      } else {
        revealedRandomCard(seed, joueur);
        revealedRandomCard(seed, joueur);
      }
    }
  }

  private void revealedRandomCard(Random seed, int joueur) {
    while (true) {
      int x = seed.nextInt(0, nbColonnes);
      int y = seed.nextInt(0, nbLignes);
      if (!cartesRevelees[joueur][x][y]) {
        cartesRevelees[joueur][x][y] = true;
        return;
      }
    }
  }

  private void recopierDansPile(List<Card> tmp) {
    for (Card c : tmp) {
      pile.push(c);
    }
  }

  private void ajouterCartesDansPile(List<Card> pile, int nbFois, int valeur) {
    for (int i = 0; i < nbFois; i++) {
      pile.add(new CardImpl(valeur));
    }
  }

  private void setStartPlayer() {
    int[] scores = getPlayerScores();
    int max = 0;
    int joueur = 0;
    for (int i = 0; i < scores.length; i++) {
      if (scores[i] > max) {
        max = scores[i];
        joueur = i;
      }
    }
    joueurCourant = joueur;
    joueurReference = joueur;
  }

  /**
   * Valide si la ligne est supprimable.
   *
   * @param playerIndex l'indice du joueur.
   * @param rowIndex    L'indice de la ligne.
   * @return Retourne true si la ligne est supprimable, sinon false.
   */
  public boolean ligneSupprimable(int playerIndex, int rowIndex) {
    boolean rep = true;
    if (rowIndex >= getCurrentDimensionsY(playerIndex)
        || getCurrentDimensionsX(playerIndex) == 0) {
      return false;
    } else {
      rep = estSupprimableLigne(playerIndex, rowIndex, rep);
    }
    return rep;
  }

  private boolean estSupprimableLigne(int playerIndex, int rowIndex, boolean rep) {
    rowIndexIsValid(playerIndex, rowIndex);
    Card carte = cartesJoueurs[playerIndex][0][rowIndex];
    for (int x = 0; x < getCurrentDimensionsX(playerIndex); x++) {
      if (!isPlayerCardAtPositionRevealed(playerIndex, x, rowIndex)
          || pasMemeValeur(carte, cartesJoueurs[playerIndex][x][rowIndex])) {
        rep = false;
        break;
      }
    }
    return rep;
  }

  /**
   * Verifier si la colonne peut être supprimer.
   *
   * @param playerIndex l'indice du joueur
   * @param colIndex    l'indice de la colonne
   * @return retourne true si la colonne peut être supprimée sinon false
   */
  public boolean colonneSupprimable(int playerIndex, int colIndex) {
    boolean rep = true;
    if (colIndex >= getCurrentDimensionsX(playerIndex)
        || getCurrentDimensionsY(playerIndex) == 0) {
      return false;
    } else {
      rep = estSupprimableColonne(playerIndex, colIndex);
    }
    return rep;
  }

  private boolean estSupprimableColonne(int playerIndex, int colIndex) {
    boolean rep = true;
    colIndexIsValid(playerIndex, colIndex);
    Card carte = cartesJoueurs[playerIndex][colIndex][0];
    for (int y = 0; y < getCurrentDimensionsY(playerIndex); y++) {
      if (!isPlayerCardAtPositionRevealed(playerIndex, colIndex, y)
          || pasMemeValeur(carte, cartesJoueurs[playerIndex][colIndex][y])) {
        rep = false;
        break;
      }
    }
    return rep;
  }

  private boolean pasMemeValeur(Card carte1, Card carte2) {
    return carte1.getValue() != carte2.getValue();
  }

  private boolean playerIndexIsValid(int playerIndex) {
    if (playerIndex >= 0 && playerIndex < nbJoueurs) {
      return true;
    } else {
      throw new ModelAccessException("Impossible ! L'indice du joueur est invalide.");
    }
  }

  private boolean cardPositionIsValid(int playerIndex, int colIndex, int rowIndex) {
    return playerIndexIsValid(playerIndex) && colIndexIsValid(playerIndex, colIndex)
        && rowIndexIsValid(playerIndex, rowIndex);
  }

  private boolean colIndexIsValid(int playerIndex, int colIndex) {
    if (playerIndexIsValid(playerIndex) && colIndex >= 0
        && colIndex < getCurrentDimensionsX(playerIndex)) {
      return true;
    } else {
      throw new ModelAccessException("Impossible ! L'indice de la colonne est invalide.");
    }
  }

  private boolean rowIndexIsValid(int playerIndex, int rowIndex) {
    if (playerIndexIsValid(playerIndex) && rowIndex >= 0
        && rowIndex < getCurrentDimensionsY(playerIndex)) {
      return true;
    } else {
      throw new ModelAccessException("Impossible ! L'indice de la ligne est invalide.");
    }
  }

  private void recopierLignesSuivantes(int playerIndex, int rowIndex, int nbLignes, int nbColonnes,
                                       Card[][] nouvelleGrilleCartesJoueurs,
                                       boolean[][] nouvelleGrilleCartesRelevees) {
    for (int y = rowIndex; y < nbLignes; y++) {
      for (int x = 0; x < nbColonnes; x++) {
        nouvelleGrilleCartesJoueurs[x][y + 1] = cartesJoueurs[playerIndex][x][y];
        nouvelleGrilleCartesRelevees[x][y + 1] = cartesRevelees[playerIndex][x][y];
      }
    }
  }

  private static void recopierLigne(int rowIndex, Card[] cards, int nbColonnes,
                                    Card[][] nouvelleGrilleCartesJoueurs,
                                    boolean[][] nouvelleGrilleCartesRelevees) {
    for (int x = 0; x < nbColonnes; x++) {
      nouvelleGrilleCartesJoueurs[x][rowIndex] = cards[x];
      nouvelleGrilleCartesRelevees[x][rowIndex] = true;
    }
  }

  private Card[] recopierLigne(int playerIndex, int rowIndex, int nbColonnes, Card[] cardsRemoved) {
    for (int x = 0; x < nbColonnes; x++) {
      cardsRemoved[x] = cartesJoueurs[playerIndex][x][rowIndex];
    }
    return cardsRemoved;
  }

  private void recopierLignesPrecedentes(int playerIndex, int rowIndex, int nbColonnes,
                                         Card[][] nouvelleGrilleCartesJoueurs,
                                         boolean[][] nouvelleGrilleCartesRelevees) {
    for (int y = 0; y < rowIndex; y++) {
      for (int x = 0; x < nbColonnes; x++) {
        nouvelleGrilleCartesJoueurs[x][y] = cartesJoueurs[playerIndex][x][y];
        nouvelleGrilleCartesRelevees[x][y] = cartesRevelees[playerIndex][x][y];
      }
    }
  }

  private void recopierColonnesSuivantes(int playerIndex, int colIndex, int nbColonnes,
                                         int nbLignes,
                                         Card[][] nouvelleGrilleCartesJoueurs,
                                         boolean[][] nouvelleGrilleCartesRelevees) {
    for (int x = colIndex; x < nbColonnes; x++) {
      for (int y = 0; y < nbLignes; y++) {
        nouvelleGrilleCartesJoueurs[x + 1][y] = cartesJoueurs[playerIndex][x][y];
        nouvelleGrilleCartesRelevees[x + 1][y] = cartesRevelees[playerIndex][x][y];
      }
    }
  }

  private void recopierColonne(int colIndex, Card[] cards, int nbLignes,
                               Card[][] nouvelleGrilleCartesJoueurs,
                               boolean[][] nouvelleGrilleCartesRelevees) {
    for (int y = 0; y < nbLignes; y++) {
      nouvelleGrilleCartesJoueurs[colIndex][y] = cards[y];
      nouvelleGrilleCartesRelevees[colIndex][y] = true;
    }
  }

  private Card[] recopierColonne(int playerIndex, int colIndex, int nbLignes, Card[] cardsRemoved) {
    for (int y = 0; y < nbLignes; y++) {
      cardsRemoved[y] = cartesJoueurs[playerIndex][colIndex][y];
    }
    return cardsRemoved;
  }

  private void recopierColonnesPrecedentes(int playerIndex, int colIndex, int nbLignes,
                                           Card[][] nouvelleGrilleCartesJoueurs,
                                           boolean[][] nouvelleGrilleCartesRelevees) {
    for (int x = 0; x < colIndex; x++) {
      for (int y = 0; y < nbLignes; y++) {
        nouvelleGrilleCartesJoueurs[x][y] = cartesJoueurs[playerIndex][x][y];
        nouvelleGrilleCartesRelevees[x][y] = cartesRevelees[playerIndex][x][y];
      }
    }
  }

  private void recopierLignesSuivantesE(int playerIndex, int rowIndex, int nbLignes, int nbColonnes,
                                        Card[][] nouvelleGrilleCartesJoueurs,
                                        boolean[][] nouvelleGrilleCartesRelevees) {
    for (int y = rowIndex; y < nbLignes - 1; y++) {
      for (int x = 0; x < nbColonnes; x++) {
        nouvelleGrilleCartesJoueurs[x][y] = cartesJoueurs[playerIndex][x][y + 1];
        nouvelleGrilleCartesRelevees[x][y] = cartesRevelees[playerIndex][x][y + 1];
      }
    }
  }

  private void recopierColonnesSuivantesE(int playerIndex, int colIndex, int nbColonnes,
                                          int nbLignes,
                                          Card[][] nouvelleGrilleCartesJoueurs,
                                          boolean[][] nouvelleGrilleCartesRelevees) {
    for (int x = colIndex; x < nbColonnes - 1; x++) {
      for (int y = 0; y < nbLignes; y++) {
        nouvelleGrilleCartesJoueurs[x][y] = cartesJoueurs[playerIndex][x + 1][y];
        nouvelleGrilleCartesRelevees[x][y] = cartesRevelees[playerIndex][x + 1][y];
      }
    }
  }
  // =================================
  // (ÉCRITURE) MÉTHODES DE SKYJOMODEL
  // =================================

  @Override
  public void restoreRow(int playerIndex, int rowIndex, Card[] cards) {
    int nbLignes = getCurrentDimensionsY(playerIndex);
    int nbColonnes = getCurrentDimensionsX(playerIndex);
    Card[][] nouvelleGrilleCartesJoueurs = new Card[nbColonnes][nbLignes + 1];
    boolean[][] nouvelleGrilleCartesRelevees = new boolean[nbColonnes][nbLignes + 1];
    // tester si la ligne peut être restaurée
    if (cards != null && rowIndexIsValid(playerIndex, rowIndex) && nbColonnes == cards.length) {
      // recopier les lignes précédentes à la ligne restoré
      recopierLignesPrecedentes(playerIndex, rowIndex, nbColonnes, nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      // recopier la ligne à restoré
      recopierLigne(rowIndex, cards, nbColonnes, nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      // recopier les lignes suivantes
      recopierLignesSuivantes(playerIndex, rowIndex, nbLignes, nbColonnes,
          nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      cartesJoueurs[playerIndex] = nouvelleGrilleCartesJoueurs;
      cartesRevelees[playerIndex] = nouvelleGrilleCartesRelevees;
    } else {
      throw new ModelAccessException(
          "Impossible ! Le nombres de cartes à restaurer diffères du nombre de colonne.");
    }
  }

  @Override
  public void restoreColumn(int playerIndex, int colIndex, Card[] cards) {
    int nbLignes = getCurrentDimensionsY(playerIndex);
    int nbColonnes = getCurrentDimensionsX(playerIndex);
    Card[][] nouvelleGrilleCartesJoueurs = new Card[nbColonnes + 1][nbLignes];
    boolean[][] nouvelleGrilleCartesRelevees = new boolean[nbColonnes + 1][nbLignes];
    if (cards != null && colIndexIsValid(playerIndex, colIndex) && nbLignes == cards.length) {
      // recopier les lignes précédentes à la ligne restoré
      recopierColonnesPrecedentes(playerIndex, colIndex, nbLignes, nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      // recopier la ligne à restoré
      recopierColonne(colIndex, cards, nbLignes, nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      // recopier les lignes suivantes
      recopierColonnesSuivantes(playerIndex, colIndex, nbColonnes, nbLignes,
          nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      cartesJoueurs[playerIndex] = nouvelleGrilleCartesJoueurs;
      cartesRevelees[playerIndex] = nouvelleGrilleCartesRelevees;
    } else {
      throw new ModelAccessException(
          "Impossible ! Le nombres de cartes à restaurer diffères du nombre de ligne.");
    }
  }

  @Override
  public Card[] eliminateRow(int playerIndex, int rowIndex) {
    int nbLignes = getCurrentDimensionsY(playerIndex);
    int nbColonnes = getCurrentDimensionsX(playerIndex);
    Card[][] nouvelleGrilleCartesJoueurs = new Card[nbColonnes][nbLignes - 1];
    boolean[][] nouvelleGrilleCartesRelevees = new boolean[nbColonnes][nbLignes - 1];
    Card[] cardsRemoved = new Card[getCurrentDimensionsX(playerIndex)];
    if (ligneSupprimable(playerIndex, rowIndex)) {
      // recopier les lignes précédentes à la ligne à supprimer
      recopierLignesPrecedentes(playerIndex, rowIndex, nbColonnes, nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      // recopier la ligne à supprimer dans cardsRemoved
      cardsRemoved = recopierLigne(playerIndex, rowIndex, nbColonnes, cardsRemoved);
      // recopier les lignes suivant la ligne à supprimer
      recopierLignesSuivantesE(playerIndex, rowIndex, nbLignes, nbColonnes,
          nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      cartesJoueurs[playerIndex] = nouvelleGrilleCartesJoueurs;
      cartesRevelees[playerIndex] = nouvelleGrilleCartesRelevees;
    } else {
      throw new ModelAccessException("Impossible ! La ligne n'est pas supprimable.");
    }
    return cardsRemoved;
  }

  @Override
  public Card[] eliminateColumn(int playerIndex, int colIndex) {
    int nbLignes = getCurrentDimensionsY(playerIndex);
    int nbColonnes = getCurrentDimensionsX(playerIndex);
    Card[][] nouvelleGrilleCartesJoueurs = new Card[nbColonnes - 1][nbLignes];
    boolean[][] nouvelleGrilleCartesRelevees = new boolean[nbColonnes - 1][nbLignes];
    Card[] cardsRemoved = new Card[getCurrentDimensionsY(playerIndex)];
    if (colonneSupprimable(playerIndex, colIndex)) {
      // recopier les colonnes précédentes à la colonne à supprimer
      recopierColonnesPrecedentes(playerIndex, colIndex, nbLignes, nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      // recopier la colonnes à supprimer dans cardsRemoved
      cardsRemoved = recopierColonne(playerIndex, colIndex, nbLignes, cardsRemoved);
      // recopier les colonnes suivant la colonne à supprimer
      recopierColonnesSuivantesE(playerIndex, colIndex, nbColonnes, nbLignes,
          nouvelleGrilleCartesJoueurs,
          nouvelleGrilleCartesRelevees);
      cartesJoueurs[playerIndex] = nouvelleGrilleCartesJoueurs;
      cartesRevelees[playerIndex] = nouvelleGrilleCartesRelevees;
    } else {
      throw new ModelAccessException("Impossible ! La colonne n'est pas supprimable.");
    }
    return cardsRemoved;
  }

  @Override
  public void revealPlayerCard(int playerIndex, int x, int y) {
    if (isPlayerCardAtPositionRevealed(playerIndex, x, y)) {
      throw new ModelAccessException("Impossible ! La carte est déjà révélée.");
    } else {
      cartesRevelees[playerIndex][x][y] = true;
    }
  }

  @Override
  public int[] endGame() {
    if (isGameOverInitialized()) {
      for (int joueur = 0; joueur < nbJoueurs; joueur++) {
        for (int y = 0; y < getCurrentDimensionsY(joueur); y++) {
          for (int x = 0; x < getCurrentDimensionsX(joueur); x++) {
            cartesRevelees[joueur][x][y] = true;
          }
        }
      }
    } else {
      throw new ModelAccessException("Impossible ! La fin du jeu n'a pas été lancé.");
    }
    return getPlayerScores();
  }

  @Override
  public Card replacePlayerCard(int playerIndex, int x, int y, Card card) {
    cardPositionIsValid(playerIndex, x, y);
    if (card != null) {
      Card carteRemplacee = cartesJoueurs[playerIndex][x][y];
      cartesJoueurs[playerIndex][x][y] = card;
      return carteRemplacee;
    } else {
      throw new ModelAccessException("Impossible de remplacer ! La carte est null.");
    }
  }

  @Override
  public Card popDeck() {
    if (pile.isEmpty()) {
      throw new ModelAccessException(
          "Impossible de retirer une carte de la pile ! La pile est vide.");
    } else {
      return pile.pop();
    }
  }

  @Override
  public Card popDiscardPile() {
    if (pileCartesRejetees.isEmpty()) {
      throw new ModelAccessException(
          "Impossible de retirer une carte de la pile ! La pile est vide.");
    } else {
      return pileCartesRejetees.pop();
    }
  }

  @Override
  public void pushDiscardPile(Card card) {
    if (card != null) {
      pileCartesRejetees.push(card);
    } else {
      throw new ModelAccessException(
          "Impossible d'ajouter la carte a la pile ! La carte est null.");
    }
  }

  @Override
  public void setBufferCard(Card card) {
    if (carteCourante != null) {
      throw new ModelAccessException(
          "Impossible d'ajouter une carte en main ! Il y a deja une carte.");
    } else {
      carteCourante = card;
    }
  }

  @Override
  public Card popBufferCard() {
    if (carteCourante == null) {
      throw new ModelAccessException(
          "Impossible de retirer la carte en main ! Il n'y a pas de carte.");
    } else {
      Card oldCard = carteCourante;
      carteCourante = null;
      return oldCard;
    }
  }

  @Override
  public void advancePlayer() {
    if (joueurCourant == nbJoueurs - 1) {
      joueurCourant = 0;
    } else {
      joueurCourant++;
    }
    nbCoupsJoues++;
    increaseRound();
  }

  @Override
  public void setGameEnder() {
    if (gameEnder == null) {
      if (joueurCourant == getGameEnder()) {
        gameEnder = joueurCourant;
      } else {
        throw new ModelAccessException(
            "Impossible ! Le joueur actuel n'a pas révélé toutes ses cartes.");
      }
    } else {
      throw new ModelAccessException("Impossible ! Le il  y a déjà un gamEnder.");
    }
  }
  // =======================================
  // (LECTURE) MÉTHODE DE SKYJOMODELREADONLY
  // =======================================

  @Override
  public int getAmountPlayers() {
    return nbJoueurs;
  }

  @Override
  public String getPlayerName(int playerIndex) {
    playerIndexIsValid(playerIndex);
    return nomsJoueurs[playerIndex];
  }

  @Override
  public boolean isPlayerCardAtPositionRevealed(int playerIndex, int cardPositionX,
                                                int cardPositionY) {
    cardPositionIsValid(playerIndex, cardPositionX, cardPositionY);
    return cartesRevelees[playerIndex][cardPositionX][cardPositionY];
  }

  @Override
  public boolean isAllPlayerCardsRevealed(int playerIndex) {
    boolean rep = true;
    if (playerIndexIsValid(playerIndex)) {
      for (int y = 0; y < getCurrentDimensionsY(playerIndex); y++) {
        for (int x = 0; x < getCurrentDimensionsX(playerIndex); x++) {
          if (!isPlayerCardAtPositionRevealed(playerIndex, x, y)) {
            rep = false;
            break;
          }
        }
      }
    }
    return rep;
  }

  @Override
  public Card getCardForPlayerByPosition(int playerIndex, int cardPositionX, int cardPositionY) {
    if (cardPositionIsValid(playerIndex, cardPositionX, cardPositionY)
        && cartesRevelees[playerIndex][cardPositionX][cardPositionY]) {
      return cartesJoueurs[playerIndex][cardPositionX][cardPositionY];
    } else {
      throw new ModelAccessException("Impossible ! La carte n'est pas révélée.");
    }
  }

  @Override
  public int getCurrentPlayerIndex() {
    return joueurCourant;
  }

  @Override
  public Card peekDiscardPile() {
    return pileCartesRejetees.peek();
  }

  @Override
  public Card getBufferCard() {
    if (!isBufferCardPresent()) {
      throw new ModelAccessException("Impossible ! Aucune carte en main.");
    } else {
      return carteCourante;
    }
  }

  @Override
  public boolean isBufferCardPresent() {
    return carteCourante != null;
  }

  @Override
  public int[] getPlayerScores() {
    int[] points = new int[nbJoueurs];
    for (int joueur = 0; joueur < nbJoueurs; joueur++) {
      for (int y = 0; y < getCurrentDimensionsY(joueur); y++) {
        for (int x = 0; x < getCurrentDimensionsX(joueur); x++) {
          if (cartesRevelees[joueur][x][y]) {
            points[joueur] += cartesJoueurs[joueur][x][y].getValue();
          }
        }
      }
    }
    return points;
  }

  @Override
  public int getInitialDimensionsX() {
    return nbColonnes;
  }

  @Override
  public int getInitialDimensionsY() {
    return nbLignes;
  }

  @Override
  public int getCurrentDimensionsX(int playerIndex) {
    if (cartesJoueurs[playerIndex] == null) {
      return 0;
    }
    return cartesJoueurs[playerIndex].length;
  }

  @Override
  public int getCurrentDimensionsY(int playerIndex) {
    if (cartesJoueurs[playerIndex] == null
        || cartesJoueurs[playerIndex].length == 0) {
      return 0;
    }
    return cartesJoueurs[playerIndex][0].length;
  }

  @Override
  public boolean isGameOverInitialized() {
    boolean rep = false;
    for (int joueur = 0; joueur < nbJoueurs; joueur++) {
      if (isAllPlayerCardsRevealed(joueur)) {
        rep = true;
        break;
      }
    }
    return rep;
  }

  @Override
  public boolean isGameOver() {
    boolean rep = true;
    for (int joueur = 0; joueur < nbJoueurs; joueur++) {
      if (!isAllPlayerCardsRevealed(joueur)) {
        rep = false;
        break;
      }
    }
    return rep;
  }

  @Override
  public int getGameEnder() {
    int rep = -1;
    if (isGameOverInitialized()) {
      for (int joueur = 0; joueur < nbJoueurs; joueur++) {
        if (isAllPlayerCardsRevealed(joueur)) {
          rep = joueur;
          break;
        }
      }
    }
    return rep;
  }

  @Override
  public int getAmountDiscardPileCards() {
    return pileCartesRejetees.getSize();
  }

  @Override
  public int getAmountDeckCards() {
    return pile.getSize();
  }

  @Override
  public int getRound() {
    return compteurRound;
  }

  private void increaseRound() {
    if (getCurrentPlayerIndex() == joueurReference && nbCoupsJoues == nbJoueurs) {
      compteurRound++;
      nbCoupsJoues = 0;
    }
  }
}