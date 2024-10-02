package sakura.iri2.repository;

import sakura.iri2.model.CourseModel;
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
public class CourseRepository {

    private final List<CourseModel> COURSES = new ArrayList<>();

    public Page<CourseModel> findAllWithPaginationAndFilters(
            Pageable pageable, String name, String description, Boolean isDeleted, String sortField, String sortDirection) {

        var filteredCourses = COURSES.stream()
                .filter(course -> (name == null || course.getName().contains(name)) &&
                        (description == null || course.getDescription().contains(description)) &&
                        (isDeleted == null || course.isDeleted() == isDeleted))
                .sorted((s1, s2) -> {
                    if (sortField.equals("name")) {
                        return sortDirection.equals("asc") ? s1.getName().compareTo(s2.getName()) : s2.getName().compareTo(s1.getName());
                    } else if (sortField.equals("description")) {
                        return sortDirection.equals("asc") ? s1.getDescription().compareTo(s2.getDescription()) : s2.getDescription().compareTo(s1.getDescription());
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), filteredCourses.size());
        int end = Math.min((start + pageable.getPageSize()), filteredCourses.size());

        return new PageImpl<>(filteredCourses.subList(start, end), pageable, filteredCourses.size());
    }

    public CourseModel findCourseById(UUID id) {
        return COURSES.stream()
                .filter(course -> course.getId().equals(id) && !course.isDeleted())
                .findFirst()
                .orElse(null);
    }

    public CourseModel createCourse(CourseModel course) {
        COURSES.add(course);
        return course;
    }

    public CourseModel updateCourse(CourseModel course) {
        var courseIndex = IntStream.range(0, COURSES.size())
                .filter(index -> COURSES.get(index).getId().equals(course.getId()))
                .findFirst()
                .orElse(-1);
        if (courseIndex == -1) {
            return null;
        }
        COURSES.set(courseIndex, course);
        return course;
    }

    public void logicDeleteCourses(List<UUID> ids) {
        ids.forEach(id -> {
            var course = findCourseById(id);
            if (course != null) {
                course.setDeleted(true);
            }
        });
    }

    public void deleteCourses(List<UUID> ids) {
        COURSES.removeIf(course -> ids.contains(course.getId()));
    }

    public List<CourseModel> findAllCourse() {
        return new ArrayList<>(COURSES);
    }

    public void deleteCourse(UUID id) {
        return;
    }
}