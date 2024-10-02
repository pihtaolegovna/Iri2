package sakura.iri2.repository;

import sakura.iri2.model.InstructorModel;
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
public class InstructorRepository {

    private final List<InstructorModel> INSTRUCTORS = new ArrayList<>();

    public Page<InstructorModel> findAllWithPaginationAndFilters(
            Pageable pageable, String name, String email, Boolean isDeleted, String sortField, String sortDirection) {

        var filteredInstructors = INSTRUCTORS.stream()
                .filter(instructor -> (name == null || instructor.getName().contains(name)) &&
                        (email == null || instructor.getEmail().contains(email)) &&
                        (isDeleted == null || instructor.isDeleted() == isDeleted))
                .sorted((s1, s2) -> {
                    if (sortField.equals("name")) {
                        return sortDirection.equals("asc") ? s1.getName().compareTo(s2.getName()) : s2.getName().compareTo(s1.getName());
                    } else if (sortField.equals("email")) {
                        return sortDirection.equals("asc") ? s1.getEmail().compareTo(s2.getEmail()) : s2.getEmail().compareTo(s1.getEmail());
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), filteredInstructors.size());
        int end = Math.min((start + pageable.getPageSize()), filteredInstructors.size());

        return new PageImpl<>(filteredInstructors.subList(start, end), pageable, filteredInstructors.size());
    }

    public InstructorModel findInstructorById(UUID id) {
        return INSTRUCTORS.stream()
                .filter(instructor -> instructor.getId().equals(id) && !instructor.isDeleted())
                .findFirst()
                .orElse(null);
    }

    public InstructorModel createInstructor(InstructorModel instructor) {
        INSTRUCTORS.add(instructor);
        return instructor;
    }

    public InstructorModel updateInstructor(InstructorModel instructor) {
        var instructorIndex = IntStream.range(0, INSTRUCTORS.size())
                .filter(index -> INSTRUCTORS.get(index).getId().equals(instructor.getId()))
                .findFirst()
                .orElse(-1);
        if (instructorIndex == -1) {
            return null;
        }
        INSTRUCTORS.set(instructorIndex, instructor);
        return instructor;
    }

    public void logicDeleteInstructors(List<UUID> ids) {
        ids.forEach(id -> {
            var instructor = findInstructorById(id);
            if (instructor != null) {
                instructor.setDeleted(true);
            }
        });
    }

    public void deleteInstructors(List<UUID> ids) {
        INSTRUCTORS.removeIf(instructor -> ids.contains(instructor.getId()));
    }

    public List<InstructorModel> findAllInstructor() {
        return new ArrayList<>(INSTRUCTORS);
    }

    public void deleteInstructor(UUID id) {
        return;
    }
}