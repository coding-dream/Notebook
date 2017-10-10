package application.db;

import java.util.List;

import application.bean.Result;

public interface BaseDao<T> {

    void save(T entity);

    void delete(Long id);

    void update(T entity);

    T getById(Long id);

    List<T> findAll();

    Class getEntityClass();

    Result<T> getPage(int currentPage);

    int count();
}
