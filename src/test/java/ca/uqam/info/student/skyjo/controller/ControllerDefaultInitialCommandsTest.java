package ca.uqam.info.student.skyjo.controller;

import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ControllerDefaultInitialCommandsTestAbstract;

/**
 * Classe pour exécuter les tests de ControllerDefaultInitialCommandsTestAbstract.
 */
public class ControllerDefaultInitialCommandsTest
    extends ControllerDefaultInitialCommandsTestAbstract {
  @Override
  public Controller getController() {
    return new ControllerImpl();
  }
}
