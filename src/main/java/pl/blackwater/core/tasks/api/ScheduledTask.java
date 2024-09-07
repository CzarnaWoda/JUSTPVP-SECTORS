package pl.blackwater.core.tasks.api;

import java.util.concurrent.ScheduledExecutorService;

public abstract class ScheduledTask {

    private final ScheduledExecutorService executorService;

    public ScheduledTask(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public abstract void runTask();

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }
}
