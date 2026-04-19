package ca.uqam.info.student.skyjo.model;

import ca.uqam.info.max.skyjo.model.Card;
import ca.uqam.info.max.skyjo.model.CardType;
import ca.uqam.info.max.skyjo.model.ModelAccessException;

/**
 * Classe implémentant l'interface Card.
 */
public class CardImpl implements Card {
  // ATTRIBUTS
  private int valeur;
  private CardType type;

  /**
   * Construit une carte avec la valeur passée en paramètre.
   *
   * @param valeur la valeur de la carte.
   */
  public CardImpl(int valeur) {
    setValue(valeur);
  }

  /**
   * Retourne le type de la carte.
   *
   * @return le type.
   */
  public CardType getType() {
    return type;
  }

  /**
   * Retourne la valeur de la carte.
   *
   * @return la valeur.
   */
  public int getValue() {
    return this.valeur;
  }

  /**
   * Initialise l'attribut valeur avec la valeur passée en paramètre.
   *
   * @param valeur la valeur.
   */
  public void setValue(int valeur) {
    // lancer exception si valeur hors borne
    if (valeur < -2 || valeur > 12) {
      throw new RuntimeException("Erreur, la valeur doit être entre -2 et 12.");
    }
    this.valeur = valeur;
    if (valeur < 0) {
      this.type = CardType.NUMERIC_NEGATIVE;
    } else if (valeur == 0) {
      this.type = CardType.NUMERIC_NEUTRAL;
    } else if (valeur <= 4) {
      this.type = CardType.NUMERIC_POSITIVE_LIGHT;
    } else if (valeur <= 8) {
      this.type = CardType.NUMERIC_POSITIVE_MODERATE;
    } else {
      this.type = CardType.NUMERIC_POSITIVE_HEAVY;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Card) {
      return valeur == ((Card) o).getValue();
    } else {
      throw new ModelAccessException("Impossible ! L'objet n'est pas une carte");
    }
  }

  @Override
  public String toString() {
    if (getValue() < 0) {
      return "" + getValue();
    } else if (getValue() >= 0 && getValue() <= 9) {
      return " " + getValue();
    } else {
      return "" + getValue();
    }
  }
}
