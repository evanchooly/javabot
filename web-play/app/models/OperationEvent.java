package models;

import javax.persistence.Entity;

@Entity
public class OperationEvent extends AdminEvent {
  public String operation;

  public OperationEvent(EventType type, String operation, String requestedBy) {
    super(type, requestedBy);
    this.operation = operation;
  }
}
