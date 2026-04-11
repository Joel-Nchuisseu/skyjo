package ca.uqam.info.student.skyjo;

import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ControllerDefaultInitialCommandsTestAbstract;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;

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
