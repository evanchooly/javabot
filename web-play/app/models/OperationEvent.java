package models;

import com.google.code.morphia.annotations.Entity;

@Entity
public class OperationEvent extends AdminEvent {
  public String operation;

  public OperationEvent(EventType type, String operation, String requestedBy) {
    super(type, requestedBy);
    this.operation = operation;
  }
}
