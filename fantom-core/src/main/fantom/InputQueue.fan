using concurrent

class InputQueue {
    private InStream in

    new make(InStream stream) {
        in = stream;
    }

    Str read() {
        in.readLine()
    }
}