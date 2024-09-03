package example.service.impl;

import example.model.Company;
import example.model.Course;
import example.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import example.service.ModelService;

import java.util.List;

@Service
@Transactional
public class CompanyService implements ModelService<Company> {

    @PersistenceContext
    private EntityManager entityManager;

    private final StudentService studentService;
    private final InstructorService instructorService;

    @Autowired
    public CompanyService(StudentService studentService, InstructorService instructorService) {
        this.studentService = studentService;
        this.instructorService = instructorService;
    }

    @Override
    public void save(Company company) {
        entityManager.persist(company);
    }

    @Override
    public List<Company> findAll() {
        return entityManager.createQuery("from Company", Company.class).getResultList();
    }

    @Override
    public Company findById(Long id) {
        return entityManager.find(Company.class, id);
    }

    @Override
    public void update(Long id, Company company) {
        Company oldCompany = findById(id);
        oldCompany.setCompanyName(company.getCompanyName());
        oldCompany.setLocatedCountry(company.getLocatedCountry());
        entityManager.persist(oldCompany);
    }

    @Override
    public void delete(Long id) {
        Company company = findById(id);
        entityManager.remove(company);
    }

    public List<Course> getCoursesByCompany(Long companyId) {
        return entityManager.createQuery("select c from Course c join Company com on c.company.id = com.id where com.id = :companyId", Course.class)
                .setParameter("companyId", companyId).getResultList();
    }

    public long countTotalStudents() {
        return studentService.count();
    }

    public double calculateStudentsPerTeacher() {
        long studentCount = studentService.count();
        long instructorCount = instructorService.count();

        if (instructorCount == 0) {
            return 0;
        }

        return (double) studentCount / instructorCount;
    }

    public List<User> searchStudentsByName(String groupName, String studentName) {
        return studentService.getStudentByName(studentName).stream()
                .filter(student -> student.getGroup().getGroupName().equals(groupName))
                .toList();
    }
}
