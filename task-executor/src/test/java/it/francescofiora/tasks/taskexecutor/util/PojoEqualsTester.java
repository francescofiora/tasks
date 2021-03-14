package it.francescofiora.tasks.taskexecutor.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.openpojo.reflection.PojoClass;
import com.openpojo.validation.rule.Rule;
import it.francescofiora.tasks.taskexecutor.domain.DomainIdentifier;
import org.junit.jupiter.api.Assertions;

public class PojoEqualsTester implements Rule {

  @Override
  public void evaluate(PojoClass pojoClass) {
    if (pojoClass.isConcrete()) {
      try {
        equalsVerifier(pojoClass.getClazz());
        if (pojoClass.extendz(DomainIdentifier.class)) {
          domainIdentifierVerifier(pojoClass.getClazz());
        }
      } catch (Exception e) {
        Assertions.fail(e.getMessage());
      }
    }
  }

  private <T> void equalsVerifier(Class<T> clazz) throws Exception {
    T domainObj1 = clazz.getConstructor().newInstance();
    T domainObj2 = clazz.getConstructor().newInstance();

    // Test equals
    assertThat(domainObj1.equals(domainObj2)).isFalse();
    assertThat(domainObj1.equals(domainObj1)).isTrue();
    assertThat(domainObj1.equals(null)).isFalse();
    assertThat(domainObj1.equals(new Object())).isFalse();

    // Test toString
    assertThat(domainObj1.toString()).isNotNull();

    // Test hashCode
    assertThat(domainObj1.hashCode()).isEqualTo(domainObj2.hashCode());
  }

  private <T> void domainIdentifierVerifier(Class<T> clazz) throws Exception {
    DomainIdentifier domainObj1 = (DomainIdentifier) clazz.getConstructor().newInstance();
    domainObj1.setId(1L);
    assertThat(domainObj1.equals(null)).isFalse();
    assertThat(domainObj1.equals(new Object())).isFalse();

    DomainIdentifier domainObj2 = (DomainIdentifier) clazz.getConstructor().newInstance();
    assertThat(domainObj1.equals(domainObj2)).isFalse();

    domainObj2.setId(2L);
    assertThat(domainObj1.equals(domainObj2)).isFalse();

    domainObj2.setId(domainObj1.getId());
    assertThat(domainObj1.equals(domainObj2)).isTrue();

    domainObj1.setId(null);
    assertThat(domainObj1.equals(domainObj2)).isFalse();
  }
}
