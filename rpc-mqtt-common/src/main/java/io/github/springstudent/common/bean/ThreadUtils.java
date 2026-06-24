package io.github.springstudent.common.bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * @author ZhouNing
 * @date 2025/7/28 8:26
 **/
public class ThreadUtils {

    public static ExecutorService newBoundedThreadPool(int threadNums, ThreadFactory threadFactory) {
        int queueSize = Math.max(threadNums, 1) * 100;
        return new ThreadPoolExecutor(threadNums, threadNums, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void shutdownThreadPool(ExecutorService executor) {
        if (executor == null) {
            return;
        }
        executor.shutdown();
        int retry = 3;
        while (retry > 0) {
            retry--;
            try {
                if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    return;
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.interrupted();
            } catch (Throwable ex) {

            }
        }
        executor.shutdownNow();
    }
}
