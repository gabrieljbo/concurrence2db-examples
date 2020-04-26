package dev.gabrieljbo.concurrence2db;

public interface Action {

    void start(long executionInterval);

    void stop();

}
