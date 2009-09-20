class Config : Persistent {
    static Config getOrCreate() {
        Config[] list := findAll()
        Config? config := list.size != 0 ? list[0] : null

        return config
    }
}