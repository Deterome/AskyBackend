package org.senla_project.application.util;

import lombok.NonNull;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimedPool<T> {

    final private ScheduledExecutorService scheduler;
    final protected Queue<Map.Entry<ScheduledFuture<?>, T>> poolMap;

    public TimedPool() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.poolMap = new LinkedList<>();
    }

    synchronized public Optional<T> pop() {
        Map.Entry<ScheduledFuture<?>, T> timedConnection = poolMap.poll();
        if (timedConnection != null) {
            timedConnection.getKey().cancel(true);
            return Optional.of(timedConnection.getValue());
        }
        return Optional.empty();
    }

    synchronized public void push(T element, int poolTime, TimeUnit timeUnit) {
        poolMap.add(new AbstractMap.SimpleEntry<>(
                scheduler.schedule(() -> deleteElementFromQueue(element), poolTime, timeUnit),
                element));
    }

    synchronized private void deleteElementFromQueue(T element) {
        AtomicBoolean found = new AtomicBoolean(false);
        poolMap.removeIf(el -> {
            if (el.getValue().equals(element) && found.compareAndSet(false, true)) {
                preElementDelete(element);
                return true;
            }
            return false;
        });
    }

    protected void preElementDelete(@NonNull T element) {}

    public boolean isEmpty() {
        return poolMap.isEmpty();
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }

}
