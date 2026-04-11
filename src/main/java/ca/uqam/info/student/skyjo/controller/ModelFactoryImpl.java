package ca.uqam.info.student.skyjo.controller;

import ca.uqam.info.max.skyjo.controller.ModelFactory;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.ModelAccessException;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.student.skyjo.model.SkyjoModelImpl;
import java.util.Random;

/**
 * Classe implémentant le ModelFactory.
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
