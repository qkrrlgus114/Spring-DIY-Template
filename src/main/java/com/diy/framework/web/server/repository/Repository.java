package com.diy.framework.web.server.repository;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();

    void save(T entity);

    T findById(long id);

    void deleteById(long id);

    void update(T entity);

}
