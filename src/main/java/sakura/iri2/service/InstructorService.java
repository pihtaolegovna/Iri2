package sakura.iri2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sakura.iri2.model.InstructorModel;

import java.util.List;
import java.util.UUID;

public interface InstructorService {
    List<InstructorModel> findAllInstructor();
    InstructorModel createInstructor(InstructorModel instructor);
    InstructorModel updateInstructor(InstructorModel instructor);
    InstructorModel findInstructorById(UUID id);
    void deleteInstructor(UUID id);
    Page<InstructorModel> findAllInstructorsWithPagination(Pageable pageable);

    Page<InstructorModel> findAllInstructorsWithPaginationAndFilters(Pageable pageable);

    void logicDeleteInstructors(List<UUID> ids);
    void deleteInstructors(List<UUID> ids);

    Page<InstructorModel> findAllInstructorsWithPaginationAndFilters(Pageable pageable, String filterName, String filterEmail, Boolean isDeleted, String sortField, String sortDirection);
}