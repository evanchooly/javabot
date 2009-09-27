@Table="configuration"
class Config : Persistent {
    @Column
    Int id;
    @Column
    Str server := "irc.freenode.org"
    @Column
    Int port := 6667
    @Column="historylength"
    Int historyLength := 6
    @Column
    Str prefixes := "~"
    @Column
    Str nick := type.pod.name
    @Column
    Str password := type.pod.name
    @Collection="select element from configuration_operations where configuration_id=@id"
    Str[] operations := [,]
    
    new make() {
    }
    
    static Config getOrCreate() {
        Config[] list := findAll(Config#)
        return list.size != 0 ? list[0] : Config.make()
    }

    override Str toStr() {
        "Config { server: '${server}', port: ${port}, historyLength: ${historyLength}, prefixes: '${prefixes}',"
         + " nick: '${nick}', password ********, operations ${operations} }"
    }
}