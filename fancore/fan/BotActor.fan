

class BotActor : Actor {
    const FanBoy bot

    new make() : super(ActorPool.make()) {
        bot = FanBoy.make(this)
    }

    override Obj? receive(Obj? msg, Context cx) {
        msg->increment
    }

    static Void main() {
//        a := BotActor(ActorPool()) | Channel chan -> Void | {
//            joinChannel(chan.name + (chan.key != null ? " ${chan.key}" : ""))
//        }
    }
}