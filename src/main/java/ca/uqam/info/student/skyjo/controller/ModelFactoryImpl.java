package ca.uqam.info.student.skyjo.controller;

import ca.uqam.info.max.skyjo.controller.ModelFactory;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.ModelAccessException;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.student.skyjo.model.SkyjoModelImpl;
import java.util.Random;

/**
 * Implémentation concrète de l'interface {@link ca.uqam.info.max.skyjo.controller.ModelFactory}.
 * Responsable de l'instanciation du model du jeu Skyjo.
 *
 * @author Joël Stéphane Tchiengang Nchuisseu
 * @author Hasmik Tadevosyan
 * @see ca.uqam.info.max.skyjo.controller.ModelFactory
 */
public class ModelFactoryImpl implements ModelFactory {
  /**
   * Constructeur par default.
   */
  public ModelFactoryImpl() {
  }

  @Override
  public SkyjoModel createModel(ModelPreset modelPreset, String[] playerNames, Random seed) {
    return new SkyjoModelImpl(modelPreset, playerNames, seed);
  }
}
