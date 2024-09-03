package example.service;

import java.util.List;

public interface ModelService<T> {
    // Create
    void save(T t);

    // Read
    List<T> findAll();
    T findById(Long id);

    // Update
    void update(Long id, T t);

    // Delete
    void delete(Long id);

//    // Additional Methods
//    long count();
//    List<T> findByName(String name);
}
