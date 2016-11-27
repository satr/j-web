package io.github.satr.jweb.components.repositories;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
    List<T> getList() throws SQLException;
    T get(int id) throws SQLException;
    void save(T entity) throws SQLException;
    void save(List<T> entities) throws SQLException;
    void remove(T entity);
    void remove(List<T> entities);
}
