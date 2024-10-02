package sakura.iri2.service;

import sakura.iri2.model.InstructorModel;
import sakura.iri2.repository.InstructorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;

@Service
public class InMemoryInstructorImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    public InMemoryInstructorImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<InstructorModel> findAllInstructor() {
        return instructorRepository.findAllInstructor();
    }

    @Override
    public InstructorModel createInstructor(InstructorModel instructor) {
        return instructorRepository.createInstructor(instructor);
    }

    @Override
    public InstructorModel updateInstructor(InstructorModel instructor) {
        return instructorRepository.updateInstructor(instructor);
    }

    @Override
    public InstructorModel findInstructorById(UUID id) {
        return instructorRepository.findInstructorById(id);
    }

    @Override
    public void deleteInstructor(UUID id) {
        instructorRepository.deleteInstructor(id);
    }

    @Override
    public Page<InstructorModel> findAllInstructorsWithPagination(Pageable pageable) {

        List<InstructorModel> allInstructors = instructorRepository.findAllInstructor();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allInstructors.size());
        return new PageImpl<>(allInstructors.subList(start, end), pageable, allInstructors.size());
    }

    @Override
    public Page<InstructorModel> findAllInstructorsWithPaginationAndFilters(Pageable pageable) {
        return findAllInstructorsWithPagination(pageable); // Если фильтры не нужны
    }

    @Override
    public void logicDeleteInstructors(List<UUID> ids) {
        for (UUID id : ids) {
            instructorRepository.deleteInstructor(id);
        }
    }

    @Override
    public void deleteInstructors(List<UUID> ids) {
        for (UUID id : ids) {
            instructorRepository.deleteInstructor(id);
        }
    }

    @Override
    public Page<InstructorModel> findAllInstructorsWithPaginationAndFilters(Pageable pageable, String filterName, String filterEmail, Boolean isDeleted, String sortField, String sortDirection) {
        List<InstructorModel> allInstructors = instructorRepository.findAllInstructor();
        if (allInstructors.isEmpty()) return new PageImpl<>(new ArrayList<>(), pageable, 0);
        if (filterName != null && !filterName.isEmpty()) {
            allInstructors.removeIf(instructor -> !instructor.getName().toLowerCase().contains(filterName.toLowerCase()));
        }

        if (filterEmail != null && !filterEmail.isEmpty()) {
            allInstructors.removeIf(instructor -> !instructor.getEmail().toLowerCase().contains(filterEmail.toLowerCase()));
        }

        if (isDeleted != null) {
            allInstructors.removeIf(instructor -> instructor.isDeleted() != isDeleted);
        }

        if (sortField != null && sortDirection != null) {
            if (sortField.equals("name")) {
                if (sortDirection.equalsIgnoreCase("asc")) {
                    allInstructors.sort(Comparator.comparing(InstructorModel::getName));
                } else {
                    allInstructors.sort(Comparator.comparing(InstructorModel::getName).reversed());
                }
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allInstructors.size());
        return new PageImpl<>(allInstructors.subList(start, end), pageable, allInstructors.size());
    }
}