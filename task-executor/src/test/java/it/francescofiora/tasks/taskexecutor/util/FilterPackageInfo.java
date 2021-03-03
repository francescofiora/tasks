package it.francescofiora.tasks.taskexecutor.util;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.java.Java;

public class FilterPackageInfo implements PojoClassFilter {

  public boolean include(final PojoClass pojoClass) {
    return !pojoClass.getName().endsWith(Java.PACKAGE_DELIMITER + Java.PACKAGE_INFO)
        && !pojoClass.getName().endsWith("Test");
  }

  @Override
  public boolean equals(Object o) {
    return this == o || !(o == null || getClass() != o.getClass());
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }
  
}
