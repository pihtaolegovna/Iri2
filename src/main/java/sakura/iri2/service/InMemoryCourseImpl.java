package sakura.iri2.service;

import sakura.iri2.model.CourseModel;
import sakura.iri2.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;

@Service
public class InMemoryCourseImpl implements CourseService {

    private final CourseRepository courseRepository;

    public InMemoryCourseImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseModel> findAllCourse() {
        return courseRepository.findAllCourse();
    }

    @Override
    public CourseModel createCourse(CourseModel course) {
        return courseRepository.createCourse(course);
    }

    @Override
    public CourseModel updateCourse(CourseModel course) {
        return courseRepository.updateCourse(course);
    }

    @Override
    public CourseModel findCourseById(UUID id) {
        return courseRepository.findCourseById(id);
    }

    @Override
    public void deleteCourse(UUID id) {
        courseRepository.deleteCourse(id);
    }

    @Override
    public Page<CourseModel> findAllCoursesWithPagination(Pageable pageable) {

        List<CourseModel> allCourses = courseRepository.findAllCourse();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allCourses.size());
        return new PageImpl<>(allCourses.subList(start, end), pageable, allCourses.size());
    }

    @Override
    public Page<CourseModel> findAllCoursesWithPaginationAndFilters(Pageable pageable) {
        return findAllCoursesWithPagination(pageable);
    }

    @Override
    public void logicDeleteCourses(List<UUID> ids) {
        for (UUID id : ids) {
            courseRepository.deleteCourse(id);
        }
    }

    @Override
    public void deleteCourses(List<UUID> ids) {
        for (UUID id : ids) {
            courseRepository.deleteCourse(id);
        }
    }

    @Override
    public Page<CourseModel> findAllCoursesWithPaginationAndFilters(Pageable pageable, String filterName, String filterDescription, Boolean isDeleted, String sortField, String sortDirection) {
        List<CourseModel> allCourses = courseRepository.findAllCourse();
        if (allCourses.isEmpty()) return new PageImpl<>(new ArrayList<>(), pageable, 0);
        if (filterName != null && !filterName.isEmpty()) {
            allCourses.removeIf(course -> !course.getName().toLowerCase().contains(filterName.toLowerCase()));
        }

        if (filterDescription != null && !filterDescription.isEmpty()) {
            allCourses.removeIf(course -> !course.getDescription().toLowerCase().contains(filterDescription.toLowerCase()));
        }

        if (isDeleted != null) {
            allCourses.removeIf(course -> course.isDeleted() != isDeleted);
        }

        if (sortField != null && sortDirection != null) {
            if (sortField.equals("name")) {
                if (sortDirection.equalsIgnoreCase("asc")) {
                    allCourses.sort(Comparator.comparing(CourseModel::getName));
                } else {
                    allCourses.sort(Comparator.comparing(CourseModel::getName).reversed());
                }
            }
        }

        // Пагинация
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allCourses.size());
        return new PageImpl<>(allCourses.subList(start, end), pageable, allCourses.size());
    }
}