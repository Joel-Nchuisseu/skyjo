package ca.uqam.info.student.skyjo.view;

import ca.uqam.info.max.skyjo.controller.ModelPreset;
import ca.uqam.info.max.skyjo.model.SkyjoModelReadOnly;
import ca.uqam.info.max.skyjo.view.TextualVisualizer;
import ca.uqam.info.student.skyjo.controller.ModelFactoryImpl;

/**
 * Launcher for TP1. You may edit SkyjoModelImpl constructor, but the rest should not be touched,
 * to ensure compatibility with upcoming TPs.
 *
 * @author Maximilian Schiedermeier
 */
public class LauncherTp1 {
  /**
   * Default constructor, as imposed by javadoc.
   */
  public LauncherTp1() {
  }

  /**
   * Main method, starting up TP1 code.
   *
   * @param args not used.
   */
  public static void main(String[] args) {
    // Create a model, using your model constructor.
    // Make sure your model implements the provided model readonly interface
    String[] playerNames = new String[] {"Max", "Ryan", "Maram", "Quentin"};

    /*
     This next line requires YOUR implementation of a ModelFactory to work.
     Make sure the returned model implements the SkyjoModelReadOnly interface and is a default
     model. See handout for definition of what a default model is.
     */
    SkyjoModelReadOnly model =
        new ModelFactoryImpl().createModel(ModelPreset.DEFAULT, playerNames, null);
    // Test model printing (you do not need to implement anything for this part to work, all view
    // concerns are provided).
    // If you're on windows, please set ttyColours to false, because
    // windows does not support them reliably:
    boolean useTtyColours = false;
    TextualVisualizer visualizer = new TextualVisualizer(model, useTtyColours);
    visualizer.refresh();
  }
}
