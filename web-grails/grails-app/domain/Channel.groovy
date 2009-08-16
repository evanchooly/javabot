class Channel {
  Long id;
  String name;
  String key;
  Date updated;
  Boolean logged;

  public def static listPublic = {
//        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
    def c = Channel.createCriteria()
    def data = c {
      eq("logged", true)
      order("name")
    }

    [results: data, total: data.size()]
  }
}
