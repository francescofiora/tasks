package it.francescofiora.tasks.taskapi.util;

import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import org.springframework.jms.annotation.JmsListener;

public class TaskTestListener {

  @Getter
  private CountDownLatch latch = new CountDownLatch(10);

  @Getter
  private Object obj;

  @JmsListener(destination = "${activemq.queue.request:QUEUE_REQUEST}")
  public void receiveMessage(Object obj) {
    this.obj = obj;
    latch.countDown();
  }
}
