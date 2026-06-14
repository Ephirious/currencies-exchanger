package com.ephirious.interfaces;

public interface ExceptionMapper<T extends Exception, R extends Exception> {
    public R map(T from);
}
