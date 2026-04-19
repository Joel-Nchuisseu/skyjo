package ca.uqam.info.student.skyjo.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import ca.uqam.info.max.skyjo.view.TextualVisualizer;
import ca.uqam.info.student.skyjo.ai.S38;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Cette classe passe plusieurs tests intégration avec le joueur robotique s38.
 */
public class S38IT {

  /**
   * Obtenir un controller.
   *
   * @return le controller
   */
  private Controller getController() {
    return new ControllerImpl();
  }

  @Test
  public void testSeedNull() {
    int cmd;
    Controller controller = getController();
    String[] players = new String[] {"Max", "Ryan", "Maram", "Quentin"};
    controller.initializeModel(ModelPreset.DEFAULT, players, null);
    CommandSelector robot = new S38(controller.getModel());
    controller.addModelObserver(new TextualVisualizer(controller.getModel(), true));

    Command[] options = controller.getCurrentPlayerCommands();

    cmd = robot.selectCommand(options, false);
    assertEquals(1, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(10, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(10, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
  }

  @Test
  public void testSmallSeed5() {
    int cmd;
    Controller controller = getController();
    String[] players = new String[] {"Max", "Ryan"};
    controller.initializeModel(ModelPreset.SMALL, players, new Random(5));
    CommandSelector robot = new S38(controller.getModel());
    controller.addModelObserver(new TextualVisualizer(controller.getModel(), true));

    Command[] options = controller.getCurrentPlayerCommands();

    cmd = robot.selectCommand(options, false);
    assertEquals(2, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(3, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(6, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(4, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(0, cmd);
    controller.doCommand(cmd);

    options = controller.getCurrentPlayerCommands();
    cmd = robot.selectCommand(options, false);
    assertEquals(4, cmd);
    controller.doCommand(cmd);
  }

}
