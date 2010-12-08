using concurrent

class OutputQueue {
    private OutStream? out

    new make(OutStream stream) {
        out = stream
    }

    Void send(Str line) {
        out.printLine(line)
    }
}