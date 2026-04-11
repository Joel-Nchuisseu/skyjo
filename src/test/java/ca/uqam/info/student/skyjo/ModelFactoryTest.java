package ca.uqam.info.student.skyjo;

import ca.uqam.info.max.skyjo.controller.ModelFactory;
import ca.uqam.info.max.skyjo.controller.ModelFactoryTestAbstract;
import ca.uqam.info.student.skyjo.controller.ModelFactoryImpl;

/**
 * Classe pour exécuter les tests de ModelFactoryTestAbstract.
 */
public class ModelFactoryTest extends ModelFactoryTestAbstract {
  @Override
  public ModelFactory getModelFactory() {
    return new ModelFactoryImpl();
  }
}

