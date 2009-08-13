class Log {
  Long id;
  String nick;
  String channel;
  String message;
  Date updated;
  Type type;

  static constraints = {
  }

  static mapping = {
    table 'logs'
  }

  static transients = [ "action", "kick", "serverMessage" ]

  boolean isAction() {
    message != null && Type.ACTION == type;
  }

  boolean isKick() {
    message != null && Type.KICK == type;
  }

  boolean isServerMessage() {
    message != null && Type.JOIN == type || Type.PART == type || Type.QUIT == type;
  }
}