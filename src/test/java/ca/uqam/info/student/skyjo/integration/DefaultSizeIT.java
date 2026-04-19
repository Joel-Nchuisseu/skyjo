package ca.uqam.info.student.skyjo.integration;

import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.integration.DefaultSizeAbstractIT;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import java.util.Random;

/**
 * Implémentation concrète des tests d'intégration.
 */
public class DefaultSizeIT extends DefaultSizeAbstractIT {

  @Override
  public Controller getController(ModelPreset preset, CommandSelector[] robots, Random modelSeed) {
    ControllerImpl controller = new ControllerImpl();
    String[] playerNames = new String[robots.length];
    for (int i = 0; i < playerNames.length; i++) {
      playerNames[i] = robots[i].getDescriptor();
    }
    controller.initializeModel(preset, playerNames, modelSeed);
    return controller;
  }

  @Override
  public boolean isFullTraceRequested() {
    return true;
  }

}
