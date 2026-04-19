package ca.uqam.info.student.skyjo.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import ca.uqam.info.student.skyjo.controller.commands.EndGameCommand;
import ca.uqam.info.student.skyjo.controller.commands.EndTurnCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReduceColumnCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReduceRowCommand;
import ca.uqam.info.student.skyjo.controller.commands.RejectAndRevealCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReplaceFromBufferCommand;
import ca.uqam.info.student.skyjo.controller.commands.ReplaceFromDiscardCommand;
import ca.uqam.info.student.skyjo.controller.commands.RevealDeckCardCommand;
import ca.uqam.info.student.skyjo.controller.commands.SpaceHolderCommand;
import ca.uqam.info.student.skyjo.model.CardImpl;
import ca.uqam.info.student.skyjo.model.SkyjoModelImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.plaf.IconUIResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe contenant les tests supplémentaires.
 */
public abstract class TestsSuppAbstract {
  private Controller controller;
  private Controller controller2;

  public abstract SkyjoModel getDefaultModel();

  public abstract Controller getController();

  /**
   * Initialiser le controller.
   */
  @BeforeEach
  public void initializeControllerWithDefaultModel() {
    controller = getController();
    String[] players = new String[] {"Max", "Ryan", "Maram", "Quentin"};
    controller.initializeModel(ModelPreset.DEFAULT, players, null);
  }

  /**
   * Initialiser le controller avec un model par default et un joueur.
   */
  @BeforeEach
  public void initializeControllerWithDefaultModelOnePlayer() {
    controller2 = getController();
    String[] players = new String[] {"Max"};
    controller2.initializeModel(ModelPreset.DEFAULT, players, null);
  }

