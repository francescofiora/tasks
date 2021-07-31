package it.francescofiora.tasks.taskapi.service;

/**
 * Sequence Generator Service.
 */
public interface SequenceGeneratorService {

  /**
   * Generate Sequence.
   *
   * @param seqName String
   * @return long
   */
  long generateSequence(String seqName);
}
