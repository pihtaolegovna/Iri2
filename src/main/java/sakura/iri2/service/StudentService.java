package sakura.iri2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sakura.iri2.model.StudentModel;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    List<StudentModel> findAllStudent();
    StudentModel createStudent(StudentModel student);
    StudentModel updateStudent(StudentModel student);
    StudentModel findStudentById(UUID id);
    void deleteStudent(UUID id);
    Page<StudentModel> findAllStudentsWithPagination(Pageable pageable);

    Page<StudentModel> findAllStudentsWithPaginationAndFilters(Pageable pageable);

    void logicDeleteStudents(List<UUID> ids);
    void deleteStudents(List<UUID> ids);

    Page<StudentModel> findAllStudentsWithPaginationAndFilters(Pageable pageable, String filterName, String filterEmail, Boolean isDeleted, String sortField, String sortDirection);
}