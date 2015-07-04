package javabot.model;

import org.mongodb.morphia.annotations.Entity;

@Entity("events")
public class OperationEvent extends AdminEvent {
    private String operation;

    public OperationEvent() {
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public void add() {
        getBot().enableOperation(operation);
    }

    @Override
    public void delete() {
        getBot().disableOperation(operation);
    }
}
