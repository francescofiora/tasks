package it.francescofiora.tasks.taskapi.service.mapper;

import it.francescofiora.tasks.taskapi.domain.Parameter;
import it.francescofiora.tasks.taskapi.service.dto.ParameterDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Parameter} and its DTO {@link ParameterDto}.
 */
@Mapper(componentModel = "spring")
public interface ParameterMapper {

  Parameter toEntity(ParameterDto dto);
}
