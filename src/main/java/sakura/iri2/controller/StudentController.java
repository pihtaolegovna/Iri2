package sakura.iri2.controller;

import sakura.iri2.model.StudentModel;
import sakura.iri2.service.InMemoryStudentImpl;
import sakura.iri2.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(InMemoryStudentImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getStudentAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filterName,
            @RequestParam(required = false) String filterEmail,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<StudentModel> studentPage = studentService.findAllStudentsWithPaginationAndFilters(
                pageable, filterName, filterEmail, isDeleted, sortField, sortDirection);

        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        return "studentPage";
    }

    @GetMapping("/create")
    public String getCreateStudent() {
        return "createStudent";
    }

    @PostMapping("/create")
    public String postCreateStudent(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password
    ) {
        studentService.createStudent(new StudentModel(name, email, password));
        return "redirect:/student";
    }

    @GetMapping("/edit/{id}")
    public String getEditStudent(@PathVariable UUID id, Model model) {
        var student = studentService.findStudentById(id);
        model.addAttribute("student", student);
        return "editStudent";
    }

    @PostMapping("/edit/{id}")
    public String postEditStudent(
            @PathVariable UUID id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password
    ) {
        var student = new StudentModel(name, email, password);
        student.setId(id);
        studentService.updateStudent(student);
        return "redirect:/student";
    }

    // Логическое удаление
    @PostMapping("/logicdelete")
    public String logicDeleteStudents(@RequestParam List<UUID> ids) {
        studentService.logicDeleteStudents(ids);
        return "redirect:/student";
    }

    // Физическое удаление
    @PostMapping("/physdelete")
    public String deleteStudents(@RequestParam List<UUID> ids) {
        studentService.deleteStudents(ids);
        return "redirect:/student";
    }
}