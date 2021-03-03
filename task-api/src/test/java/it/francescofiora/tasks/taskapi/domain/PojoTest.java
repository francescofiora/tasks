package it.francescofiora.tasks.taskapi.domain;

import java.util.List;
import org.junit.jupiter.api.Test;
import com.openpojo.reflection.PojoClass;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.tasks.taskapi.util.FilterPackageInfo;
import it.francescofiora.tasks.taskapi.util.PojoEqualsTester;

public class PojoTest {
  // Configured for expectation, so we know when a class gets added or removed.
  private static final int EXPECTED_CLASS_COUNT = 4;

  // The package to test
  private static final String POJO_PACKAGE = "it.francescofiora.tasks.taskapi.domain";

  @Test
  public void ensureExpectedPojoCount() {
    List<PojoClass> pojoClasses =
        PojoClassFactory.getPojoClasses(POJO_PACKAGE, new FilterPackageInfo());
    Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, pojoClasses.size());
  }

  @Test
  public void testPojoStructureAndBehavior() {
    Validator validator = ValidatorBuilder.create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .with(new SetterTester())
        .with(new GetterTester())
        .with(new PojoEqualsTester()).build();

    validator.validate(POJO_PACKAGE, new FilterPackageInfo());
  }
}
