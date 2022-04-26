package it.francescofiora.tasks.taskexecutor.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.taskexecutor.domain.Parameter;
import it.francescofiora.tasks.taskexecutor.web.errors.InternalAlertException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Json to Set Converter.
 */
@Converter
public class JsonToSetConverter implements AttributeConverter<Set<Parameter>, String> {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Set<Parameter> attribute) {
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new InternalAlertException("Could not convert set to json string.", e);
    }
  }

  @Override
  public Set<Parameter> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return new HashSet<>();
    }
    try {
      return objectMapper.readValue(dbData, new TypeReference<Set<Parameter>>() {});
    } catch (IOException e) {
      throw new InternalAlertException(
          "Convert error while trying to convert string(JSON) to set data structure.", e);
    }
  }
}
