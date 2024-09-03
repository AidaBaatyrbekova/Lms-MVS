package example.service.impl;

import example.model.User;
import example.model.enums.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentService {
    @PersistenceContext
    private EntityManager manager;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getStudentByName(String name) {
        return manager.createQuery(
                        "SELECT s FROM User s WHERE s.role = :role AND s.firstName LIKE :name",
                        User.class)
                .setParameter("role", Role.STUDENT)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    public void update(Long id, User user) {
        User oldUser = manager.find(User.class, id);
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setRole(user.getRole());
        oldUser.setGroup(user.getGroup());
        manager.merge(oldUser);
    }

    public User findById(Long id) {
        return manager.find(User.class, id);
    }

    public List<User> findAllStudents() {
        return manager.createQuery("SELECT user FROM User user WHERE user.role = :role", User.class)
                .setParameter("role", Role.STUDENT)
                .getResultList();
    }

    public long count() {
        return manager.createQuery("SELECT COUNT(user) FROM User user WHERE user.role = :role", Long.class)
                .setParameter("role", Role.STUDENT)
                .getSingleResult();
    }
}
