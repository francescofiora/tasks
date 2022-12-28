package it.francescofiora.tasks.taskapi.config.parameter;

import static org.assertj.core.api.Assertions.assertThat;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.tasks.taskapi.util.FilterPackageInfo;
import org.junit.jupiter.api.Test;

class PropertiesTest {
  // Configured for expectation, so we know when a class gets added or removed.
  private static final int EXPECTED_CLASS_COUNT = 3;

  // The package to test
  private static final String PROPERTIES_PACKAGE = PropertiesTest.class.getPackage().getName();

  @Test
  void ensureExpectedCount() {
    var classes = PojoClassFactory.getPojoClasses(PROPERTIES_PACKAGE, new FilterPackageInfo());
    assertThat(classes.size()).isEqualTo(EXPECTED_CLASS_COUNT);
  }

  @Test
  void testStructureAndBehavior() {
    // @formatter:off
    var validator = ValidatorBuilder.create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
            .with(new SetterTester())
            .with(new GetterTester())
            .build();
    // @formatter:on

    validator.validate(PROPERTIES_PACKAGE, new FilterPackageInfo());
  }
}
