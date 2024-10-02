package sakura.iri2.controller;

import sakura.iri2.model.CourseModel;
import sakura.iri2.service.InMemoryCourseImpl;
import sakura.iri2.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(InMemoryCourseImpl courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String getCourseAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filterName,
            @RequestParam(required = false) String filterDescription,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CourseModel> coursePage = courseService.findAllCoursesWithPaginationAndFilters(
                pageable, filterName, filterDescription, isDeleted, sortField, sortDirection);

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        return "coursePage";
    }

    @GetMapping("/create")
    public String getCreateCourse() {
        return "createCourse";
    }

    @PostMapping("/create")
    public String postCreateCourse(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description
    ) {
        courseService.createCourse(new CourseModel(name, description));
        return "redirect:/course";
    }

    @GetMapping("/edit/{id}")
    public String getEditCourse(@PathVariable UUID id, Model model) {
        var course = courseService.findCourseById(id);
        model.addAttribute("course", course);
        return "editCourse";
    }

    @PostMapping("/edit/{id}")
    public String postEditCourse(
            @PathVariable UUID id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description
    ) {
        var course = new CourseModel(name, description);
        course.setId(id);
        courseService.updateCourse(course);
        return "redirect:/course";
    }

    // Логическое удаление
    @PostMapping("/logicdelete")
    public String logicDeleteCourses(@RequestParam List<UUID> ids) {
        courseService.logicDeleteCourses(ids);
        return "redirect:/course";
    }

    // Физическое удаление
    @PostMapping("/physdelete")
    public String deleteCourses(@RequestParam List<UUID> ids) {
        courseService.deleteCourses(ids);
        return "redirect:/course";
    }
}