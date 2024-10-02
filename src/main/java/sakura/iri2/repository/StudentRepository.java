package sakura.iri2.repository;

import sakura.iri2.model.StudentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class StudentRepository {

    private final List<StudentModel> STUDENTS = new ArrayList<>();

    public Page<StudentModel> findAllWithPaginationAndFilters(
            Pageable pageable, String name, String email, Boolean isDeleted, String sortField, String sortDirection) {

        var filteredStudents = STUDENTS.stream()
                .filter(student -> (name == null || student.getName().contains(name)) &&
                        (email == null || student.getEmail().contains(email)) &&
                        (isDeleted == null || student.isDeleted() == isDeleted))
                .sorted((s1, s2) -> {
                    if (sortField.equals("name")) {
                        return sortDirection.equals("asc") ? s1.getName().compareTo(s2.getName()) : s2.getName().compareTo(s1.getName());
                    } else if (sortField.equals("email")) {
                        return sortDirection.equals("asc") ? s1.getEmail().compareTo(s2.getEmail()) : s2.getEmail().compareTo(s1.getEmail());
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), filteredStudents.size());
        int end = Math.min((start + pageable.getPageSize()), filteredStudents.size());

        return new PageImpl<>(filteredStudents.subList(start, end), pageable, filteredStudents.size());
    }

    public StudentModel findStudentById(UUID id) {
        return STUDENTS.stream()
                .filter(student -> student.getId().equals(id) && !student.isDeleted())
                .findFirst()
                .orElse(null);
    }

    public StudentModel createStudent(StudentModel student) {
        STUDENTS.add(student);
        return student;
    }

    public StudentModel updateStudent(StudentModel student) {
        var studentIndex = IntStream.range(0, STUDENTS.size())
                .filter(index -> STUDENTS.get(index).getId().equals(student.getId()))
                .findFirst()
                .orElse(-1);
        if (studentIndex == -1) {
            return null;
        }
        STUDENTS.set(studentIndex, student);
        return student;
    }

    public void logicDeleteStudents(List<UUID> ids) {
        ids.forEach(id -> {
            var student = findStudentById(id);
            if (student != null) {
                student.setDeleted(true);
            }
        });
    }

    public void deleteStudents(List<UUID> ids) {
        STUDENTS.removeIf(student -> ids.contains(student.getId()));
    }

    public List<StudentModel> findAllStudent() {
        return new ArrayList<>(STUDENTS);
    }

    public void deleteStudent(UUID id) {
        return;
    }
}