  private void revealAllCardsPlayerZero(SkyjoModel model) {
    // Manually reveal all cards of current player, so game ender can be set.
    // default model has 4x3 cards
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 4; x++) {
        if (!model.isPlayerCardAtPositionRevealed(0, x, y)) {
          model.revealPlayerCard(0, x, y);
        } else {
          // If already revealed, attempting to reveal must throw an exception
          int finalX = x;
          int finalY = y;
          assertThrows(RuntimeException.class, () -> model.revealPlayerCard(0, finalX, finalY));
        }
      }
    }
  }

  private boolean egal(Card[] tab1, Card[] tab2) {
    boolean rep = true;
    for (int i = 0; i < tab1.length; i++) {
      if (!tab1[i].equals(tab2[i])) {
        rep = false;
        break;
      }
    }
    return rep;
  }
  // =========
  // TESTS CARD
  // =========

  @Test
  public void testValeurInvalide() {
    // Exception, car valeur invalide
    assertThrows(RuntimeException.class, () -> new CardImpl(13));
    assertThrows(RuntimeException.class, () -> new CardImpl(-3));
  }

  @Test
  public void testEquals() {
    assertEquals(new CardImpl(5), new CardImpl(5));
    assertNotEquals(new CardImpl(2), new CardImpl(8));
    assertThrows(RuntimeException.class, () -> new CardImpl(4).equals("allo"));
  }

  @Test
  public void testCreateModel() {
    // Exception, car tab null
    assertThrows(RuntimeException.class,
        () -> new SkyjoModelImpl(ModelPreset.DEFAULT, null, null));
    // Exception, car modelpreset est null
    assertThrows(RuntimeException.class,
        () -> new SkyjoModelImpl(null, null, null));
    // valider nb joueurs
    SkyjoModel model =
        new SkyjoModelImpl(ModelPreset.DEFAULT, new String[] {"Max", "Ryan", "Maram"}, null);
    assertEquals(3, model.getAmountPlayers());
    SkyjoModel model2 =
        new SkyjoModelImpl(ModelPreset.DEFAULT,
            new String[] {"Max", "Ryan"},
            new Random(0));
    assertEquals(2, model2.getAmountPlayers());
  }
  // ========================
  // TESTS SKYJOMODELREADONLY
  // ========================

  @Test
  public void testGetPlayerNames() {
    SkyjoModel model = getDefaultModel();
    // Exception, car joueur invalide
    assertThrows(RuntimeException.class, () -> model.getPlayerName(4));
    assertThrows(RuntimeException.class, () -> model.getPlayerName(-3));
  }

  @Test
  public void testIsCardAtPositionRevealed() {
    SkyjoModel model = getDefaultModel();
    // Exception, car position est invalide
    assertThrows(RuntimeException.class, () -> model.isPlayerCardAtPositionRevealed(0, -1, 0));
    assertThrows(RuntimeException.class, () -> model.isPlayerCardAtPositionRevealed(1, 1, 4));
    assertThrows(RuntimeException.class, () -> model.isPlayerCardAtPositionRevealed(2, 0, 4));
    assertThrows(RuntimeException.class, () -> model.isPlayerCardAtPositionRevealed(3, 6, 2));
  }

  @Test
  public void testIsAllPlayerCardsRevealed() {
    SkyjoModel model = getDefaultModel();
    // Exception, car joueur invalide
    assertThrows(RuntimeException.class, () -> model.isAllPlayerCardsRevealed(-1));
    assertThrows(RuntimeException.class, () -> model.isAllPlayerCardsRevealed(4));
  }

  @Test
  public void testGetCardPosition() {
    SkyjoModel model = getDefaultModel();
    // Exception, car indice invalide
    assertThrows(RuntimeException.class,
        () -> model.getCardForPlayerByPosition(-1, 0, 0).getValue());
    assertThrows(RuntimeException.class,
        () -> model.getCardForPlayerByPosition(4, 0, 0).getValue());
    assertThrows(RuntimeException.class,
        () -> model.getCardForPlayerByPosition(0, -1, 0).getValue());
    assertThrows(RuntimeException.class,
        () -> model.getCardForPlayerByPosition(0, 4, 0).getValue());
    assertThrows(RuntimeException.class,
        () -> model.getCardForPlayerByPosition(0, 0, -1).getValue());
    assertThrows(RuntimeException.class,
        () -> model.getCardForPlayerByPosition(0, 0, 3).getValue());
  }

  @Test
  public void testGetDimensions() {
    SkyjoModel model = getDefaultModel();
    // Exception, car indice joueur invalide
    assertThrows(RuntimeException.class, () -> model.getCurrentDimensionsX(-1));
    assertThrows(RuntimeException.class, () -> model.getCurrentDimensionsY(-1));
    assertThrows(RuntimeException.class, () -> model.getCurrentDimensionsX(4));
    assertThrows(RuntimeException.class, () -> model.getCurrentDimensionsY(4));
  }
  // ================
  // TESTS SKYJOMODEL
  // ================

  @Test
  public void testRestoreRow() {
    final SkyjoModel model = getDefaultModel();
    final SkyjoModel model2 = getDefaultModel();
    final SkyjoModel model3 = getDefaultModel();
    Card[] cartes = {new CardImpl(5),
        new CardImpl(5), new CardImpl(5), new CardImpl(5), new CardImpl(5)};
    Card[] cartes2 = {new CardImpl(5), new CardImpl(5), new CardImpl(5), new CardImpl(5)};
    // Exception, car nb cartes diffèrent des dimensions
    assertThrows(RuntimeException.class, () -> model.restoreRow(0, 0, cartes));
    assertThrows(RuntimeException.class, () -> model.restoreRow(0, 0, null));
    // Exception, car indice invalide
    assertThrows(RuntimeException.class, () -> model.restoreRow(-1, 0, cartes2));
    assertThrows(RuntimeException.class, () -> model.restoreRow(4, 0, cartes2));
    assertThrows(RuntimeException.class, () -> model.restoreRow(0, -1, cartes2));
    assertThrows(RuntimeException.class, () -> model.restoreRow(0, 3, cartes2));
    // cas 1
    // Ajouter une ligne, la matrice doit avoir 4 ligne
    model.restoreRow(0, 0, cartes2);
    assertEquals(4, model.getCurrentDimensionsY(0));
    // Valider les cartes
    assertEquals(5, model.getCardForPlayerByPosition(0, 0, 0).getValue());
    assertEquals(5, model.getCardForPlayerByPosition(0, 2, 0).getValue());
    assertEquals(12, model.getCardForPlayerByPosition(0, 0, 1).getValue());
    assertEquals(11, model.getCardForPlayerByPosition(0, 3, 3).getValue());
    // Exception, car la carte n'est pas révélée
    assertThrows(RuntimeException.class, () -> model.getCardForPlayerByPosition(0, 1, 2));
    assertThrows(RuntimeException.class, () -> model.getCardForPlayerByPosition(0, 2, 3));
    // cas2
    // Ajouter une ligne, la matrice doit avoir 4 ligne
    model2.restoreRow(0, 1, cartes2);
    assertEquals(4, model2.getCurrentDimensionsY(0));
    // valider les cartes
    assertEquals(12, model2.getCardForPlayerByPosition(0, 0, 0).getValue());
    assertEquals(5, model2.getCardForPlayerByPosition(0, 1, 1).getValue());
    assertEquals(5, model2.getCardForPlayerByPosition(0, 2, 1).getValue());
    assertEquals(11, model2.getCardForPlayerByPosition(0, 3, 3).getValue());
    // Exception, car la carte n'est pas révélée
    assertThrows(RuntimeException.class, () -> model2.getCardForPlayerByPosition(0, 3, 0));
    assertThrows(RuntimeException.class, () -> model2.getCardForPlayerByPosition(0, 1, 2));
    // cas3
    // Ajouter une ligne, la matrice doit avoir 4 ligne
    model3.restoreRow(0, 2, cartes2);
    assertEquals(4, model3.getCurrentDimensionsY(0));
    // valider les cartes
    assertEquals(12, model3.getCardForPlayerByPosition(0, 0, 0).getValue());
    assertEquals(5, model3.getCardForPlayerByPosition(0, 0, 2).getValue());
    assertEquals(5, model3.getCardForPlayerByPosition(0, 3, 2).getValue());
    assertEquals(11, model3.getCardForPlayerByPosition(0, 3, 3).getValue());
    // Exception, car la carte n'est pas révélée
    assertThrows(RuntimeException.class, () -> model3.getCardForPlayerByPosition(0, 2, 1));
    assertThrows(RuntimeException.class, () -> model3.getCardForPlayerByPosition(0, 0, 3));
  }

  @Test
  public void testRestoreColumn() {
    final SkyjoModel model = getDefaultModel();
    final SkyjoModel model2 = getDefaultModel();
    final SkyjoModel model3 = getDefaultModel();
    Card[] cartes = {new CardImpl(5), new CardImpl(5)};
    Card[] cartes2 = {new CardImpl(5), new CardImpl(5), new CardImpl(5)};
    // Exception, car nb cartes diffèrent des dimensions
    assertThrows(RuntimeException.class, () -> model.restoreColumn(0, 0, cartes));
    assertThrows(RuntimeException.class, () -> model.restoreColumn(0, 0, null));
    // Exception, car indice invalide
    assertThrows(RuntimeException.class, () -> model.restoreColumn(-1, 0, cartes2));
    assertThrows(RuntimeException.class, () -> model.restoreColumn(4, 0, cartes2));
    assertThrows(RuntimeException.class, () -> model.restoreColumn(0, -1, cartes2));
    assertThrows(RuntimeException.class, () -> model.restoreColumn(0, 4, cartes2));
    // cas 1
    // Ajouter une colonne, la matrice doit avoir 5 colonnes
    model.restoreColumn(0, 0, cartes2);
    assertEquals(5, model.getCurrentDimensionsX(0));
    // Valider les cartes
    assertEquals(12, model.getCardForPlayerByPosition(0, 1, 0).getValue());
    assertEquals(5, model.getCardForPlayerByPosition(0, 0, 0).getValue());
    assertEquals(5, model.getCardForPlayerByPosition(0, 0, 2).getValue());
    assertEquals(11, model.getCardForPlayerByPosition(0, 4, 2).getValue());
    // Exception, car la carte n'est pas révélée
    assertThrows(RuntimeException.class, () -> model.getCardForPlayerByPosition(0, 1, 1));
    assertThrows(RuntimeException.class, () -> model.getCardForPlayerByPosition(0, 4, 1));
    // cas2
    // Ajouter une colonne, la matrice doit avoir 5 colonnes
    model2.restoreColumn(0, 2, cartes2);
    assertEquals(5, model2.getCurrentDimensionsX(0));
    // valider les cartes
    assertEquals(12, model2.getCardForPlayerByPosition(0, 0, 0).getValue());
    assertEquals(5, model2.getCardForPlayerByPosition(0, 2, 1).getValue());
    assertEquals(5, model2.getCardForPlayerByPosition(0, 2, 2).getValue());
    assertEquals(11, model2.getCardForPlayerByPosition(0, 4, 2).getValue());
    // Exception, car la carte n'est pas révélée
    assertThrows(RuntimeException.class, () -> model2.getCardForPlayerByPosition(0, 3, 0));
    assertThrows(RuntimeException.class, () -> model2.getCardForPlayerByPosition(0, 3, 2));
    // cas3
    // Ajouter une colonne, la matrice doit avoir 5 colonnes
    model3.restoreColumn(0, 3, cartes2);
    assertEquals(5, model2.getCurrentDimensionsX(0));
    // valider les cartes
    assertEquals(12, model3.getCardForPlayerByPosition(0, 0, 0).getValue());
    assertEquals(5, model3.getCardForPlayerByPosition(0, 3, 0).getValue());
    assertEquals(5, model3.getCardForPlayerByPosition(0, 3, 1).getValue());
    assertEquals(11, model3.getCardForPlayerByPosition(0, 4, 2).getValue());
    // Exception, car la carte n'est pas révélée
    assertThrows(RuntimeException.class, () -> model3.getCardForPlayerByPosition(0, 2, 0));
    assertThrows(RuntimeException.class, () -> model3.getCardForPlayerByPosition(0, 2, 2));
  }

  @Test
  public void testRevealedPlayerCard() {
    SkyjoModel model = getDefaultModel();
    assertThrows(RuntimeException.class, () -> model.revealPlayerCard(-1, 0, 0));
    assertThrows(RuntimeException.class, () -> model.revealPlayerCard(4, 0, 0));
    assertThrows(RuntimeException.class, () -> model.revealPlayerCard(0, -1, 0));
    assertThrows(RuntimeException.class, () -> model.revealPlayerCard(0, 4, 0));
    assertThrows(RuntimeException.class, () -> model.revealPlayerCard(0, 0, -1));
    assertThrows(RuntimeException.class, () -> model.revealPlayerCard(0, 0, 3));
  }

  @Test
  public void testEliminateRow() {
    final SkyjoModel model = getDefaultModel();
    final SkyjoModel model2 = getDefaultModel();
    final SkyjoModel model3 = getDefaultModel();
    final SkyjoModel model4 = getDefaultModel();
    final Card[] cartes1 = {new CardImpl(12), new CardImpl(12), new CardImpl(12), new CardImpl(12)};
    final Card[] cartes2 = {new CardImpl(5), new CardImpl(5), new CardImpl(5), new CardImpl(5)};
    // Exception, car indice invalide
    assertThrows(RuntimeException.class, () -> model.eliminateRow(-1, 0));
    assertThrows(RuntimeException.class, () -> model.eliminateRow(4, 0));
    assertThrows(RuntimeException.class, () -> model.eliminateRow(0, -1));
    assertThrows(RuntimeException.class, () -> model.eliminateRow(-1, 3));
    // cas1
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model);
    // Valider le tableau des cartes retournées et nb lignes
    assertTrue(egal(model.eliminateRow(0, 0), cartes1));
    assertEquals(2, model.getCurrentDimensionsY(0));
    // Valider les cartes
    assertEquals(11, model.getCardForPlayerByPosition(0, 3, 1).getValue());
    assertEquals(12, model.getCardForPlayerByPosition(0, 0, 0).getValue());
    // cas 2
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model2);
    // Valider le tableau des cartes retournées et nb lignes
    assertTrue(egal(model2.eliminateRow(0, 1), cartes1));
    assertEquals(2, model2.getCurrentDimensionsY(0));
    // Valider les cartes
    assertEquals(11, model2.getCardForPlayerByPosition(0, 3, 1).getValue());
    assertEquals(12, model2.getCardForPlayerByPosition(0, 0, 0).getValue());
    // cas 3
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model3);
    // Exception, les cartes sur la ligne n'ont pas tous la même valeur
    assertThrows(RuntimeException.class, () -> model3.eliminateRow(0, 2));
    // cas 4
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model4);
    // ajouter une ligne
    model4.restoreRow(0, 0, cartes2);
    // Valider le tableau des cartes retournées et nb lignes
    assertTrue(egal(model4.eliminateRow(0, 0), cartes2));
    assertEquals(3, model4.getCurrentDimensionsY(0));
  }

  @Test
  public void testEliminateColumn() {
    final SkyjoModel model = getDefaultModel();
    final SkyjoModel model2 = getDefaultModel();
    final SkyjoModel model3 = getDefaultModel();
    final SkyjoModel model4 = getDefaultModel();
    final Card[] cartes1 = {new CardImpl(12), new CardImpl(12), new CardImpl(12)};
    final Card[] cartes2 = {new CardImpl(5), new CardImpl(5), new CardImpl(5)};
    // Exception, car indice invalide
    assertThrows(RuntimeException.class, () -> model.eliminateColumn(-1, 0));
    assertThrows(RuntimeException.class, () -> model.eliminateColumn(4, 0));
    assertThrows(RuntimeException.class, () -> model.eliminateColumn(0, -1));
    assertThrows(RuntimeException.class, () -> model.eliminateColumn(-1, 3));
    // cas1
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model);
    // Valider le tableau des cartes retournées et nb colonnes
    assertTrue(egal(model.eliminateColumn(0, 0), cartes1));
    assertEquals(3, model.getCurrentDimensionsX(0));
    // Valider les cartes
    assertEquals(11, model.getCardForPlayerByPosition(0, 2, 2).getValue());
    assertEquals(12, model.getCardForPlayerByPosition(0, 0, 0).getValue());
    // cas 2
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model2);
    // Valider le tableau des cartes retournées et nb colonnes
    assertTrue(egal(model2.eliminateColumn(0, 1), cartes1));
    assertEquals(3, model2.getCurrentDimensionsX(0));
    // Valider les cartes
    assertEquals(11, model.getCardForPlayerByPosition(0, 2, 2).getValue());
    assertEquals(12, model.getCardForPlayerByPosition(0, 0, 0).getValue());
    // cas 3
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model3);
    // Exception, les cartes sur la ligne n'ont pas tous la même valeur
    assertThrows(RuntimeException.class, () -> model3.eliminateColumn(0, 2));
    // cas 4
    // Retourner toutes les cartes
    revealAllCardsPlayerZero(model4);
    // ajouter une ligne
    model4.restoreColumn(0, 0, cartes2);
    // Valider le tableau des cartes retournées et nb colonnes
    assertTrue(egal(model4.eliminateColumn(0, 0), cartes2));
    assertEquals(4, model4.getCurrentDimensionsX(0));
  }

  @Test
  public void testReplaceCard() {
    SkyjoModel model = getDefaultModel();
    Card carte = new CardImpl(0);
    // Exception, car indice invalide
    assertThrows(RuntimeException.class, () -> model.replacePlayerCard(-1, 0, 0, carte));
    assertThrows(RuntimeException.class, () -> model.replacePlayerCard(4, 0, 0, carte));
    assertThrows(RuntimeException.class, () -> model.replacePlayerCard(0, -1, 0, carte));
    assertThrows(RuntimeException.class, () -> model.replacePlayerCard(0, 4, 0, carte));
    assertThrows(RuntimeException.class, () -> model.replacePlayerCard(0, 0, -1, carte));
    assertThrows(RuntimeException.class, () -> model.replacePlayerCard(0, 0, 3, carte));
    // Exception, car carte null
    assertThrows(RuntimeException.class, () -> model.replacePlayerCard(0, 0, 0, null));
    // Remplacer puis valider
    assertEquals(new CardImpl(12), model.replacePlayerCard(0, 0, 0, carte));
    assertEquals(carte, model.replacePlayerCard(0, 0, 0, new CardImpl(12)));
    assertEquals(new CardImpl(11), model.replacePlayerCard(0, 3, 2, carte));
    assertEquals(carte, model.replacePlayerCard(0, 3, 2, new CardImpl(11)));
  }

  @Test
  public void testPopDeck() {
    SkyjoModel model = getDefaultModel();
    int nbCartes = model.getAmountDeckCards();
    // Retirer toutes les cartes
    for (int i = 0; i < nbCartes; i++) {
      model.popDeck();
    }
    assertEquals(0, model.getAmountDeckCards());
    // Exception, car le deck est vide
    assertThrows(RuntimeException.class, model::popDeck);
  }

  @Test
  public void testPopDiscardPile() {
    SkyjoModel model = getDefaultModel();
    int nbCartes = model.getAmountDiscardPileCards();
    // Retirer toutes les cartes
    for (int i = 0; i < nbCartes; i++) {
      model.popDiscardPile();
    }
    assertEquals(0, model.getAmountDiscardPileCards());
    // Exception, car le deck est vide
    assertThrows(RuntimeException.class, model::popDiscardPile);
  }

  @Test
  public void testPushDiscardPile() {
    SkyjoModel model = getDefaultModel();
    assertThrows(RuntimeException.class, () -> model.pushDiscardPile(null));
  }

  @SuppressWarnings("checkstyle:MethodName")
  @Test
  public void TestStartPlayer() {
    SkyjoModel model = getDefaultModel();
    // Le joueur qui commence est le premier joueur
    assertEquals(0, model.getCurrentPlayerIndex());
  }
  // ===============
  // TEST CONTROLLER
  // ===============

  @Test
  public void testDoCommand() {
    // Exception, car indice invalide
    assertThrows(RuntimeException.class, () -> controller.doCommand(-1));
    assertThrows(RuntimeException.class, () -> controller.doCommand(-1));
  }

  @Test
  public void testUndoLastCommand() {
    /*
    controller.addModelObserver(new ModelObserver() {
      @Override
      public void refresh() {
      }
    });
    */
    // commande replace
    controller.doCommand(1);
    assertEquals(12, controller.getModel().peekDiscardPile().getValue());
    assertEquals(8, controller.getModel().getCardForPlayerByPosition(0, 0, 0).getValue());
    // La commande replace est annulable
    assertTrue(controller.isUndoAvailable());
    // Annuler la dernière commande
    controller.undoLastCommand();
    assertEquals(8, controller.getModel().peekDiscardPile().getValue());
    assertEquals(12, controller.getModel().getCardForPlayerByPosition(0, 0, 0).getValue());
    // commande revealed
    controller.doCommand(0);
    // valider la carte dans le buffer
    assertEquals(8, controller.getModel().getBufferCard().getValue());
    // la commande n'est pas annulable
    assertThrows(RuntimeException.class, () -> controller.undoLastCommand());
    // Exception, car la commande revealed n'est pas annulable
    Command revealed = new RevealDeckCardCommand(controller.getModel());
    assertThrows(RuntimeException.class, revealed::undo);
  }

  @Test
  public void testGetFollowUpCommands() {
    Command[] cmds = new RevealDeckCardCommand(controller.getModel()).getFollowUpCommands();
    // 24 commandes totales apres avoir reveler la carte du deck
    assertEquals(24, cmds.length);
    // filtrer les space holder
    ArrayList<Command> tmp = new ArrayList<>();
    Arrays.stream(cmds).filter(x -> !x.isSpaceholder()).forEach(tmp::add);
    // valider la taille
    assertEquals(22, tmp.size());
    // valider que la premiere commande soit RejectAndRevealCommand
    assertInstanceOf(RejectAndRevealCommand.class, tmp.getFirst());
    assertEquals("Rej&Rev (1/0)", tmp.getFirst().toString());
    // valider les commandes
    assertInstanceOf(RejectAndRevealCommand.class, tmp.get(9));
    assertEquals("Rej&Rev (2/2)", tmp.get(9).toString());
    assertInstanceOf(ReplaceFromBufferCommand.class, tmp.get(10));
    assertEquals("Replace (0/0)", tmp.get(10).toString());
    assertInstanceOf(ReplaceFromBufferCommand.class, tmp.get(21));
    assertEquals("Replace (3/2)", tmp.getLast().toString());
  }
  // =============
  // TEST COMMANDS
  // =============

  @Test
  public void testEndTurnCommand() {
    controller.doCommand(5);
    // essayer d'annuler la commande end turn
    assertThrows(RuntimeException.class, () -> controller.undoLastCommand());
    assertThrows(RuntimeException.class, () -> new EndTurnCommand(controller.getModel()).undo());
    assertEquals(0, new EndTurnCommand(controller.getModel()).getFollowUpCommands().length);
  }

  @Test
  public void testRejectAndRevealCommand() {
    controller.doCommand(0);
    controller.doCommand(0);
    // essayer d'annuler la commande reject and reveal
    assertThrows(RuntimeException.class, () -> controller.undoLastCommand());
    assertThrows(RuntimeException.class,
        () -> new RejectAndRevealCommand(controller.getModel(), 0, 0, 0).undo());
    assertEquals(1,
        new RejectAndRevealCommand(controller.getModel(), 0, 0, 0).getFollowUpCommands().length);
  }

  @Test
  public void testReplaceFromBufferCommand() {
    // reveal deck card
    controller.doCommand(0);
    // reject and reveal (0/1)
    controller.doCommand(0);
    assertThrows(RuntimeException.class, () -> controller.undoLastCommand());
    // end turn
    controller.doCommand(0);
    // reveal deck card
    controller.doCommand(0);
    // replace (0/0)
    controller.doCommand(10);
    controller.undoLastCommand();
    assertEquals(11, controller.getModel().getCardForPlayerByPosition(1, 0, 0).getValue());
    assertEquals(1,
        new ReplaceFromBufferCommand((SkyjoModel) controller.getModel(), 0, 0,
            0).getFollowUpCommands().length);
  }

  @Test
  public void testReplaceFromDiscardCommand() {
    // replace (0/0)
    controller.doCommand(1);
    controller.undoLastCommand();
    assertEquals(12, controller.getModel().getCardForPlayerByPosition(0, 0, 0).getValue());
    // replace (1/0)
    controller.doCommand(2);
    assertThrows(RuntimeException.class, () -> controller.undoLastCommand());
    assertEquals(1,
        new ReplaceFromDiscardCommand(controller.getModel(), 0, 0, 0).getFollowUpCommands().length);
  }

  @Test
  public void testSpaceHolder() {
    Command[] cmds = controller.getCurrentPlayerCommands();
    assertThrows(RuntimeException.class, () -> cmds[1].undo());
    assertFalse(cmds[1].isUndoable());
    assertEquals(0, new SpaceHolderCommand().getFollowUpCommands().length);
    cmds[1].execute();
  }

  @Test
  public void testReduceRowColumn() {
    Command[] options = controller2.getCurrentPlayerCommands();
    controller2.doCommand(0);
    controller2.getCurrentPlayerCommands();
    controller2.doCommand(3);
    controller2.getCurrentPlayerCommands();
    controller2.doCommand(0);
    controller2.getCurrentPlayerCommands();
    controller2.doCommand(0);
    controller2.getCurrentPlayerCommands();
    controller2.doCommand(6);
    controller2.getCurrentPlayerCommands();
    // reduce
    controller2.doCommand(0);
    controller2.getCurrentPlayerCommands();
    controller2.doCommand(0);
    assertEquals(3, controller2.getModel().getCurrentDimensionsX(0));
    assertThrows(RuntimeException.class, () ->
        new ReduceColumnCommand(controller2.getModel(), 0, 0).undo());
    assertEquals(1,
        new ReduceColumnCommand(controller2.getModel(), 0, 0).getFollowUpCommands().length);
    assertInstanceOf(EndTurnCommand.class,
        new ReduceColumnCommand(controller2.getModel(), 0, 0).getFollowUpCommands()[0]);
  }

  @Test
  public void testReduceRow() {
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    controller2.doCommand(0);
    assertEquals(2, controller2.getModel().getCurrentDimensionsY(0));
    assertThrows(RuntimeException.class, () ->
        new ReduceRowCommand(controller2.getModel(), 0, 0).undo());
    assertEquals(1,
        new ReduceRowCommand(controller2.getModel(), 0, 0).getFollowUpCommands().length);
    assertInstanceOf(EndTurnCommand.class,
        new ReduceRowCommand(controller2.getModel(), 0, 0).getFollowUpCommands()[0]);
    assertFalse(new ReduceRowCommand(controller2.getModel(), 0, 0).isUndoable());
  }

  @Test
  public void testEndGameCommand() {
    Command endGame = new EndGameCommand(controller2.getModel());
    assertThrows(RuntimeException.class, endGame::execute);
    assertFalse(endGame.isSpaceholder());
    assertThrows(RuntimeException.class, endGame::undo);
    assertFalse(endGame.isUndoable());
    assertEquals(0, endGame.getFollowUpCommands().length);
    assertEquals("", endGame.toString());

  }
}
