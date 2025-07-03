package javabot

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class JavabotThreadFactory(
    private val createDaemonThreads: Boolean,
    private val namePrefix: String,
) : ThreadFactory {
    private val threadNumber = AtomicInteger(1)

    override fun newThread(runnable: Runnable): Thread {
        val t = Thread(runnable, namePrefix + threadNumber.andIncrement)
        t.isDaemon = createDaemonThreads
        if (t.priority != Thread.NORM_PRIORITY) {
            t.priority = Thread.NORM_PRIORITY
        }
        return t
    }
}
