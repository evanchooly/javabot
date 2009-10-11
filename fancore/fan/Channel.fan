
@Table="channel"
class Channel : Persistent {
    @Column
    Int? id
    @Column
    Str? name
    @Column
    Str? key
    @Column
    DateTime updated := DateTime.now
    @Column
    Bool logged := true;

    static Channel[] list() {
        findAll(Channel#)
    }

    override Str toStr() {
        return """Channel { name : ${name}, key : ${key == null ? "" : "******" }, logged : ${logged}, updated : ${updated} }"""
    }
    
    Void join(FanBoy bot) {
        if (name.startsWith("#")) {
            log.debug("Joining " + name);
            echo ("Joining " + name);
            if (key == null) {
                bot.joinChannel(name);
            } else {
                bot.joinChannel(name, key);
            }
        }
    }
}