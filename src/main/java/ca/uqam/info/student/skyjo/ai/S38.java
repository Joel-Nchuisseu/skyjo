package ca.uqam.info.student.skyjo.ai;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.CardType;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import java.util.ArrayList;

/**
 * Joueur robotique s38.
 *
 * @author Joel Stephane Tchiengang Nchuisseu (TCHJ90020608)
 */
@SuppressWarnings("checkstyle:CyclomaticComplexity")
public class S38 implements CommandSelector {
  private final SkyjoModelReadOnly model;
  private int player;
  private int columns;
  private int rows;
  private int columnUnRevealedCard;
  private int rowUnRevealedCard;
  private Card highestCardValue;
  private int rowHighestCard;
  private int columnHighestCard;

  /**
   * Constructeur du joueur robotique.
   *
   * @param model le model du jeu en accès lecture.
   */
  public S38(SkyjoModelReadOnly model) {
    this.model = model;
  }

  @Override
  public int selectCommand(Command[] commands, boolean b) {
    player = model.getCurrentPlayerIndex();
    columns = model.getCurrentDimensionsX(player);
    rows = model.getCurrentDimensionsY(player);
    findHighestCardValue();
    findFirstUnRevealed(commands);
    // end turn ou reduce
    if (commands[0].toString().equalsIgnoreCase("End turn.")
        || commands[0].toString().contains("Reduce")) {
      return 0;
    }
    commands = getCommands(commands);
    // buffer vide
    if (!model.isBufferCardPresent()) {
      Card discardCard = model.peekDiscardPile();
      // D : [-2,4]
      if (discardCard.getType() == CardType.NUMERIC_NEGATIVE
          || discardCard.getType() == CardType.NUMERIC_NEUTRAL
          || discardCard.getType() == CardType.NUMERIC_POSITIVE_LIGHT) {
        if (highestCardValue.getValue() > discardCard.getValue()) {
          // Remplacer H par D
          return findCommandReplace(commands, columnHighestCard, rowHighestCard);
        } else {
          // remplacer unrevealed
          return findCommandReplace(commands, columnUnRevealedCard, rowUnRevealedCard);
        }
        // D : [5-8]
      } else if (discardCard.getType() == CardType.NUMERIC_POSITIVE_MODERATE) {
        if (highestCardValue.getValue() > discardCard.getValue()) {
          // Remplacer H par D
          return findCommandReplace(commands, columnHighestCard, rowHighestCard);
        } else {
          // reveal decl card
          return 0;
        }
        // D : [9,12]
      } else {
        return 0;
      }
      // buffer pas vide
    } else {
      Card bufferCard = model.getBufferCard();
      // B : [-2,4]
      if (bufferCard.getType() == CardType.NUMERIC_NEGATIVE
          || bufferCard.getType() == CardType.NUMERIC_NEUTRAL
          || bufferCard.getType() == CardType.NUMERIC_POSITIVE_LIGHT) {
        if (highestCardValue.getValue() > bufferCard.getValue()) {
          // Remplacer H par D
          return findCommandReplace(commands, columnHighestCard, rowHighestCard);
        } else {
          // remplacer unrevealed
          return findCommandReplace(commands, columnUnRevealedCard, rowUnRevealedCard);
        }
        // B : [5-8]
      } else if (bufferCard.getType() == CardType.NUMERIC_POSITIVE_MODERATE) {
        if (highestCardValue.getValue() > bufferCard.getValue()) {
          // Remplacer H par D
          return findCommandReplace(commands, columnHighestCard, rowHighestCard);
        } else {
          // Rej&Rev
          return findCommandRejAndRev(commands);
        }
        // B : [9-12]
      } else {
        return findCommandRejAndRev(commands);
      }
    }
  }

  private static Command[] getCommands(Command[] commands) {
    ArrayList<Command> tmp = new ArrayList<>();
    for (Command c : commands) {
      if (!c.isSpaceholder()) {
        tmp.add(c);
      }
    }
    commands = tmp.toArray(new Command[0]);
    return commands;
  }

  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  private void findHighestCardValue() {
    // trouver la premiere carte revealed
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++) {
        if (model.isPlayerCardAtPositionRevealed(player, x, y)) {
          highestCardValue = model.getCardForPlayerByPosition(player, x, y);
          break;
        }
      }
    }
    // trouver la position de la carte avec la plus haute valeur
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++) {
        if (model.isPlayerCardAtPositionRevealed(player, x, y)
            && model.getCardForPlayerByPosition(player, x, y).getValue()
            >= highestCardValue.getValue()) {
          highestCardValue = model.getCardForPlayerByPosition(player, x, y);
          rowHighestCard = y;
          columnHighestCard = x;
        }
      }
    }
  }

  @Override
  public String getDescriptor() {
    return "s38";
  }

  private int findCommandReplace(Command[] cmds, int x, int y) {
    int indice = 0;
    String cmd = "Replace (" + x + "/" + y + ")";
    for (int i = 0; i < cmds.length; i++) {
      if (cmds[i].toString().equalsIgnoreCase(cmd)) {
        indice = i;
        break;
      }
    }
    return indice;
  }

  private int findCommandRejAndRev(Command[] cmds) {
    int indice = 0;
    for (int i = 0; i < cmds.length; i++) {
      if (cmds[i].toString().contains("Rej&Rev")) {
        indice = i;
      }
    }
    return indice;
  }

  private void findFirstUnRevealed(Command[] cmds) {
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++) {
        if (!model.isPlayerCardAtPositionRevealed(player, x, y)) {
          columnUnRevealedCard = x;
          rowUnRevealedCard = y;
          x = columns;
          y = rows;
        }
      }
    }
  }
}

