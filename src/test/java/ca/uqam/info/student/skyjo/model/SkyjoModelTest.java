package ca.uqam.info.student.skyjo.model;

import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.max.skyjo.model.SkyjoModelTestAbstract;

/**
 * Classe pour exécuter les tests de SkyjoModelTestAbstract.
 */
public class SkyjoModelTest extends SkyjoModelTestAbstract {
  @Override
  public SkyjoModel getDefaultModel() {
    return
        new SkyjoModelImpl(
            ModelPreset.DEFAULT,
            new String[] {"Max", "Ryan", "Maram", "Quentin"},
            null);
  }
}

