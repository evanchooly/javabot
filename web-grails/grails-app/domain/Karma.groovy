class Karma {
  static constraints = {
  }

  static mapping = {
    table 'karma'
    version false
    userName column:'userName'
  }

  Long id;
  String name;
  Integer value;
  String userName;
  Date updated;
}
