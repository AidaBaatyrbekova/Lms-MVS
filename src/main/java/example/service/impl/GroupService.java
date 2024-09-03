package example.service.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import example.model.Course;
import example.model.Group;
import example.service.ModelService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GroupService implements ModelService<Group> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Group group) {
        ArrayList<Group> groupList = new ArrayList<>();
        groupList.add(group);

        Course course = entityManager.find(Course.class, group.getCourseId());
        ArrayList<Course> courseList = new ArrayList<>();
        courseList.add(course);

        course.setGroups(groupList);
        group.setCourses(courseList);

        entityManager.persist(group);
    }

    @Override
    public List<Group> findAll() {
        return entityManager.createQuery("from Group", Group.class).getResultList();
    }

    @Override
    public Group findById(Long id) {
        return entityManager.find(Group.class, id);
    }

    @Override
    public void update(Long id, Group group) {
        Group oldGroup = findById(id);
        oldGroup.setGroupName(group.getGroupName());
        oldGroup.setDateOfStart(group.getDateOfStart());
        oldGroup.setDateOfFinish(group.getDateOfFinish());
        entityManager.persist(oldGroup);
    }

    @Override
    public void delete(Long id) {
        entityManager.remove(entityManager.find(Group.class, id));
    }

    public long countTotalStudents() {
        return entityManager.createQuery("SELECT COUNT(s) FROM User s", Long.class).getSingleResult();
    }

    public double countTeacher() {
        long studentCount = countTotalStudents();
        long teacherCount = entityManager.createQuery("SELECT COUNT(t) FROM User t", Long.class).getSingleResult();

        if (teacherCount == 0) {
            return 0;
        }

        return (double) studentCount / teacherCount;
    }

//    public List<User> searchStudentsByName(String groupName, String studentName) {
//        return entityManager.createQuery("SELECT s FROM User s WHERE s.group.groupName = :groupName AND s.firstName LIKE :studentName", User.class)
//                .setParameter("groupName", groupName)
//                .setParameter("studentName", "%" + studentName + "%")
//                .getResultList();
//    }
}
