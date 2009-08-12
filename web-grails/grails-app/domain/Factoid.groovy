class Factoid {
  static constraints = {
  }

  static mapping = {
    table 'factoids'
  }

  Long id;
  String name;
  String value;
  String userName;
  Date updated;
  Date lastUsed;
}