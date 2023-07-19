package com.github.alwaysdarkk.enderchest.connection;

public interface ConnectionProvider<T> {

    void prepare();

    void shutdown();

    boolean isClosed();

}