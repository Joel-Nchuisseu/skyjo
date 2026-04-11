package ca.uqam.info.student.skyjo;

import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.SkyjoModel;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import ca.uqam.info.student.skyjo.model.SkyjoModelImpl;

/**
 * Classe pour exécuter les tests supplémentaires.
 */
public class TestSupp extends TestsSuppAbstract {
  @Override
  public SkyjoModel getDefaultModel() {
    return
        new SkyjoModelImpl(
            ModelPreset.DEFAULT,
            new String[] {"Max", "Ryan", "Maram", "Quentin"},
            null);
  }

  @Override
  public Controller getController() {
    return new ControllerImpl();
  }
}
