package sakura.iri2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sakura.iri2.model.CourseModel;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<CourseModel> findAllCourse();
    CourseModel createCourse(CourseModel course);
    CourseModel updateCourse(CourseModel course);
    CourseModel findCourseById(UUID id);
    void deleteCourse(UUID id);
    Page<CourseModel> findAllCoursesWithPagination(Pageable pageable);

    Page<CourseModel> findAllCoursesWithPaginationAndFilters(Pageable pageable);

    void logicDeleteCourses(List<UUID> ids);
    void deleteCourses(List<UUID> ids);

    Page<CourseModel> findAllCoursesWithPaginationAndFilters(Pageable pageable, String filterName, String filterDescription, Boolean isDeleted, String sortField, String sortDirection);
}