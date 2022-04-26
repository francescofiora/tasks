package it.francescofiora.tasks.taskapi.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.openpojo.reflection.PojoClass;
import com.openpojo.validation.rule.Rule;
import it.francescofiora.tasks.util.DtoIdentifier;

/**
 * Dto Equals Tester Rule.
 */
public class DtoEqualsTester implements Rule {

  @Override
  public void evaluate(PojoClass pojoClass) {
    if (pojoClass.isConcrete()) {
      try {
        equalsVerifier(pojoClass.getClazz());
        if (pojoClass.extendz(DtoIdentifier.class)) {
          dtoObjectVerifier(pojoClass.getClazz());
          dtoIdentifierVerifier(pojoClass.getClazz());
        }
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }
  }

  private <T> void equalsVerifier(Class<T> clazz) throws Exception {
    var dtoObj1 = clazz.getConstructor().newInstance();
    var dtoObj2 = clazz.getConstructor().newInstance();

    // Test equals
    assertThat(dtoObj1.equals(dtoObj2)).isTrue();
    assertThat(dtoObj1.equals(dtoObj1)).isTrue();
    assertThat(dtoObj1.equals(null)).isFalse();
    assertThat(dtoObj1.equals(new Object())).isFalse();

    // Test toString
    assertThat(dtoObj1.toString()).isNotNull();

    // Test hashCode
    assertThat(dtoObj1.hashCode()).isEqualTo(dtoObj2.hashCode());
  }

  private <T> void dtoObjectVerifier(Class<T> clazz) throws Exception {
    Object dtoObj1 = TestUtils.createNewDtoIdentifier(clazz, 1L);
    Object dtoObj2 = null;
    assertThat(dtoObj1).isNotEqualTo(dtoObj2);
    assertThat(dtoObj1).isNotEqualTo(new Object());
  }

  private <T> void dtoIdentifierVerifier(Class<T> clazz) throws Exception {
    var dtoObj1 = TestUtils.createNewDtoIdentifier(clazz, 1L);
    var dtoObj2 = TestUtils.createNewDtoIdentifier(clazz, null);

    assertThat(dtoObj1).isNotEqualTo(dtoObj2);
    TestUtils.checkNotEqualHashAndToString(dtoObj2, dtoObj1);

    dtoObj2 = TestUtils.createNewDtoIdentifier(clazz, 2L);
    TestUtils.checkNotEqualHashAndToString(dtoObj1, dtoObj2);

    dtoObj2 = TestUtils.createNewDtoIdentifier(clazz, 1L);
    TestUtils.checkEqualHashAndToString(dtoObj1, dtoObj2);
  }
}
