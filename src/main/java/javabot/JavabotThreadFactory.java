package javabot;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class JavabotThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final boolean createDaemonThreads;

    public JavabotThreadFactory(final boolean daemon, final String name) {
        createDaemonThreads = daemon;
        namePrefix = name;
    }

    public Thread newThread(final Runnable runnable) {
        final Thread t = new Thread(runnable, namePrefix + threadNumber.getAndIncrement());
        t.setDaemon(createDaemonThreads);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
