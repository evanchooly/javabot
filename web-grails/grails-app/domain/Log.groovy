class Log {
  Long id;
  String nick;
  String channel;
  String message;
  Date updated;

  enum Type {
    JOIN, PART, QUIT, ACTION, KICK, BAN, MESSAGE
  }

  static constraints = {
  }
}