package io.github.springstudent.common.bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * @author ZhouNing
 * @date 2025/7/28 8:26
 **/
public class ThreadUtils {

    public static void shutdownThreadPool(ExecutorService executor) {
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
