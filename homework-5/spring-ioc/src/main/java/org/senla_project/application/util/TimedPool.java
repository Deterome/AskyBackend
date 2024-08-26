package org.senla_project.application.util;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimedPool<T> {

    private final Queue<Map.Entry<Timer, T>> poolMap;

    public TimedPool() {
        this.poolMap = new ConcurrentLinkedQueue<>();
    }

    public Optional<T> pop() {
        Map.Entry<Timer, T> timedConnection = poolMap.poll();
        if (timedConnection != null) {
            timedConnection.getKey().cancel();
            return Optional.of(timedConnection.getValue());
        }
        return Optional.empty();
    }

    public void push(T element, int poolTimeInMillis) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                deleteElementFromQueue(element);
            }
        }, poolTimeInMillis);
        poolMap.add(new AbstractMap.SimpleEntry<>(timer, element));
    }

    private void deleteElementFromQueue(T element) {
        poolMap.stream()
                .filter(el -> el.getValue().equals(element))
                .map(Map.Entry::getValue)
                .forEach(this::preElementDelete);
        poolMap.removeIf(el -> el.getValue().equals(element));
    }

    protected void preElementDelete(T element) {}

    public boolean isEmpty() {
        return poolMap.isEmpty();
    }

}
