package example.service.impl;

import example.model.Course;
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
public class InstructorService {
    @PersistenceContext
    private EntityManager manager;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public InstructorService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        Course course = manager.find(Course.class, user.getCourse().getId());
        user.setCourse(course);
        user.setRole(Role.INSTRUCTOR);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        manager.persist(user);
    }

    public void update(Long id, User user) {
        User oldUser = manager.find(User.class, id);
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setRole(user.getRole());
        oldUser.setCourse(user.getCourse());
        manager.merge(oldUser);
    }

    public List<User> findAllInstructors() {
        return manager.createQuery("select user from User user where user.role = 'INSTRUCTOR'", User.class).getResultList();
    }

    public List<User> searchStudentsByName(String groupName, String studentName) {
        return manager.createQuery(
                        "SELECT s FROM User s WHERE s.role = 'STUDENT' AND s.group.groupName = :groupName AND s.firstName LIKE :studentName",
                        User.class)
                .setParameter("groupName", groupName)
                .setParameter("studentName", "%" + studentName + "%")
                .getResultList();
    }

    public long count() {
        return manager.createQuery("SELECT COUNT(user) FROM User user WHERE user.role = :role", Long.class)
                .setParameter("role", Role.INSTRUCTOR)
                .getSingleResult();
    }
}
