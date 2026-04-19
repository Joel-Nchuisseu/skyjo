package ca.uqam.info.student.skyjo.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import ca.uqam.info.max.skyjo.ai.Keksli;
import ca.uqam.info.max.skyjo.ai.MadMax;
import ca.uqam.info.max.skyjo.controller.Command;
import ca.uqam.info.max.skyjo.controller.Controller;
import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.view.CommandSelector;
import ca.uqam.info.max.skyjo.view.TextualVisualizer;
import ca.uqam.info.student.skyjo.ai.S38;
import ca.uqam.info.student.skyjo.controller.ControllerImpl;
import ca.uqam.info.student.skyjo.view.Launcher;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Cette classe permet de jouer plusieurs parties avec des joueurs robotiques.
 */
public class FullGameIT {
  private Controller controller;
  private Controller controller2;
  private Controller controller3;



  @Test
  public void fullGame() {
    String[] args = {"5", "s38", "s38", "keksli", "s38"};
    Launcher.main(args);
    int[] pointsJeu = Launcher.controller.getModel().getPlayerScores();
    int[] pointsAttendus = {53, 54, 62, 24};
    assertEquals(pointsAttendus[0], pointsJeu[0]);
    assertEquals(pointsAttendus[1], pointsJeu[1]);
    assertEquals(pointsAttendus[2], pointsJeu[2]);
    assertEquals(pointsAttendus[3], pointsJeu[3]);
  }

  @Test
  public void fullGame1() {
    String[] args = {"130", "s38", "s38", "s38", "s38"};
    Launcher.main(args);
    int[] pointsJeu = Launcher.controller.getModel().getPlayerScores();
    int[] pointsAttendus = {31, 13, 2, 31};
    assertEquals(pointsAttendus[0], pointsJeu[0]);
    assertEquals(pointsAttendus[1], pointsJeu[1]);
    assertEquals(pointsAttendus[2], pointsJeu[2]);
    assertEquals(pointsAttendus[3], pointsJeu[3]);
  }

  @Test
  public void fullGame2() {
    String[] args = {"15", "s38", "s38", "s38", "s38"};
    Launcher.main(args);
    int[] pointsJeu = Launcher.controller.getModel().getPlayerScores();
    int[] pointsAttendus = {12, 24, 29, 17};
    assertEquals(pointsAttendus[0], pointsJeu[0]);
    assertEquals(pointsAttendus[1], pointsJeu[1]);
    assertEquals(pointsAttendus[2], pointsJeu[2]);
    assertEquals(pointsAttendus[3], pointsJeu[3]);
  }

  @Test
  public void fullGame3() {
    String[] args = {"45", "s38", "s38", "s38", "s38"};
    Launcher.main(args);
    int[] pointsJeu = Launcher.controller.getModel().getPlayerScores();
    int[] pointsAttendus = {11, 12, 21, 24};
    assertEquals(pointsAttendus[0], pointsJeu[0]);
    assertEquals(pointsAttendus[1], pointsJeu[1]);
    assertEquals(pointsAttendus[2], pointsJeu[2]);
    assertEquals(pointsAttendus[3], pointsJeu[3]);
  }

  @Test
  public void fullGame4() {
    String[] args = {"73", "s38", "s38", "s38", "s38"};
    Launcher.main(args);
    int[] pointsJeu = Launcher.controller.getModel().getPlayerScores();
    int[] pointsAttendus = {12, 17, 17, 51};
    assertEquals(pointsAttendus[0], pointsJeu[0]);
    assertEquals(pointsAttendus[1], pointsJeu[1]);
    assertEquals(pointsAttendus[2], pointsJeu[2]);
    assertEquals(pointsAttendus[3], pointsJeu[3]);
  }

  @Test
  public void fullGame5() {
    String[] args = {"99", "s38", "s38", "s38", "s38"};
    Launcher.main(args);
    int[] pointsJeu = Launcher.controller.getModel().getPlayerScores();
    int[] pointsAttendus = {2, 29, 26, 23};
    assertEquals(pointsAttendus[0], pointsJeu[0]);
    assertEquals(pointsAttendus[1], pointsJeu[1]);
    assertEquals(pointsAttendus[2], pointsJeu[2]);
    assertEquals(pointsAttendus[3], pointsJeu[3]);
  }

  @Test
  public void fullGame6() {
    String[] args = {"105", "s38", "s38", "s38", "s38"};
    Launcher.main(args);
    int[] pointsJeu = Launcher.controller.getModel().getPlayerScores();
    int[] pointsAttendus = {13, 15, 35, 7};
    assertEquals(pointsAttendus[0], pointsJeu[0]);
    assertEquals(pointsAttendus[1], pointsJeu[1]);
    assertEquals(pointsAttendus[2], pointsJeu[2]);
    assertEquals(pointsAttendus[3], pointsJeu[3]);
  }
}


