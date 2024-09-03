package example.service.impl;

import example.model.Course;
import example.service.ModelService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CourseService implements ModelService<Course> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Course course) {
        entityManager.persist(course);
    }

    @Override
    public List<Course> findAll() {
        return entityManager.createQuery("from Course", Course.class).getResultList();
    }

    @Override
    public Course findById(Long id) {
        return entityManager.find(Course.class, id);
    }

    @Override
    public void update(Long id, Course course) {
        Course oldCourse = findById(id);
        if (oldCourse != null) {
            oldCourse.setCourseName(course.getCourseName());
            oldCourse.setDurationMonth(course.getDurationMonth());
            oldCourse.setCompany(course.getCompany());
            entityManager.merge(oldCourse);
        } else {
            throw new IllegalArgumentException("Course not found for ID: " + id);
        }
    }

    @Override
    public void delete(Long id) {
        Course course = findById(id);
        if (course != null) {
            entityManager.remove(course);
        } else {
            throw new IllegalArgumentException("Course not found for ID: " + id);
        }
    }
}
