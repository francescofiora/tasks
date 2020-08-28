package it.francescofiora.tasks.taskapi.service;

public interface SequenceGeneratorService {

  /**
   * generate Sequence.
   * @param seqName String
   * @return long
   */
  long generateSequence(String seqName);
}
