package com.hxxzt.recruitment.common.manager;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
