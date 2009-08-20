class Change {
  static constraints = {
  }
  static mapping = {
    table 'changes'
    version false
    changeDate column:'changeDate'
  }
  
  Long id;
  String message;
  Date changeDate;
}
