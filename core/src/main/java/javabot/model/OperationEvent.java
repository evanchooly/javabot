package javabot.model;

import com.google.code.morphia.annotations.Entity;

import javabot.Javabot;

@Entity
public class OperationEvent extends AdminEvent {
  private String operation;

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  @Override
  public void add(Javabot bot) {
    bot.enableOperation(operation);
  }

  @Override
  public void delete(Javabot bot) {
    bot.disableOperation(operation);
  }
}
