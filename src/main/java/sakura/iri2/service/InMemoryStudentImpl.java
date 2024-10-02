package sakura.iri2.service;

import sakura.iri2.model.StudentModel;
import sakura.iri2.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;

@Service
public class InMemoryStudentImpl implements StudentService {

    private final StudentRepository studentRepository;

    public InMemoryStudentImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentModel> findAllStudent() {
        return studentRepository.findAllStudent();
    }

    @Override
    public StudentModel createStudent(StudentModel student) {
        return studentRepository.createStudent(student);
    }

    @Override
    public StudentModel updateStudent(StudentModel student) {
        return studentRepository.updateStudent(student);
    }

    @Override
    public StudentModel findStudentById(UUID id) {
        return studentRepository.findStudentById(id);
    }

    @Override
    public void deleteStudent(UUID id) {
        studentRepository.deleteStudent(id);
    }

    @Override
    public Page<StudentModel> findAllStudentsWithPagination(Pageable pageable) {

        List<StudentModel> allStudents = studentRepository.findAllStudent();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allStudents.size());
        return new PageImpl<>(allStudents.subList(start, end), pageable, allStudents.size());
    }

    @Override
    public Page<StudentModel> findAllStudentsWithPaginationAndFilters(Pageable pageable) {
        return findAllStudentsWithPagination(pageable); // Если фильтры не нужны
    }

    @Override
    public void logicDeleteStudents(List<UUID> ids) {
        for (UUID id : ids) {
            studentRepository.deleteStudent(id);
        }
    }

    @Override
    public void deleteStudents(List<UUID> ids) {
        for (UUID id : ids) {
            studentRepository.deleteStudent(id);
        }
    }

    @Override
    public Page<StudentModel> findAllStudentsWithPaginationAndFilters(Pageable pageable, String filterName, String filterEmail, Boolean isDeleted, String sortField, String sortDirection) {
        List<StudentModel> allStudents = studentRepository.findAllStudent();
        if (allStudents.isEmpty()) return new PageImpl<>(new ArrayList<>(), pageable, 0);
        if (filterName != null && !filterName.isEmpty()) {
            allStudents.removeIf(student -> !student.getName().toLowerCase().contains(filterName.toLowerCase()));
        }

        if (filterEmail != null && !filterEmail.isEmpty()) {
            allStudents.removeIf(student -> !student.getEmail().toLowerCase().contains(filterEmail.toLowerCase()));
        }

        if (isDeleted != null) {
            allStudents.removeIf(student -> student.isDeleted() != isDeleted);
        }

        if (sortField != null && sortDirection != null) {
            if (sortField.equals("name")) {
                if (sortDirection.equalsIgnoreCase("asc")) {
                    allStudents.sort(Comparator.comparing(StudentModel::getName));
                } else {
                    allStudents.sort(Comparator.comparing(StudentModel::getName).reversed());
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allStudents.size());
        return new PageImpl<>(allStudents.subList(start, end), pageable, allStudents.size());
    }
}