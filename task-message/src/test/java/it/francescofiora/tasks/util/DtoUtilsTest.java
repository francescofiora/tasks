package it.francescofiora.tasks.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DtoUtilsTest {

  static class DummyDto implements DtoIdentifier {

    private Long id;

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public void setId(Long id) {
      this.id = id;
    }
  }

  @Test
  public void equalsVerifier() {
    DummyDto dtoObj1 = new DummyDto();
    DummyDto dtoObj2 = new DummyDto();

    // Test equals
    assertThat(DtoUtils.equals(dtoObj1, dtoObj2)).isTrue();
    assertThat(DtoUtils.equals(dtoObj1, dtoObj1)).isTrue();
    assertThat(DtoUtils.equals(dtoObj1, null)).isFalse();
    assertThat(DtoUtils.equals(dtoObj1, new Object())).isFalse();
  }

  @Test
  public void dtoIdentifierVerifier() {
    DtoIdentifier domainObj1 = new DummyDto();
    domainObj1.setId(1L);
    assertThat(DtoUtils.equals(domainObj1, null)).isFalse();
    assertThat(DtoUtils.equals(domainObj1, new Object())).isFalse();

    DtoIdentifier domainObj2 = new DummyDto();
    assertThat(DtoUtils.equals(domainObj1, domainObj2)).isFalse();

    domainObj2.setId(2L);
    assertThat(DtoUtils.equals(domainObj1, domainObj2)).isFalse();

    domainObj2.setId(domainObj1.getId());
    assertThat(DtoUtils.equals(domainObj1, domainObj2)).isTrue();

    domainObj1.setId(null);
    assertThat(DtoUtils.equals(domainObj1, domainObj2)).isFalse();
  }
}
