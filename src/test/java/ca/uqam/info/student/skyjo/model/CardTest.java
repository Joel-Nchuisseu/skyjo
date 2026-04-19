package ca.uqam.info.student.skyjo.model;

import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.CardTestAbstract;

/**
 * Classe pour exécuter les tests de CardTestAbstract.
 */
public class CardTest extends CardTestAbstract {
  @Override
  public Card getCard(int value) {
    return new CardImpl(value);
  }
}
