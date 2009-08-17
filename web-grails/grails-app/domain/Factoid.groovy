class Factoid {
  static constraints = {
  }

  static mapping = {
    table 'factoids'
    columns {
      userName column: 'username'
    }
  }

  Long id;
  String name;
  String value;
  String userName;
  Date updated;
  Date lastUsed;
}