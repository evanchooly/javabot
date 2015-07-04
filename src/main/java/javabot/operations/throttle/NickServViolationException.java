package javabot.operations.throttle;

public class NickServViolationException extends RuntimeException {

  public NickServViolationException(final String message) {
    super(message);
  }
}
