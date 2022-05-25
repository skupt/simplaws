package com.example.simplaws.dao;

import com.example.simplaws.entities.Human;

import java.util.List;
import java.util.Optional;

public interface DynamoDao<ID, T> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    void deleteById(ID id);

}
