package it.francescofiora.tasks.taskapi.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.openpojo.reflection.PojoClass;
import com.openpojo.validation.rule.Rule;
import it.francescofiora.tasks.util.DtoIdentifier;
import org.junit.jupiter.api.Assertions;

public class DtoEqualsTester implements Rule {

  @Override
  public void evaluate(PojoClass pojoClass) {
    if (pojoClass.isConcrete()) {
      try {
        equalsVerifier(pojoClass.getClazz());
        if (pojoClass.extendz(DtoIdentifier.class)) {
          dtoIdentifierVerifier(pojoClass.getClazz());
        }
      } catch (Exception e) {
        Assertions.fail(e.getMessage());
      }
    }
  }

  private <T> void equalsVerifier(Class<T> clazz) throws Exception {
    T dtoObj1 = clazz.getConstructor().newInstance();
    T dtoObj2 = clazz.getConstructor().newInstance();

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

  private <T> void dtoIdentifierVerifier(Class<T> clazz) throws Exception {
    DtoIdentifier dtoObj1 = (DtoIdentifier) clazz.getConstructor().newInstance();
    dtoObj1.setId(1L);
    assertThat(dtoObj1.equals(null)).isFalse();
    assertThat(dtoObj1.equals(new Object())).isFalse();

    DtoIdentifier dtoObj2 = (DtoIdentifier) clazz.getConstructor().newInstance();
    assertThat(dtoObj1.equals(dtoObj2)).isFalse();

    dtoObj2.setId(2L);
    assertThat(dtoObj1.equals(dtoObj2)).isFalse();

    dtoObj2.setId(dtoObj1.getId());
    assertThat(dtoObj1.equals(dtoObj2)).isTrue();

    dtoObj1.setId(null);
    assertThat(dtoObj1.equals(dtoObj2)).isFalse();
  }
}