package io.github.springstudent.common.timer;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouning
 */
public abstract class ScheduleTimerTask implements TimerTask {
    /**
     * 单位毫秒
     */
    private final Integer duration;

    protected volatile boolean cancel = false;

    public ScheduleTimerTask(Integer duration) {
        this.duration = duration;
    }

    public void cancel() {
        this.cancel = true;
    }

    private void reput(Timeout timeout, Integer tick) {
        if (timeout == null || tick == null) {
            throw new IllegalArgumentException();
        }
        if (cancel) {
            return;
        }
        Timer timer = timeout.timer();
        if (timeout.isCancelled()) {
            return;
        }
        timer.newTimeout(timeout.task(), tick, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        schedule();
        reput(timeout, duration);
    }

    protected abstract void schedule();

}