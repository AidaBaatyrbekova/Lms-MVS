package example.service.impl;

import example.model.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import example.model.Group;
import example.model.User;
import example.model.enums.Role;
import example.service.ModelService;

import java.util.List;

@Service
@Transactional
public class UserService implements ModelService<User> {
    @PersistenceContext
    private EntityManager entityManager;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        if (findAll().isEmpty()) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.STUDENT);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole().equals(Role.STUDENT)) {
            user.setGroup(entityManager.find(Group.class, user.getGroup().getId()));
        } else {
            user.setGroup(null);
        }
        entityManager.persist(user);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(Long id, User user) {
        User oldUser = findById(id);
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole().equals(Role.STUDENT)) {
            user.setGroup(user.getGroup());
            user.setCourse(oldUser.getCourse());
        } else if (user.getRole().equals(Role.INSTRUCTOR)) {
            user.setCourse(user.getCourse());
            user.setGroup(oldUser.getGroup());
        }
        oldUser.setRole(oldUser.getRole());
        entityManager.merge(oldUser);
    }

    @Override
    public void delete(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    public User findByEmail(String email) {
        return entityManager.createQuery("select user from User user where user.email =: email", User.class)
                .setParameter("email", email).getSingleResult();
    }
}
